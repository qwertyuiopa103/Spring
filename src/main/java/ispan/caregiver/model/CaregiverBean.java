package ispan.caregiver.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity // 指定這是一個 Hibernate 的實體類
@Table(name = "Caregiver") // 對應資料庫中的表名
public class CaregiverBean {	
    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiverNO")
    private int caregiverNO;

    @Column(name = "caregiverName", nullable = false, length = 100)
    private String caregiverName;
    
    @Column(name = "password", nullable = false, length = 100)
    private String caregiverPassword;

    @Column(name = "caregiverGender", nullable = false, length = 10)
    private String caregiverGender;

    @Column(name = "caregiverAge", length = 3)
    private int caregiverAge;

    @Column(name = "expYears", length = 3)
    private int expYears;

    @Column(name = "eduExperience", length = 255)
    private String eduExperience;

    @Column(name = "caregiverPhone", length = 15)
    private String caregiverPhone;

    @Column(name = "caregiverEmail", length = 100)
    private String caregiverEmail;

    @Column(name = "caregiverTWID", length = 20)
    private String caregiverTWID;

    @Column(name = "caregiverAddress", length = 255)
    private String caregiverAddress;

    // 使用 String 來儲存圖片存放路徑
    @Column(name = "caregiverPicture") 
    private String caregiverPicture;

    // 改為以「時薪」儲存，使用 Double 型別
    @Column(name = "hourlyRate")
    private Double hourlyRate;
    
   
    @Transient
    private MultipartFile caregiverPictureFile;

    public MultipartFile getCaregiverPictureFile() {
        return caregiverPictureFile;
    }

    public void setCaregiverPictureFile(MultipartFile caregiverPictureFile) {
        this.caregiverPictureFile = caregiverPictureFile;
    }




	public CaregiverBean() {
		// TODO Auto-generated constructor stub
	}
	
	


	public int getCaregiverNO() {
		return caregiverNO;
	}




	public void setCaregiverNO(int caregiverNO) {
		this.caregiverNO = caregiverNO;
	}




	public String getCaregiverName() {
		return caregiverName;
	}




	public void setCaregiverName(String caregiverName) {
		this.caregiverName = caregiverName;
	}




	public String getCaregiverPassword() {
		return caregiverPassword;
	}




	public void setCaregiverPassword(String caregiverPassword) {
		this.caregiverPassword = caregiverPassword;
	}




	public String getCaregiverGender() {
		return caregiverGender;
	}




	public void setCaregiverGender(String caregiverGender) {
		this.caregiverGender = caregiverGender;
	}




	public int getCaregiverAge() {
		return caregiverAge;
	}




	public void setCaregiverAge(int caregiverAge) {
		this.caregiverAge = caregiverAge;
	}




	public int getExpYears() {
		return expYears;
	}




	public void setExpYears(int expYears) {
		this.expYears = expYears;
	}




	public String getEduExperience() {
		return eduExperience;
	}




	public void setEduExperience(String eduExperience) {
		this.eduExperience = eduExperience;
	}




	public String getCaregiverPhone() {
		return caregiverPhone;
	}




	public void setCaregiverPhone(String caregiverPhone) {
		this.caregiverPhone = caregiverPhone;
	}




	public String getCaregiverEmail() {
		return caregiverEmail;
	}




	public void setCaregiverEmail(String caregiverEmail) {
		this.caregiverEmail = caregiverEmail;
	}




	public String getCaregiverTWID() {
		return caregiverTWID;
	}




	public void setCaregiverTWID(String caregiverTWID) {
		this.caregiverTWID = caregiverTWID;
	}




	public String getCaregiverAddress() {
		return caregiverAddress;
	}




	public void setCaregiverAddress(String caregiverAddress) {
		this.caregiverAddress = caregiverAddress;
	}




	public String getCaregiverPicture() {
		return caregiverPicture;
	}




	public void setCaregiverPicture(String caregiverPicture) {
		this.caregiverPicture = caregiverPicture;
	}




	public Double getHourlyRate() {
		return hourlyRate;
	}




	public void setHourlyRate(Double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}




	@Override
	public String toString() {
		return "caregiverBean [caregiverNO=" + caregiverNO + ", caregiverName=" + caregiverName + ", caregiverPassword="
				+ caregiverPassword + ", caregiverGender=" + caregiverGender + ", caregiverAge=" + caregiverAge
				+ ", expYears=" + expYears + ", eduExperience=" + eduExperience + ", caregiverPhone=" + caregiverPhone
				+ ", caregiverEmail=" + caregiverEmail + ", caregiverTWID=" + caregiverTWID + ", caregiverAddress="
				+ caregiverAddress + ", caregiverPicture=" + caregiverPicture + ", hourlyRate=" + hourlyRate + "]";
	}

	
	
	

}
