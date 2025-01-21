package ispan.user.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Value("${file.upload.userPic.dir}")
	private String saveFileDir;
	
	private UserRepository userRepository;
	private UserEmailService  userEmailService ;
	
	@Autowired
	public UserServiceImpl(UserRepository theUserRepository,UserEmailService theUserEmailService) {
		userRepository = theUserRepository;
		userEmailService = theUserEmailService;
	}
	
	@Override
	public UserBean queryOne(String userID) {
		Optional<UserBean> result = userRepository.findById(userID);
		
		UserBean userBean = null;
		
		if (result.isPresent()) {
			userBean = result.get();
		}else {
			throw new RuntimeException("Did not find employee id - " + userID);
		}
		
		return userBean;
	}

	@Override
	public List<UserBean> queryAll() {
		return userRepository.findAll();
	}

	@Override
	public UserBean insertAndUpate(UserBean user) {
		if ((user.getUserID()) == null) {
			if (user.isUserVerified()) {
				generateUserId(user);
			}else {
            generateUserId(user);
//            String token = UUID.randomUUID().toString();  
//            String verificationLink = frontendUrl + "/userCheck/verify?token=" + token;
//            userEmailService.sendVerificationEmail(user.getUserEmail(), verificationLink);
//            user.setUserVerificationToken(token);
            String verificationCode = userEmailService.generateVerificationCode();
            user.setUserVerificationToken(verificationCode); // 保存驗證碼到資料庫
            userEmailService.sendVerificationEmail(user.getUserEmail(), verificationCode);
			}
        } else {
        	Optional<UserBean> result = userRepository.findById(user.getUserID());
        	UserBean userBean = result.get();
        	user.setUserLogin(userBean.getUserLogin());
        	user.setUserLockoutEnd(userBean.getUserLockoutEnd());
        	user.setUserFailedLoginAttempts(userBean.getUserFailedLoginAttempts());
        	user.setUserVerified(userBean.isUserVerified());
        	user.setUserRole(userBean.getUserRole());
        	user.setUserEnabled(userBean.isUserEnabled());
        	user.setUserActive(userBean.isUserActive());
        	user.setUserDeleted(userBean.isUserDeleted());
            user.setUserUpdated(new Timestamp(System.currentTimeMillis()));
        }
		return userRepository.save(user);
	}

	
	public void softDelete(String userID) {
		Optional<UserBean> result = userRepository.findById(userID);
		if (result.isPresent()) {
    	UserBean userBean = result.get();
    	boolean deleted = true;
    	userBean.setUserDeleted(deleted);
		userRepository.updateUserDeleted(deleted, userID);
		} else {
	        throw new EntityNotFoundException("User not found with ID: " + userID);
	    }
	}
	
	@Override
	public void delete(String userID) {
		Optional<UserBean> result = userRepository.findById(userID);
		if (result.isPresent()) {
    	UserBean userBean = result.get();
    	userRepository.deleteById(userID);
		} else {
	        throw new EntityNotFoundException("User not found with ID: " + userID);
	    }
	}
	
	//1的話代表true更新成功
	public boolean updateUserStatus(Boolean userActive, String userID) {
        int rowsAffected = userRepository.updateUserStatusByUserID(userActive, userID);
        return rowsAffected > 0;
    }
	
	public boolean checkEmail(String userEmail) {
		boolean email = userRepository.existsByUserEmail(userEmail);
		return email;
	}
	
	public boolean checkPhone(String userPhone) {
		boolean phone = userRepository.existsByUserPhone(userPhone);
		return phone;
	}
	
	public void generateUserId(UserBean user) {
		// 獲取下一個序列值
		Long seqValue = userRepository.getNextSequenceValue();
		// 根據序列值生成 userID
		user.setUserID("USR" + String.format("%04d", seqValue));
	}
	
	public long getUserCount() {
		return userRepository.countByExcludingUserDeleted();
	}
	
	public Optional<UserBean> findById(String userID) {
		Optional<UserBean> optionalUser = userRepository.findById(userID);
		return optionalUser;
	}
	 
	public Optional<UserBean> findByMail(String mail){
		Optional<UserBean> optionalUser = userRepository.findByUserEmail(mail);
		return optionalUser;
	}
	
}