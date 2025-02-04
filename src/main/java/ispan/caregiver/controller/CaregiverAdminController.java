//package ispan.caregiver.controller;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import ispan.caregiver.model.CaregiverBean;
//import ispan.caregiver.service.CaregiverServiceImpl;
//import ispan.event.model.EventBean;
//import ispan.event.service.EventServiceImpl;
//@RestController
//@RequestMapping("/api/CaregiverAdmin")
//public class CaregiverAdminController {
//	@Autowired
//	private CaregiverServiceImpl caregiverService;
//
////	@Autowired
////	public caregiverController(caregiverService caregiverService) {
////		caregiverService = caregiverService;
////	}
//
//	// http://localhost:8080/caregiver/caregiverAll
//	  // 查詢所有護理人員
//	@GetMapping("/FindAllCaregiver")
//	public List<CaregiverBean> findAllCaregiver(Model model) {
//		List<CaregiverBean> caregiver = caregiverService.findAllCaregivers();
////		model.addAttribute("caregivers", caregiver);
//		System.out.println(caregiver.size());
//		return caregiver;
//	}
//
//    
//    
//
//	@GetMapping(path = "/FindCaregiver", produces = "application/json")@ResponseBody
//    public ResponseEntity<?> findCaregiver(@RequestParam("caregiverNO") Integer caregiverNO) {
//        System.out.println("Received caregiverNO: " + caregiverNO); // 日誌輸出接收到的參數
//
//        try {
//            // 單表查詢，只根據 caregiverNO 進行操作
//            CaregiverBean caregiver = caregiverService.findCaregiver(caregiverNO);
//
//            if (caregiver != null) {
//                return ResponseEntity.ok(caregiver); // 查詢成功，返回 JSON 格式數據
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(Map.of("error", "未找到相關護理人員")); // 查無資料，返回錯誤訊息
//            }
//        } catch (Exception e) {
//            System.err.println("Error occurred while finding caregiver: " + e.getMessage()); // 錯誤日誌輸出
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "查詢失敗：" + e.getMessage())); // 返回伺服器錯誤訊息
//        }
//    }
//
//	
//	@GetMapping("/GetThatUpdateCaregiver")@ResponseBody
//	public String getThatUpdateCaregiver(@RequestParam("caregiverNO") Integer caregiverNO, Model model) {
//
//		CaregiverBean caregiver = caregiverService.findCaregiver(caregiverNO);
//
//		model.addAttribute("caregiver", caregiver);
//
//		return "caregiverView/UpdateCaregiver";
//	}
//	
//	@PostMapping("/InsertCaregiver") 
//	public ResponseEntity<?> insertCaregiver(@RequestBody CaregiverBean caregiver) {
//	    try {
//	        // 創建新的 CaregiverBean 實例
////	        CaregiverBean caregiver = new CaregiverBean();
////	        
////	        // 設置 UserBean (只設置 userID)
////	        UserBean user = new UserBean();
////	        user.setUserID(formData.get("userID")); // 只需要設置關聯的 userID
////	        caregiver.setUser(user);
////	        System.out.println(caregiver.getUser().getUserID());
////	        // 設置 Caregiver 表的欄位
////	        caregiver.setCaregiverGender(formData.get("caregiverGender"));
////	        caregiver.setCaregiverAge(Integer.parseInt(formData.get("caregiverAge")));
////	        caregiver.setExpYears(Integer.parseInt(formData.get("expYears")));
////	        caregiver.setEduExperience(formData.get("eduExperience"));
////	        caregiver.setHourlyRate(Double.parseDouble(formData.get("hourlyRate")));
//	    	
//	        CaregiverBean savedCaregiver = caregiverService.insertCaregiver(caregiver);
//	        return ResponseEntity.ok()
//	            .body(Map.of("message", "新增成功", "caregiver", savedCaregiver));
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	            .body(Map.of("error", "新增失敗：" + e.getMessage()));
//	    }
//	}
//	
//
////	@PostMapping("/InsertCaregiver") 
////	public ResponseEntity<?> insertCaregiver(@RequestParam Map<String, String> formData,
////	                                       @RequestParam(required = false) MultipartFile caregiverPicture) {
////	    try {
////	        // 創建新的 CaregiverBean 實例
////	        CaregiverBean caregiver = new CaregiverBean();
////	        UserBean user = new UserBean();
////	        user.setUserID(formData.get("userID"));
////	        caregiver.setUser(user);
////	        caregiver.setExpYears(Integer.parseInt(formData.get("expYears")));
////	        caregiver.setEduExperience(formData.get("eduExperience"));
////	        caregiver.setHourlyRate(Double.parseDouble(formData.get("hourlyRate")));
////	        
////	        // 手動設置各個欄位
//////	        caregiver.setCaregiverName(formData.get("caregiverName"));
//////	        caregiver.setCaregiverPassword(formData.get("caregiverPassword"));
//////	        caregiver.setCaregiverGender(formData.get("caregiverGender"));
//////	        caregiver.setCaregiverAge(Integer.parseInt(formData.get("caregiverAge")));
//////	        caregiver.setExpYears(Integer.parseInt(formData.get("expYears")));
//////	        caregiver.setEduExperience(formData.get("eduExperience"));
//////	        caregiver.setCaregiverPhone(formData.get("caregiverPhone"));
//////	        caregiver.setCaregiverEmail(formData.get("caregiverEmail"));
//////	        caregiver.setCaregiverTWID(formData.get("caregiverTWID"));
//////	        caregiver.setCaregiverAddress(formData.get("caregiverAddress"));
//////	        caregiver.setHourlyRate(Double.parseDouble(formData.get("hourlyRate")));
////	        
////	        // 確保新增時不帶入 ID
////	        caregiver.setCaregiverNO(0);
////
////	        CaregiverBean savedCaregiver = caregiverService.insertCaregiver(caregiver, caregiverPicture);
////	        return ResponseEntity.ok()
////	            .body(Map.of("message", "新增成功", "caregiver", savedCaregiver));
////	    } catch (Exception e) {
////	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////	            .body(Map.of("error", "新增失敗：" + e.getMessage()));
////	    }
////	}
////	
//	@PutMapping("/UpdateCaregiver")
//	public ResponseEntity<?> updateCaregiver(@RequestBody CaregiverBean caregiver) {
//	    try {
//	        // 先查找現有的護工資料
//	        CaregiverBean existingCaregiver = caregiverService.findCaregiver(caregiver.getCaregiverNO());
//	        if (existingCaregiver == null) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body(Map.of("error", "更新失敗，未找到護理人員"));
//	        }
//
//	        // 只更新 CAREGIVER 表的欄位
//	        existingCaregiver.setCaregiverGender(caregiver.getCaregiverGender());
//	        existingCaregiver.setCaregiverAge(caregiver.getCaregiverAge());
//	        existingCaregiver.setExpYears(caregiver.getExpYears());
////	        existingCaregiver.setEduExperience(caregiver.getEduExperience());
////	        existingCaregiver.setHourlyRate(caregiver.getHourlyRate());
//
//	        // 保存更新
//	        CaregiverBean updatedCaregiver = caregiverService.updateCaregiver(existingCaregiver);
//	        return ResponseEntity.ok()
//	            .body(Map.of("message", "更新成功", "caregiver", updatedCaregiver));
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body(Map.of("error", "更新失敗：" + e.getMessage()));
//	    }
//	}
//	
//	
//	
//	
////	@PutMapping("/UpdateCaregiver")
////	public ResponseEntity<?> updateCaregiver(@RequestBody CaregiverBean caregiver) {
////	    try {
////	        // 使用服務層查詢目標對象
////	        CaregiverBean existingCaregiver = caregiverService.findCaregiver(caregiver.getCaregiverNO());
////	        if (existingCaregiver == null) {
////	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
////	                    .body(Map.of("error", "更新失敗，未找到護理人員"));
////	        }
////
////	        // 保留原有ID
////	        int originalId = existingCaregiver.getCaregiverNO();
////	        
////	        // 複製所有新的屬性值到現有對象
////	        BeanUtils.copyProperties(caregiver, existingCaregiver);
////	        
////	        // 確保ID不被更改
////	        existingCaregiver.setCaregiverNO(originalId);
////
////	        // 保存更新後的數據
////	        CaregiverBean updatedCaregiver = caregiverService.updateCaregiver(existingCaregiver, null);
////	        return ResponseEntity.ok(Map.of("message", "更新成功", "caregiver", updatedCaregiver));
////	    } catch (Exception e) {
////	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////	                .body(Map.of("error", "更新失敗：" + e.getMessage()));
////	    }
////	}
//	
//	
//
////    // 更新護理人員（通過 JSON 內的 caregiverNO 確認更新對象）
////    @PutMapping("/UpdateCaregiver")
////    public ResponseEntity<?> updateCaregiver(@RequestBody CaregiverBean caregiver) {
////        try {
////            // 使用服務層查詢目標對象，並更新其他屬性
////            CaregiverBean existingCaregiver = caregiverService.findCaregiver(caregiver.getCaregiverNO());
////            if (existingCaregiver == null) {
////                return ResponseEntity.status(HttpStatus.NOT_FOUND)
////                        .body(Map.of("error", "更新失敗，未找到護理人員"));
////            }
////
////            // 更新現有對象的屬性
////            existingCaregiver.setCaregiverName(caregiver.getCaregiverName());
////            existingCaregiver.setCaregiverPassword(caregiver.getCaregiverPassword());
////            existingCaregiver.setCaregiverGender(caregiver.getCaregiverGender());
////            existingCaregiver.setCaregiverAge(caregiver.getCaregiverAge());
////            existingCaregiver.setExpYears(caregiver.getExpYears());
////            existingCaregiver.setEduExperience(caregiver.getEduExperience());
////            existingCaregiver.setCaregiverPhone(caregiver.getCaregiverPhone());
////            existingCaregiver.setCaregiverEmail(caregiver.getCaregiverEmail());
////            existingCaregiver.setCaregiverTWID(caregiver.getCaregiverTWID());
////            existingCaregiver.setCaregiverAddress(caregiver.getCaregiverAddress());
////            existingCaregiver.setHourlyRate(caregiver.getHourlyRate());
////
////            // 保存更新後的數據
////            CaregiverBean updatedCaregiver = caregiverService.updateCaregiver(existingCaregiver, null);
////            return ResponseEntity.ok(Map.of("message", "更新成功", "caregiver", updatedCaregiver));
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body(Map.of("error", "更新失敗：" + e.getMessage()));
////        }
////    }
//
//    // 刪除護理人員
//    @DeleteMapping("/DeleteCaregiver")
//    public ResponseEntity<?> deleteCaregiver(@RequestParam("caregiverNO") Integer caregiverNO) {
//        try {
//            caregiverService.deleteCaregiver(caregiverNO);
//            return ResponseEntity.ok(Map.of("message", "刪除成功"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "刪除失敗：" + e.getMessage()));
//        }
//    }
//}