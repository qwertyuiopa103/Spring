package ispan.caregiver.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ServiceArea") // 與資料表同名
public class ServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AreaID")
    private Integer areaId; // 主鍵

    // 各地區 bit 欄位
    @Column(name = "taipei_city")
    private boolean taipeiCity;

    @Column(name = "new_taipei_city")
    private boolean newTaipeiCity;

    @Column(name = "taoyuan_city")
    private boolean taoyuanCity;

    @Column(name = "taichung_city")
    private boolean taichungCity;

    @Column(name = "tainan_city")
    private boolean tainanCity;

    @Column(name = "kaohsiung_city")
    private boolean kaohsiungCity;

    @Column(name = "hsinchu_city")
    private boolean hsinchuCity;

    @Column(name = "hsinchu_county")
    private boolean hsinchuCounty;

    @Column(name = "keelung_city")
    private boolean keelungCity;

    @Column(name = "yilan_county")
    private boolean yilanCounty;

    @Column(name = "miaoli_county")
    private boolean miaoliCounty;

    @Column(name = "changhua_county")
    private boolean changhuaCounty;

    @Column(name = "nantou_county")
    private boolean nantouCounty;

    @Column(name = "yunlin_county")
    private boolean yunlinCounty;

    @Column(name = "chiayi_city")
    private boolean chiayiCity;

    @Column(name = "chiayi_county")
    private boolean chiayiCounty;

    @Column(name = "pingtung_county")
    private boolean pingtungCounty;

    @Column(name = "taitung_county")
    private boolean taitungCounty;

    @Column(name = "hualien_county")
    private boolean hualienCounty;

    @Column(name = "penghu_county")
    private boolean penghuCounty;

    @Column(name = "kinmen_county")
    private boolean kinmenCounty;

    @Column(name = "lienchiang_county")
    private boolean lienchiangCounty;
}
