package ispan.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ispan.user.model.UserBean;
import ispan.user.service.UserLoginService;

@RestController
@RequestMapping("/api/LoginController")
public class AuthController {

	private UserLoginService userLoginService;

	@Autowired
	public AuthController(UserLoginService theuserLoginService) {
		userLoginService = theuserLoginService;
	}
//一般使用者
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
		String useraccount = request.get("useraccount");
	    String userpassword = request.get("userpassword");
	    
		String token = userLoginService.login(useraccount, userpassword);
		// 獲取用戶資料
	    Optional<UserBean> userOpt =userLoginService.findByPhoneOrEmail(useraccount);
	    if (userOpt.isPresent()) {
	        UserBean user = userOpt.get();

	        Map<String, Object> responseBody = new HashMap<>();
	        responseBody.put("id", user.getUserID()); // 返回用戶 ID
	        responseBody.put("token", token); // 返回 JWT Token
	        responseBody.put("role", user.getUserRole());
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token); // 可選，用於方便前端存儲

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(responseBody);
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號不存在或密碼錯誤");
	    }
	}
	//管理員
	@PostMapping("/adminlogin")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> request) {
		String useraccount = request.get("useraccount");
	    String userpassword = request.get("userpassword");

	    String token = userLoginService.validateAdmin(useraccount ,userpassword );
	    Optional<UserBean> userOpt=userLoginService.findByPhoneOrEmail(useraccount);
        if (userOpt.isPresent()) {
            // 如果驗證成功，可返回簡單的成功訊息
        	 UserBean user = userOpt.get();

 	        Map<String, Object> responseBody = new HashMap<>();
 	        responseBody.put("id", user.getUserID()); // 返回用戶 ID
 	        responseBody.put("token", token); // 返回 JWT Token
 	       responseBody.put("role", user.getUserRole()); // 返回角色
 	        HttpHeaders headers = new HttpHeaders();
 	        headers.set("Authorization", "Bearer " + token); // 可選，用於方便前端存儲

 	        return ResponseEntity.ok()
 	                .headers(headers)
 	                .body(responseBody);
           
        } else {
            // 驗證失敗
            return ResponseEntity.status(401).body("Invalid admin credentials.");
        }
    }
}
