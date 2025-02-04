package ispan.caregiver.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ispan.user.tools.CommonTool;

@Entity
@Table(name = "CertifiPhoto")
@Data @Getter @Setter @NoArgsConstructor
public class CertifiPhotoBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certifiPhotoID")
    private Integer certifiPhotoID;
    
    @Lob
    @Column(name = "photo1")
    private byte[] photo1;
    
    @Lob
    @Column(name = "photo2")
    private byte[] photo2;
    
    @Lob
    @Column(name = "photo3")
    private byte[] photo3;
    
    @Lob
    @Column(name = "photo4")
    private byte[] photo4;
    
    @Lob
    @Column(name = "photo5")
    private byte[] photo5;
    
    // 修改為使用 CommonTool 轉換
    @JsonProperty("photo1")
    public String getPhoto1Base64() {
        return photo1 != null ? CommonTool.convertByteArrayToBase64String(photo1) : null;
    }
    
    @JsonProperty("photo2")
    public String getPhoto2Base64() {
        return photo2 != null ? CommonTool.convertByteArrayToBase64String(photo2) : null;
    }
    
    @JsonProperty("photo3")
    public String getPhoto3Base64() {
        return photo3 != null ? CommonTool.convertByteArrayToBase64String(photo3) : null;
    }
    
    @JsonProperty("photo4")
    public String getPhoto4Base64() {
        return photo4 != null ? CommonTool.convertByteArrayToBase64String(photo4) : null;
    }
    
    @JsonProperty("photo5")
    public String getPhoto5Base64() {
        return photo5 != null ? CommonTool.convertByteArrayToBase64String(photo5) : null;
    }

    
}
