// ServiceArea.java
package ispan.caregiver.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Entity
@Table(name = "ServiceArea")
@Getter 
@Setter 
@NoArgsConstructor
public class ServiceAreaBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer areaID;
    
    private boolean taipei_city;
    private boolean new_taipei_city;
    private boolean taoyuan_city;
    private boolean taichung_city;
    private boolean tainan_city;
    private boolean kaohsiung_city;
    private boolean hsinchu_city;
    private boolean hsinchu_county;
    private boolean keelung_city;
    private boolean yilan_county;
    private boolean miaoli_county;
    private boolean changhua_county;
    private boolean nantou_county;
    private boolean yunlin_county;
    private boolean chiayi_city;
    private boolean chiayi_county;
    private boolean pingtung_county;
    private boolean taitung_county;
    private boolean hualien_county;
    private boolean penghu_county;
    private boolean kinmen_county;
    private boolean lienchiang_county;
}
