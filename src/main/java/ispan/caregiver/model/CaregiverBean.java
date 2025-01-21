package ispan.caregiver.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import ispan.user.model.UserBean;
import jakarta.persistence.CascadeType;

//import java.io.Serializable;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity // 指定這是一個 Hibernate 的實體類
@Table(name = "Caregiver") // 對應資料庫中的表名
public class CaregiverBean {	
    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiverNO")
    private int caregiverNO;
    
    @Column(name = "caregiverGender")
    private String caregiverGender;
    
    @Column(name = "caregiverAge")
    private int caregiverAge;
    
    @Column(name = "expYears", length = 3)
    private int expYears;

    @Column(name = "eduExperience", length = 255)
    private String eduExperience;

    // 改為以「時薪」儲存，使用 Double 型別
    @Column(name = "hourlyRate")
    private Double hourlyRate;
    
    // 與 User 表的關聯
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private UserBean user;

    // 便利方法，用於檢查是否有關聯的使用者
    public boolean hasUser() {
        return user != null;
    }

    // 取得關聯使用者的ID
    public String getUserID() {
        return hasUser() ? user.getUserID() : null;
    }

    // 取得關聯使用者的姓名
    public String getUserName() {
        return hasUser() ? user.getUserName() : null;
    }

    // 取得關聯使用者的Email
    public String getUserEmail() {
        return hasUser() ? user.getUserEmail() : null;
    }

    // 取得關聯使用者的電話
    public String getUserPhone() {
        return hasUser() ? user.getUserPhone() : null;
    }

    // 取得關聯使用者的照片Base64
    public String getUserPhotoBase64() {
        return hasUser() ? user.getUserPhotoBase64() : null;
    }


	
	
	

}
