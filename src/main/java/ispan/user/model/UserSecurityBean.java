package ispan.user.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Table(name = "UserSecurity")
@Getter
@Setter
@NoArgsConstructor
public class UserSecurityBean {
	@Id
	@Column(name = "userID")
	private String userID;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "userID")
	@JsonBackReference // 被管理方
    private UserBean users;
	
	@Column(name = "userCreat",updatable = false, insertable = false)
	private Timestamp userCreat;

	@Column(name = "userLogin")
	private Timestamp userLogin;

	@Column(name = "userUpdated")
	private Timestamp userUpdated;

	@Column(name = "userLockoutEnd")
	private Timestamp userLockoutEnd;

	@Column(name = "userFailedLoginAttempts", insertable = false)
	private Integer userFailedLoginAttempts;

	@Column(name = "userVerified")
	private boolean userVerified;
	
	@Column(name = "userVerificationToken")
	private String userVerificationToken;
	
	@Column(name = "userActive",insertable = false)
	private boolean userActive;
	
	@Column(name = "userDeleted",insertable = false)
	private boolean userDeleted;
	
	@Column(name = " userDeletedTime")
	private Timestamp userDeletedTime;
	
	@Column(name = "userResetPasswordToken ")
	private String userResetPasswordToken ;
	
	@Column(name = "userPasswordChanged")
	private Timestamp userPasswordChanged;
	
	@Column(name = " userResetPasswordExpires")
	private Timestamp userResetPasswordExpires;
	
	
	
}
