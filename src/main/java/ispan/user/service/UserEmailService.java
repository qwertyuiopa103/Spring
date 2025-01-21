package ispan.user.service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ispan.user.exception.AccountNotVerifiedException;
import ispan.user.exception.AccountPasswordChangeException;
import ispan.user.exception.InvalidCredentialsException;
import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.tools.BCryptEncryptionTool;

@Service
public class UserEmailService {

	@Autowired
    private JavaMailSender mailSender;
	
	private UserRepository userRepository;

	@Autowired
	public UserEmailService(UserRepository theUserRepository) {
		userRepository = theUserRepository;
	}
	
	@Value("${frontend.url}")
	private String frontendUrl;
	
	private static final int LOCKOUT_TIME_MINUTES = 2;

    public void sendVerificationEmail(String to, String verification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("心護家_驗證電子信箱");
        message.setText("請使用以下驗證碼完成信箱驗證: " + verification);
        mailSender.send(message);
    }
    
    public void sendPaaawordMail(String to, String verification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("心護家_更改密碼");
        message.setText("請點擊以下連結網址以更改密碼 : " + verification);
        mailSender.send(message);
    }
    
    public String VerificationEmail(String email,String token) {
    Optional<UserBean> user = userRepository.findByUserEmail(email);
    if (user.isPresent()) {
    	UserBean verifiedUser = user.get();
        if (verifiedUser.getUserVerificationToken().equalsIgnoreCase(token)) {
        	 verifiedUser.setUserVerified(true); // 更新為已驗證狀態
            userRepository.save(verifiedUser);
            return "驗證成功！";
        } else {
        	System.out.println("sudd2");
        	throw new AccountNotVerifiedException("驗證碼錯誤！");
        }
    } else {
    	System.out.println("err");
    	throw new InvalidCredentialsException("無此用戶");
    }
    }
   

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // 生成 1000-9999 之間的數字
        return String.valueOf(code);
    }

    public String resend(String email){
    	 String verificationCode = generateVerificationCode();
    	  Optional<UserBean> user = userRepository.findByUserEmail(email);
    	    if (user.isPresent()) {
    	    	UserBean verifiedUser = user.get();
    	    	verifiedUser.setUserVerificationToken(verificationCode);
    	    	 userRepository.save(verifiedUser);
    	    	 sendVerificationEmail(email,verificationCode);
    	         return "重發成功！";
    	    }
    	    throw new AccountNotVerifiedException("重發失敗！");
    }
    
    
    public String sendPasswordToke(String email) {
    	 Optional<UserBean> user = userRepository.findByUserEmail(email);
  	    if (user.isPresent()) {
  	      UserBean findUser = user.get();
	      String token = UUID.randomUUID().toString();  
	      String verificationLink = frontendUrl + "/userchangepassword/" + token;
	      sendPaaawordMail(findUser.getUserEmail(), verificationLink);
	      findUser.setUserResetPasswordToken(token);
	      findUser.setUserResetPasswordExpires(new Timestamp(System.currentTimeMillis() + (LOCKOUT_TIME_MINUTES * 60 * 1000)));
	      userRepository.save(findUser);
	      return "送發成功！";
  	    }else {
  	    	throw new AccountPasswordChangeException("查無此帳戶");
		}
  	
    }
    
    public String verificationPassword(String token,String password) {
    	Optional<UserBean> user = userRepository.findByUserResetPasswordTokenAndUserResetPasswordExpiresAfter(
    	        token, new Timestamp(System.currentTimeMillis())
    	    );
    	    if (user.isEmpty()) {
    	        throw new AccountPasswordChangeException("無效的 token 或已過期");
    	    }
    	    UserBean verifiedUser = user.get();
        verifiedUser.setUserPasswordChanged(new Timestamp(System.currentTimeMillis()));
        verifiedUser.setUserPassword(BCryptEncryptionTool.encrypt(password));
        verifiedUser.setUserResetPasswordToken(null);
        verifiedUser.setUserResetPasswordExpires(null);
        userRepository.save(verifiedUser);
        return "驗證成功";
    }
}
