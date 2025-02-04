package ispan.caregiver.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ispan.caregiver.model.*;
import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.service.UserEmailService;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CaregiverServiceImpl implements CaregiverService {
    @Autowired
    private CaregiverRepository caregiverRepository;
    
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired
    private UserEmailService emailService;
    @Autowired
    private ServiceAreaService serviceAreaService;
    
    @Autowired
    private CertifiPhotoService certifiPhotoService;
    
    @Override
    public List<CaregiverBean> findAllCaregivers() {
        return caregiverRepository.findAll();
    }
    
    @Override
    public List<CaregiverBean> findByCGStatus(String CGstatus) {
        return caregiverRepository.findByCGstatus(CGstatus);
    }
    
    @Override
    public long countPendingApplications() {
        return caregiverRepository.countPendingApplications();
    }
    
    @Override
    @Transactional
    public void approveCaregiver(Integer caregiverNO) {
        try {
            CaregiverBean caregiver = caregiverRepository.findById(caregiverNO)
                .orElseThrow(() -> new RuntimeException("找不到該護工資料"));
                
            // 更新護工狀態
            caregiver.setCGstatus("APPROVED");
            caregiverRepository.save(caregiver);
            
            // 更新用戶角色
            UserBean user = caregiver.getUser();
            user.setUserRole("ROLE_CAREGIVER");
            userRepository.save(user);
            
            try {
                // 嘗試發送郵件，但即使失敗也不影響狀態更新
            	
                emailService.sendApprovalEmail(user.getUserEmail());
            } catch (Exception e) {
                // 記錄郵件發送錯誤但不中斷交易
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException("審核過程發生錯誤: " + e.getMessage());
        }
    }
    
    
    
    
    
    @Override
    @Transactional
    public void rejectCaregiver(Integer caregiverNO, String reason) {
        CaregiverBean caregiver = caregiverRepository.findById(caregiverNO)
            .orElseThrow(() -> new RuntimeException("找不到該護工資料"));
            
        caregiver.setCGstatus("REJECTED");
        caregiverRepository.save(caregiver);
        
        // 發送拒絕通知郵件
        emailService.sendRejectionEmail(caregiver.getUser().getUserEmail(), reason);
    }
    
    @Override
    public CaregiverBean findById(Integer caregiverNO) {
        return caregiverRepository.findById(caregiverNO)
            .orElseThrow(() -> new RuntimeException("找不到該護工資料"));
    }
    
    
    @Override
    public CaregiverBean insertCaregiver(CaregiverBean caregiver) {
        try {
            if (caregiver.getUser() == null || caregiver.getUser().getUserID() == null) {
                throw new RuntimeException("必須提供使用者 ID");
            }

            Optional<UserBean> userOpt = userRepository.findById(caregiver.getUser().getUserID());
            if (userOpt.isEmpty()) {
                throw new RuntimeException("找不到對應的使用者");
            }

            UserBean user = userOpt.get();
            caregiver.setUser(user);
            caregiver.setCGstatus("PENDING");

            // 處理服務區域
            if (caregiver.getServiceArea() != null) {
                ServiceAreaBean newArea = new ServiceAreaBean();
                BeanUtils.copyProperties(caregiver.getServiceArea(), newArea);
                ServiceAreaBean savedArea = serviceAreaService.save(newArea);
                caregiver.setServiceArea(savedArea);
            }

            // 處理證照照片
            if (caregiver.getCertifiPhoto() != null) {
                // 從數據庫中獲取已保存的 CertifiPhotoBean
                CertifiPhotoBean savedPhoto = certifiPhotoService.findById(caregiver.getCertifiPhoto().getCertifiPhotoID());
//                    .orElseThrow(() -> new RuntimeException("找不到對應的證照照片"));
                caregiver.setCertifiPhoto(savedPhoto);
            }

            return caregiverRepository.save(caregiver);
            
        } catch (Exception e) {
            throw new RuntimeException("建立看護資料失敗: " + e.getMessage());
        }
    }

   
    
    @Override
    @Transactional
    public CaregiverBean updateCaregiver(CaregiverBean caregiver) {
        CaregiverBean existing = findById(caregiver.getCaregiverNO());
        
        // 更新基本資訊
        existing.setCaregiverGender(caregiver.getCaregiverGender());
        existing.setCaregiverAge(caregiver.getCaregiverAge());
        existing.setExpYears(caregiver.getExpYears());
        existing.setEducation(caregiver.getEducation());
        existing.setDaylyRate(caregiver.getDaylyRate());
        existing.setServices(caregiver.getServices());
        if (caregiver.getServiceArea() != null) {
            ServiceAreaBean updatedArea = serviceAreaService.save(caregiver.getServiceArea());
            existing.setServiceArea(updatedArea);
        }

        // 更新證照照片
        if (caregiver.getCertifiPhoto() != null) {
            CertifiPhotoBean existingPhoto = existing.getCertifiPhoto();
            if (existingPhoto == null) {
                existingPhoto = new CertifiPhotoBean();
            }
            
            CertifiPhotoBean newPhoto = caregiver.getCertifiPhoto();
            if (newPhoto.getPhoto1() != null) existingPhoto.setPhoto1(newPhoto.getPhoto1());
            if (newPhoto.getPhoto2() != null) existingPhoto.setPhoto2(newPhoto.getPhoto2());
            if (newPhoto.getPhoto3() != null) existingPhoto.setPhoto3(newPhoto.getPhoto3());
            if (newPhoto.getPhoto4() != null) existingPhoto.setPhoto4(newPhoto.getPhoto4());
            if (newPhoto.getPhoto5() != null) existingPhoto.setPhoto5(newPhoto.getPhoto5());
            
            existing.setCertifiPhoto(existingPhoto);
        }

        return caregiverRepository.save(existing);
    }

   @Override
   @Transactional
   public void deleteCaregiver(Integer caregiverNO) {
       caregiverRepository.deleteById(caregiverNO);
   }
   @Override
   public CaregiverBean findCaregiverByUserId(String userId) {
	   return caregiverRepository.findByUserUserID(userId);
   }
   
}