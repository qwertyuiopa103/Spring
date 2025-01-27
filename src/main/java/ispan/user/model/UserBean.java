package ispan.user.model;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import ispan.user.tools.CommonTool;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.OneToOne;

@Component
@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class UserBean {
	@Id
	@Column(name = "userID")
	private String userID;
	
	@OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JsonManagedReference 
	private UserSecurityBean userSecurity= new UserSecurityBean();
	
	@Column(name = "userName")
	private String userName;

	@Column(name = "userPassword")
	private String userPassword;

	@Column(name = "userPhone")
	private String userPhone;

	@Column(name = "userEmail")
	private String userEmail;

	@Column(name = "userCity")
	private String userCity;

	@Column(name = "userDistrict")
	private String userDistrict;

	@Column(name = "userAddress")
	private String userAddress;

	@Column(name = "userPhoto")
	@Lob
	@JsonIgnore // 忽略序列化原始的 byte[] 字段
	private byte[] userPhoto;
	//private String userPhoto;


//	@Column(name = "userCreat",updatable = false, insertable = false)
//	private Timestamp userCreat;
//
//	@Column(name = "userLogin")
//	private Timestamp userLogin;
//
//	@Column(name = "userUpdated")
//	private Timestamp userUpdated;
//
//	@Column(name = "userLockoutEnd")
//	private Timestamp userLockoutEnd;
//
//	@Column(name = "userFailedLoginAttempts", insertable = false)
//	private Integer userFailedLoginAttempts;
//
//	@Column(name = "userVerified")
//	private boolean userVerified;
//	
//	@Column(name = "userVerificationToken")
//	private String userVerificationToken;
	
	@Column(name = "userRole",insertable = false)
	private String userRole;
	
//	@Column(name = "userEnabled",insertable = false)
//	private boolean userEnabled;
//	
//	@Column(name = "userActive",insertable = false)
//	private boolean userActive;
//	
//	@Column(name = "userDeleted",insertable = false)
//	private boolean userDeleted;
//	
//	@Column(name = "userResetPasswordToken ")
//	private String userResetPasswordToken ;
//	
//	@Column(name = "userPasswordChanged")
//	private Timestamp userPasswordChanged;
//	
//	@Column(name = " userResetPasswordExpires")
//	private Timestamp userResetPasswordExpires;
//	
	
//	@PreUpdate
//    public void preUpdate() {
//        // 如果 userVerified 已經被設置為 true，就不再修改它
//        if (this.userVerified) {
//            // 保持 userVerified 為 true，不允許更新
//            return;
//        }
//    }
	
	 @JsonProperty("userPhoto") // 這樣序列化時會使用這個方法的返回值作為 userPhoto
	    public String getUserPhotoBase64() {
	        if (this.userPhoto != null && this.userPhoto.length > 0) {
	            return CommonTool.convertByteArrayToBase64String(this.userPhoto);
	        }
	        return null; // 或者返回預設圖片的 Base64 字串
	    }
}
