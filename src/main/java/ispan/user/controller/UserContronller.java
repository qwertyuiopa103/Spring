package ispan.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
@RequestMapping("/api/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserContronller {
	private UserServiceImpl userServiceImpl;

	@Autowired
	public UserContronller(UserServiceImpl theUserServiceImpl, UserLoginService theuserLoginService) {
		userServiceImpl = theUserServiceImpl;

	}

	@GetMapping("/profile")
	public ResponseEntity<Map<String, Object>> getUserProfile() {
		// 從 SecurityContextHolder 中獲取當前用戶的身份信息
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = userDetails.getUsername(); // 假設 username 存儲的是 userID

		// 根據 userId 查詢用戶信息
		UserBean user = userServiceImpl.queryOne(userId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		// 構造響應數據
		Map<String, Object> response = new HashMap<>();
		response.put("id", user.getUserID());
		response.put("name", user.getUserName());
		response.put("email", user.getUserEmail());
		response.put("phone", user.getUserPhone());
		response.put("address", user.getUserAddress());
		response.put("city", user.getUserCity());
		response.put("district", user.getUserDistrict());
		response.put("role", user.getUserRole());
		// 動態將 `byte[]` 轉換為 Base64 格式
		String avatarBase64 = (user.getUserPhoto() != null)
				? CommonTool.convertByteArrayToBase64String(user.getUserPhoto())
				: null;

		response.put("avatar", avatarBase64);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/update")
	public ResponseEntity<?> userUpdate(@RequestParam("id") String userID, @RequestParam("name") String userName,
			@RequestParam("email") String userEmail, @RequestParam("phone") String userPhone,
			@RequestParam("city") String userCity, @RequestParam("district") String userDistrict,
			@RequestParam("address") String userAddress,
			@RequestParam(value = "userPhoto", required = false) MultipartFile userPhoto) {
		try {
			// 1) 從資料庫查詢該使用者
			Optional<UserBean> optionalUser = userServiceImpl.findById(userID);
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到此使用者");
			}
			UserBean user = optionalUser.get();

			// 2) 更新文字欄位
			user.setUserName(userName);
			user.setUserEmail(userEmail);
			user.setUserPhone(userPhone);
			user.setUserCity(userCity);
			user.setUserDistrict(userDistrict);
			user.setUserAddress(userAddress);

			// 3) 更新照片（若有上傳）
			if (userPhoto != null && !userPhoto.isEmpty()) {
				// 檔案大小、類型檢查（可自行加）
				byte[] photoBytes = userPhoto.getBytes();
				user.setUserPhoto(photoBytes);
			} else {
				System.out.println("userPhoto 為空或未上傳");
			}

			// 4) 呼叫 service 儲存
			UserBean updatedUser = userServiceImpl.insertAndUpate(user);

			// 5) 回傳更新後的資料或成功訊息
			return ResponseEntity.ok(updatedUser);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新時發生錯誤");
		}
	}
	//假刪除
	@DeleteMapping("/users/{userID}")
	public ResponseEntity<String> delete(@PathVariable String userID) {
		// 從 SecurityContextHolder 中獲取當前用戶的身份信息
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String userId = userDetails.getUsername(); // 假設 username 存儲的是 userID
		UserBean user = userServiceImpl.queryOne(userID);
		if (user == null) {
            throw new RuntimeException("User id not found - " + userID);
        }
		userServiceImpl.softDelete(userID);
		return ResponseEntity.ok("User status delete successfully.");
	}
}
