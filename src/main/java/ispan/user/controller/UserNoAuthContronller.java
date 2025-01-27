package ispan.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.service.UserEmailService;
import ispan.user.service.UserLoginService;
import ispan.user.service.UserServiceImpl;
import ispan.user.tools.BCryptEncryptionTool;

@RestController
@RequestMapping("/api/UserNoAuth")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserNoAuthContronller {

	
	private UserServiceImpl userServiceImpl;

	private UserEmailService userEmailService;
	
	private UserLoginService userLoginService;

	@Autowired
	public UserNoAuthContronller(UserServiceImpl theUserServiceImpl,UserEmailService theUserEmailService,
			UserLoginService theuserLoginService) {
		userServiceImpl = theUserServiceImpl;
		userEmailService = theUserEmailService;
		userLoginService = theuserLoginService;
	}
	
	@GetMapping("/user/emailCheck")
	public boolean emailCheck(@RequestParam("userEmail")String userEmail) {
		boolean checkEmail = userServiceImpl.checkEmail(userEmail);
		return checkEmail;
	}
	
	@GetMapping("/user/phoneCheck")
	public boolean phoneCheck(@RequestParam("userPhone")String userPhone) {
		boolean checkPhone = userServiceImpl.checkPhone(userPhone);
		return checkPhone;
	}
	
	@GetMapping("/user/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String email,@RequestParam String token) {
		try {
	        String verifyEmail = userEmailService.VerificationEmail(email, token);
	        return ResponseEntity.ok(verifyEmail);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("驗證失敗：" + e.getMessage());
	    }
	}
	
	@PatchMapping("/user/resend")
    public ResponseEntity<String> resendEmail(@RequestParam String email) {
		String resendEmail =  userEmailService.resend(email);
		return ResponseEntity.ok(resendEmail);
    }
	
	@PostMapping("/user/register")
	public ResponseEntity<String> userCreate(
            @RequestParam("userName") String userName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("userPhone") String userPhone,
            @RequestParam("userCity") String userCity,
            @RequestParam("userDistrict") String userDistrict,
            @RequestParam("userAddress") String userAddress,
            @RequestParam(value = "userPhoto", required = false) MultipartFile userPhoto,
            @RequestParam(value = "isOAuth", defaultValue = "false") boolean isOAuth
            ) {
        try {
           
            // 創建新的 UserBean
            UserBean user = new UserBean();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            String encryptedPassword = BCryptEncryptionTool.encrypt(userPassword);
            user.setUserPassword(encryptedPassword);
            user.setUserPhone(userPhone);
            user.setUserCity(userCity);
            user.setUserDistrict(userDistrict);
            user.setUserAddress(userAddress);
            user.setUserRole("ROLE_USER");
            if (isOAuth) {
                user.getUserSecurity().setUserVerified(true); // 跳過信箱驗證
            } else {
                user.getUserSecurity().setUserVerified(false); // 需要後續驗證
            }
            // 處理照片
            if (userPhoto != null && !userPhoto.isEmpty()) {
//             
                // 將 MultipartFile 轉換為 byte[]
                byte[] photoBytes = userPhoto.getBytes();
                user.setUserPhoto(photoBytes);
            }

            
            // 儲存到資料庫
            userServiceImpl.insertAndUpate(user);

            return ResponseEntity.ok("使用者註冊成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤");
        }
    }
	
	@GetMapping("/user/userCount")
	public long getUserCount() {
		return userServiceImpl.getUserCount();
	}
	
	@GetMapping("/user/caregiverCount")
	public long getCaregiverCount() {
		return userServiceImpl.getCaregiverCount();
	}
	
	@PatchMapping("/user/sendPassword")
    public ResponseEntity<String> sendPassword(@RequestParam String email) {
		String sendPasswordToke =  userEmailService.sendPasswordToke(email);
		return ResponseEntity.ok(sendPasswordToke);
    }
	
	@PatchMapping("/user/verificationPassword/{token}")
    public ResponseEntity<String>  verificationPassword(@PathVariable String token,@RequestParam String password) {
		String passwordToken =  userEmailService.verificationPassword(token,password);
		return ResponseEntity.ok(passwordToken);
    }
}
