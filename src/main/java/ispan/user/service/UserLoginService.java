package ispan.user.service;

import java.sql.Timestamp;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ispan.user.exception.AccountDisabledException;
import ispan.user.exception.AccountLockedException;
import ispan.user.exception.AccountNotVerifiedException;
import ispan.user.exception.InvalidCredentialsException;
import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.model.UserSecurityBean;
import ispan.user.model.UserSecurityRepository;
import ispan.user.tools.BCryptEncryptionTool;
import ispan.user.tools.JwtTool;

@Service
public class UserLoginService {

	private UserRepository userRepository;
	private UserSecurityRepository userSecurityRepository;

	@Autowired
	public UserLoginService(UserRepository theUserRepository, UserSecurityRepository theuserSecurityRepository) {
		userRepository = theUserRepository;
		userSecurityRepository = theuserSecurityRepository;
	}

	private static final int MAX_FAILED_ATTEMPTS = 3;

	private static final int LOCKOUT_TIME_SECONDS = 10;

	 public String login(String account, String userPassword) {
	        // 使用JPA的findByPhoneOrEmail方法查找用戶
	        Optional<UserBean> userOptional = userRepository.findByPhoneOrEmail(account);

	        if (userOptional.isPresent()) {
	            UserBean user = userOptional.get();
	            // 若關聯的 UserSecurity 尚未綁定用戶資料，則綁定之
	            user.getUserSecurity().setUsers(user);
	            if (user.getUserSecurity().isUserDeleted()) {
	                // 視為帳號不存在
	                throw new InvalidCredentialsException("帳號已刪除");
	            }

	            if (!user.getUserSecurity().isUserActive()) {
	                // 帳號被禁用
	                throw new AccountDisabledException("帳號被禁用，請洽客服");
	            }

	            if (!user.getUserSecurity().isUserVerified()) {
	                // 尚未驗證信箱
	                throw new AccountNotVerifiedException("信箱尚未認證");
	            }

	            String storedPassword = user.getUserPassword();
	            Integer failedAttempts = user.getUserSecurity().getUserFailedLoginAttempts();
	            Timestamp lockoutEnd = user.getUserSecurity().getUserLockoutEnd();
	            Timestamp now = new Timestamp(System.currentTimeMillis());

	            // 若鎖定時間已過，重置失敗次數與鎖定時間
	            if (lockoutEnd != null && now.after(lockoutEnd)) {
	                failedAttempts = 0;
	                user.getUserSecurity().setUserFailedLoginAttempts(0);
	                user.getUserSecurity().setUserLockoutEnd(null);
	                userRepository.save(user);
	            }

	            // 重新取得是否仍在鎖定狀態 (若鎖定時間未到, lockoutEnd 仍有效)
	            boolean userIsLocked = (user.getUserSecurity().getUserLockoutEnd() != null 
	                                    && user.getUserSecurity().getUserLockoutEnd().after(now));

	            if (userIsLocked) {
	                throw new AccountLockedException("您的帳號因嘗試登錄次數過多而被鎖定，請稍後再嘗試。");
	            }

	            // 使用 BCrypt 進行密碼驗證
	            boolean matches = BCryptEncryptionTool.verify(userPassword, storedPassword);
	            if (matches) {
	                // 密碼正確，重置失敗登錄次數與鎖定狀態，並更新登錄時間
	                user.getUserSecurity().setUserLogin(now);
	                user.getUserSecurity().setUserFailedLoginAttempts(0);
	                user.getUserSecurity().setUserLockoutEnd(null);
	                userRepository.save(user);
	                String token = JwtTool.generateToken(user.getUserID(), user.getUserRole());
	                return token;
	            } else {
	                // 密碼錯誤，累計失敗次數
	                failedAttempts++;
	                user.getUserSecurity().setUserFailedLoginAttempts(failedAttempts);

	                // 若失敗次數已達上限則重新鎖定
	                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
	                    user.getUserSecurity().setUserLockoutEnd(
	                        new Timestamp(now.getTime() + (LOCKOUT_TIME_SECONDS  * 1000))
	                    );
	                    userRepository.save(user);
	                    throw new AccountLockedException("您的帳號因嘗試登錄次數過多而被鎖定，請稍後再嘗試。");
	                } else {
	                    // 失敗次數未達上限，顯示密碼錯誤提示
	                    user.getUserSecurity().setUserLockoutEnd(null);
	                    userRepository.save(user);
	                    throw new InvalidCredentialsException("帳號不存在或密碼錯誤");
	                }
	            }
	        } else {
	            throw new InvalidCredentialsException("帳號不存在或密碼錯誤");
	        }
	    }
	public Optional<UserBean> findByPhoneOrEmail(String account) {
		return userRepository.findByPhoneOrEmail(account);
	}

	public String validateAdmin(String account, String password) {
		Optional<UserBean> userOptional = userRepository.findByPhoneOrEmail(account);

		if (userOptional.isPresent()) {

			UserBean user = userOptional.get();

			if (!"ROLE_ADMIN".equals(user.getUserRole())) {
				throw new InvalidCredentialsException("非管理員帳號");
			}

			// 檢查密碼是否正確
			boolean matches = BCryptEncryptionTool.verify(password, user.getUserPassword());
			if (!matches) {
				throw new InvalidCredentialsException("密碼錯誤");
			}
			String token = JwtTool.generateToken(user.getUserID(), user.getUserRole());
			return token;
		} else {
			throw new InvalidCredentialsException("帳號或密碼錯誤");
		}
	}

}
