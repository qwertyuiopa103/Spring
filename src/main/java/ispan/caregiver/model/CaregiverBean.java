package ispan.caregiver.model;


import java.io.IOException;
import java.math.BigDecimal;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import jakarta.persistence.*;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ispan.user.model.UserBean;
@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Caregiver")
public class CaregiverBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiverNO")
    private int caregiverNO;
    
    @Column(name = "caregiverGender")
    private String caregiverGender;
    
    @Column(name = "caregiverAge")
    private int caregiverAge;
    
    @Column(name = "expYears")
    private int expYears;
    
    @Column(name = "Services")
    private String services; 
    
    @Column(name = "Education")
    private String education;
    
    @Column(name = "DaylyRate")
    private Double daylyRate;
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private UserBean user;
    
    @ManyToOne
    @JoinColumn(name = "AreaID")
    private ServiceAreaBean serviceArea;

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

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "AreaID")
//    private ServiceAreaBean serviceArea;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CertifiPhotoID")
    private CertifiPhotoBean certifiPhoto;
    
    @Column(name = "CGstatus")
    private String CGstatus = "PENDING"; // PENDING, APPROVED, REJECTED

}



