package ispan.caregiver.controller;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispan.caregiver.model.CaregiverBean;
import ispan.caregiver.model.CertifiPhotoBean;
import ispan.caregiver.model.ServiceAreaBean;
import ispan.caregiver.service.CaregiverService;
import ispan.caregiver.service.CertifiPhotoService;
import ispan.user.model.UserBean;
import ispan.user.tools.CommonTool;

@RestController
@RequestMapping("/api/caregiver")
public class CaregiverController {
	
	// 改為
	@Autowired
	private CaregiverService caregiverService;
	   @Autowired
	   private CertifiPhotoService certifiPhotoService;  // 先加入注入

	@GetMapping("/CGstatus/{CGstatus}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public ResponseEntity<?> getCaregiversByCGStatus(@PathVariable String CGstatus) {
		try {
			List<CaregiverBean> caregivers = caregiverService.findByCGStatus(CGstatus);
			return ResponseEntity.ok(caregivers);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("篩選資料失敗: " + e.getMessage());
		}
	}
	

	@GetMapping("/pending-count")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Map<String, Object>> getPendingCount() {
		Map<String, Object> response = new HashMap<>();
		response.put("count", caregiverService.countPendingApplications());
		response.put("applications", caregiverService.findByCGStatus("PENDING"));
		return ResponseEntity.ok(response);
	}
	
	

   @GetMapping("/findAllCaregiver")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
   public ResponseEntity<?> findAllCaregiver() {
       return ResponseEntity.ok(caregiverService.findAllCaregivers());
   }
   
 
   
   @PostMapping("/approve/{caregiverNO}")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
   public ResponseEntity<?> approveCaregiver(@PathVariable Integer caregiverNO) {
       try {
           caregiverService.approveCaregiver(caregiverNO);
           return ResponseEntity.ok("審核通過");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }

   @PostMapping("/reject/{caregiverNO}")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
   public ResponseEntity<?> rejectCaregiver(
           @PathVariable Integer caregiverNO,
           @RequestBody Map<String, String> request) {
       try {
           String reason = request.get("reason");
           if (reason == null || reason.trim().isEmpty()) {
               return ResponseEntity.badRequest().body("必須提供退回原因");
           }
           caregiverService.rejectCaregiver(caregiverNO, reason);
           return ResponseEntity.ok("已退回申請");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }
//////////////////////////////////////////////////////////////////////////////////////////////////////////


   //INSERT
   @PostMapping("/insert")
   @PreAuthorize("hasAuthority('ROLE_USER')")
   public ResponseEntity<?> insertCaregiver(
       @RequestBody CaregiverBean caregiver) {
       System.out.println(caregiver);
       CaregiverBean savedCaregiver = caregiverService.insertCaregiver(caregiver);
       return ResponseEntity.ok(savedCaregiver);
   }
   
//   //UPDATE
//   @PutMapping("/update")
//   @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CAREGIVER')")
//   public ResponseEntity<?> updateCaregiver(
//		    @RequestParam("caregiverData") String caregiverDataStr,
//		    @RequestParam(value = "photos", required = false) MultipartFile[] photos
//		) {
//		    try {
//		        // 將 JSON 字串轉換為 CaregiverBean 對象
//		        ObjectMapper mapper = new ObjectMapper();
//		        CaregiverBean caregiver = mapper.readValue(caregiverDataStr, CaregiverBean.class);
//		        
//		        // 處理新上傳的照片
//		        if (photos != null && photos.length > 0) {
//		            CertifiPhotoBean certifiPhoto = caregiver.getCertifiPhoto();
//		            if (certifiPhoto == null) {
//		                certifiPhoto = new CertifiPhotoBean();
//		            }
//		            
//		            // 獲取當前照片數量
//		            int currentPhotoCount = 0;
//		            if (certifiPhoto.getPhoto1() != null) currentPhotoCount++;
//		            if (certifiPhoto.getPhoto2() != null) currentPhotoCount++;
//		            if (certifiPhoto.getPhoto3() != null) currentPhotoCount++;
//		            if (certifiPhoto.getPhoto4() != null) currentPhotoCount++;
//		            if (certifiPhoto.getPhoto5() != null) currentPhotoCount++;
//		            
//		            // 根據可用空間添加新照片
//		            for (int i = 0; i < photos.length && currentPhotoCount < 5; i++) {
//		                byte[] photoBytes = photos[i].getBytes();
//		                switch (currentPhotoCount) {
//		                    case 0: certifiPhoto.setPhoto1(photoBytes); break;
//		                    case 1: certifiPhoto.setPhoto2(photoBytes); break;
//		                    case 2: certifiPhoto.setPhoto3(photoBytes); break;
//		                    case 3: certifiPhoto.setPhoto4(photoBytes); break;
//		                    case 4: certifiPhoto.setPhoto5(photoBytes); break;
//		                }
//		                currentPhotoCount++;
//		            }
//		            
//		            caregiver.setCertifiPhoto(certifiPhoto);
//		        }
//		        
//		        CaregiverBean updatedCaregiver = caregiverService.updateCaregiver(caregiver);
//		        return ResponseEntity.ok()
//		                .body(Map.of("message", "更新成功", 
//		                            "caregiver", updatedCaregiver));
//		    } catch (Exception e) {
//		        return ResponseEntity.badRequest().body("更新失敗: " + e.getMessage());
//		    }
//		}
   @PutMapping("/update")
   @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CAREGIVER')")
   public ResponseEntity<?> updateCaregiver(@RequestBody CaregiverBean caregiver) {
       try {
           CaregiverBean updatedCaregiver = caregiverService.updateCaregiver(caregiver);
           return ResponseEntity.ok()
                   .body(Map.of("message", "更新成功", 
                               "caregiver", updatedCaregiver));
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("更新失敗: " + e.getMessage());
       }
   }
   
   //DELETE
   @DeleteMapping("/{caregiverNO}")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
   public ResponseEntity<?> deleteCaregiver(@PathVariable Integer caregiverNO) {
       try {
    	   CaregiverBean byId = caregiverService.findById(caregiverNO);
    	   byId.getUser().setUserRole("ROLE_USER");
           caregiverService.deleteCaregiver(caregiverNO);
           
           return ResponseEntity.ok("刪除成功");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("刪除失敗: " + e.getMessage());
       }
   }
   
   //FIND A CAREGIVER 
   @GetMapping("/findCaregiver/{caregiverNO}")
   @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CAREGIVER', 'ROLE_USER')")
   public ResponseEntity<?> findCaregiver(@PathVariable Integer caregiverNO) {
       try {
           CaregiverBean caregiver = caregiverService.findById(caregiverNO);
           return ResponseEntity.ok(caregiver);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(Map.of("error", "未找到護工資料"));
       }
   }
   
   @GetMapping("/findByUserId/{userId}")
   public CaregiverBean findCaregiverByUserId(@PathVariable String userId) {
	   System.out.println("123!!!!!!!!!");
	   CaregiverBean caregiver = caregiverService.findCaregiverByUserId(userId);
	   return caregiver;
   }
   


  


}