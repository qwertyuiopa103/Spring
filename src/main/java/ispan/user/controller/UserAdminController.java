package ispan.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ispan.user.model.UserBean;
import ispan.user.service.UserLoginService;
import ispan.user.service.UserServiceImpl;
import ispan.user.tools.CommonTool;

@RestController
@RequestMapping("/api/UserAdmin")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserAdminController {
	private UserServiceImpl userServiceImpl;
	private UserLoginService userLoginService;

	@Autowired
	public UserAdminController(UserServiceImpl theUserServiceImpl, UserLoginService theuserLoginService) {
		userServiceImpl = theUserServiceImpl;
		userLoginService = theuserLoginService;
	}
	
	@GetMapping("/users/{userID}")
	public ResponseEntity<?> userById(@PathVariable String userID) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getPrincipal();

	    // 檢查是否具有 ROLE_ADMIN
	    boolean isAdmin = userDetails.getAuthorities().stream()
	        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

	    if (!isAdmin) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您無權訪問此資源");
	    }
		UserBean user = userServiceImpl.queryOne(userID);
		if (user == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户 ID 未找到: " + userID);
        }
        return ResponseEntity.ok(user);
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		// 從 SecurityContext 獲取當前用戶信息
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
		            .getAuthentication()
		            .getPrincipal();

		    // 檢查是否具有 ROLE_ADMIN
		    boolean isAdmin = userDetails.getAuthorities().stream()
		        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		    if (!isAdmin) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您無權訪問此資源");
		    }

	    // 如果是管理員，返回用戶列表
	    List<UserBean> users = userServiceImpl.queryAll();
	    return ResponseEntity.ok(users);
	}

	
	@DeleteMapping("/users/{userID}")
	public ResponseEntity<String> delete(@PathVariable String userID) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getPrincipal();

	    // 檢查是否具有 ROLE_ADMIN
	    boolean isAdmin = userDetails.getAuthorities().stream()
	        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

	    if (!isAdmin) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您無權訪問此資源");
	    }
		UserBean user = userServiceImpl.queryOne(userID);
		if (user == null) {
            throw new RuntimeException("User id not found - " + userID);
        }
		userServiceImpl.delete(userID);
		return ResponseEntity.ok("User status delete successfully.");
	}
	
	@PatchMapping("/users/{userID}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable String userID,
            @RequestBody Map<String, Integer> requestBody) {
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
		            .getAuthentication()
		            .getPrincipal();

		    // 檢查是否具有 ROLE_ADMIN
		    boolean isAdmin = userDetails.getAuthorities().stream()
		        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		    if (!isAdmin) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您無權訪問此資源");
		    }

		Integer userStatus = requestBody.get("userStatus");
        if (userStatus == null || (userStatus != 0 && userStatus != 1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid userStatus value. Must be 0 or 1.");
        }

        Boolean newStatus = userStatus == 1;

        boolean isUpdated = userServiceImpl.updateUserStatus(newStatus, userID);

        if (isUpdated) {
            return ResponseEntity.ok("User status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with userID " + userID + " not found.");
        }
    }
	
	@GetMapping("/users/avatar")
	public ResponseEntity<?> getUserAvatar() {
	    // 从 SecurityContext 获取用户信息
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    boolean isAdmin = userDetails.getAuthorities().stream()
		        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		    if (!isAdmin) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您無權訪問此資源");
		    }

	    String userId = userDetails.getUsername(); // 假设 username 存储的是 userID

	    // 查询用户
	    UserBean user = userServiceImpl.queryOne(userId);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户未找到");
	    }

	    // 返回头像信息
	    Map<String,String> response = new HashMap<>();
	    response.put("name", user.getUserName());
	    String avatarBase64 = (user.getUserPhoto() != null)
				? CommonTool.convertByteArrayToBase64String(user.getUserPhoto())
				: null;
		response.put("avatar", avatarBase64);
	  
	    return ResponseEntity.ok(response);
	}
	
}
