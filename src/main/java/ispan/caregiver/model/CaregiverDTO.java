package ispan.caregiver.model;


import org.springframework.web.multipart.MultipartFile;

import ispan.user.model.UserBean;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverDTO {
    // Caregiver 相關欄位
    private Integer caregiverNO;
    private String caregiverGender;
    private Integer caregiverAge;
    private Integer expYears;
    private String eduExperience;
    private Double hourlyRate;
    
    // User 相關欄位
    private String userID;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userCity;
    private String userDistrict;
    private String userAddress;
    private String userPhoto;  // Base64 字串格式
    
    // 用於檔案上傳，不會被序列化
    private transient MultipartFile photoFile;
    
    // 建構子
    public CaregiverDTO(CaregiverBean caregiver) {
        if (caregiver != null) {
            this.caregiverNO = caregiver.getCaregiverNO();
            this.caregiverGender = caregiver.getCaregiverGender();
            this.caregiverAge = caregiver.getCaregiverAge();
            this.expYears = caregiver.getExpYears();
//            this.eduExperience = caregiver.getEduExperience();
//            this.hourlyRate = caregiver.getHourlyRate();
            
            UserBean user = caregiver.getUser();
            if (user != null) {
                this.userID = user.getUserID();
                this.userName = user.getUserName();
                this.userEmail = user.getUserEmail();
                this.userPhone = user.getUserPhone();
                this.userCity = user.getUserCity();
                this.userDistrict = user.getUserDistrict();
                this.userAddress = user.getUserAddress();
                // 如果需要轉換照片為 Base64
                if (user.getUserPhoto() != null) {
                    this.userPhoto = user.getUserPhotoBase64();
                }
            }
        }
    }
    
    // 轉換為 CaregiverBean 的方法
    public CaregiverBean toCaregiver() {
        CaregiverBean caregiver = new CaregiverBean();
        caregiver.setCaregiverNO(this.caregiverNO);
        caregiver.setCaregiverGender(this.caregiverGender);
        caregiver.setCaregiverAge(this.caregiverAge);
        caregiver.setExpYears(this.expYears);
//        caregiver.setEduExperience(this.eduExperience);
//        caregiver.setHourlyRate(this.hourlyRate);
        
        // 設置 User
        UserBean user = new UserBean();
        user.setUserID(this.userID);
        user.setUserName(this.userName);
        user.setUserEmail(this.userEmail);
        user.setUserPhone(this.userPhone);
        user.setUserCity(this.userCity);
        user.setUserDistrict(this.userDistrict);
        user.setUserAddress(this.userAddress);
        
        caregiver.setUser(user);
        
        return caregiver;
    }
}
