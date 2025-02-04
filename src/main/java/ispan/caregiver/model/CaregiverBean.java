package ispan.caregiver.model;

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
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AreaID")
    private ServiceAreaBean serviceArea;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CertifiPhotoID")
    private CertifiPhotoBean certifiPhoto;
    
    @Column(name = "CGstatus")
    private String CGstatus = "PENDING"; // PENDING, APPROVED, REJECTED
    
    // User convenience methods remain the same...
}



