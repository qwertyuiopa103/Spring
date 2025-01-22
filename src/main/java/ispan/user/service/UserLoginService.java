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
import ispan.user.tools.BCryptEncryptionTool;
import ispan.user.tools.JwtTool;
@Service
public class UserLoginService {

	private UserRepository userRepository;

	@Autowired
	public UserLoginService(UserRepository theUserRepository) {
		userRepository = theUserRepository;
	}
	//三次
	private static final int MAX_FAILED_ATTEMPTS = 3;
	private static final int LOCKOUT_TIME_MINUTES = 1;

	public String login(String account, String userPassword) {
		// 使用JPA的findByPhoneOrEmail方法查找用戶
	
		Optional<UserBean> userOptional = userRepository.findByPhoneOrEmail(account);

		if (userOptional.isPresent()) {
			UserBean user = userOptional.get();

			if (user.isUserDeleted()) {
	            // 視為帳號不存在
	            throw new InvalidCredentialsException("帳號不存在或密碼錯誤");
	        }
			
			 if (!user.isUserActive()) {
		            // 帳號被禁用
		            throw new AccountDisabledException("帳號被禁用，請洽客服");
		        }

			 if (!user.isUserVerified()) {
		            // 尚未驗證信箱
		            throw new AccountNotVerifiedException("信箱尚未認證");
		        }
			
			String storedPassword = user.getUserPassword();
			Integer failedAttempts = user.getUserFailedLoginAttempts();
			Timestamp lockoutEnd = user.getUserLockoutEnd();

			boolean userIsLocked = (lockoutEnd != null && lockoutEnd.after(new Timestamp(System.currentTimeMillis())));

			if (userIsLocked) {
				 throw new AccountLockedException("您的帳號因嘗試登錄次數過多而被鎖定，請稍後再嘗試。"); // 用戶被鎖定
			}
			// 使用 BCrypt 進行驗證
	        boolean matches = BCryptEncryptionTool.verify(userPassword, storedPassword);
			// 密碼正確，重置錯誤登錄次數並更新登錄時間
			if (matches && !userIsLocked) {
				user.setUserLogin(new Timestamp(System.currentTimeMillis()));
				user.setUserFailedLoginAttempts(0);
				user.setUserLockoutEnd(null);
				userRepository.save(user); // 使用JPA保存更新的UserBean
				String token = JwtTool.generateToken(user.getUserID(), user.getUserRole());
				return token;  // 登錄成功
			} else {
				failedAttempts++;
				if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
					// 鎖定用戶
					user.setUserFailedLoginAttempts(failedAttempts);
					user.setUserLockoutEnd(
							new Timestamp(System.currentTimeMillis() + (LOCKOUT_TIME_MINUTES * 60 * 1000)));
					userRepository.save(user); // 更新用戶
					throw new AccountLockedException("您的帳號因嘗試登錄次數過多而被鎖定，請稍後再嘗試。"); // 登錄失敗並鎖定
				} else {
					user.setUserFailedLoginAttempts(failedAttempts);
					user.setUserLockoutEnd(null);
					userRepository.save(user); // 更新用戶
					throw new InvalidCredentialsException("帳號不存在或密碼錯誤");  // 密碼錯誤
				}
			}
		} else {
			throw new InvalidCredentialsException("帳號不存在或密碼錯誤"); // 找不到用戶
		}
	}
	
	public Optional<UserBean> findByPhoneOrEmail(String account){
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
