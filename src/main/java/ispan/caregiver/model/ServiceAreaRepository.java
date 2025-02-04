package ispan.caregiver.model;

import ispan.caregiver.model.ServiceAreaBean;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceAreaBean, Integer> {
	 @Query("SELECT s FROM ServiceAreaBean s WHERE s.taipei_city = true")
	    List<ServiceAreaBean> findByTaipeiCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.new_taipei_city = true")
	    List<ServiceAreaBean> findByNewTaipeiCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.taoyuan_city = true")
	    List<ServiceAreaBean> findByTaoyuanCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.taichung_city = true")
	    List<ServiceAreaBean> findByTaichungCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.tainan_city = true")
	    List<ServiceAreaBean> findByTainanCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.kaohsiung_city = true")
	    List<ServiceAreaBean> findByKaohsiungCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.hsinchu_city = true")
	    List<ServiceAreaBean> findByHsinchuCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.hsinchu_county = true")
	    List<ServiceAreaBean> findByHsinchuCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.keelung_city = true")
	    List<ServiceAreaBean> findByKeelungCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.yilan_county = true")
	    List<ServiceAreaBean> findByYilanCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.miaoli_county = true")
	    List<ServiceAreaBean> findByMiaoliCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.changhua_county = true")
	    List<ServiceAreaBean> findByChanghuaCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.nantou_county = true")
	    List<ServiceAreaBean> findByNantouCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.yunlin_county = true")
	    List<ServiceAreaBean> findByYunlinCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.chiayi_city = true")
	    List<ServiceAreaBean> findByChiayiCityTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.chiayi_county = true")
	    List<ServiceAreaBean> findByChiayiCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.pingtung_county = true")
	    List<ServiceAreaBean> findByPingtungCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.taitung_county = true")
	    List<ServiceAreaBean> findByTaitungCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.hualien_county = true")
	    List<ServiceAreaBean> findByHualienCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.penghu_county = true")
	    List<ServiceAreaBean> findByPenghuCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.kinmen_county = true")
	    List<ServiceAreaBean> findByKinmenCountyTrue();

	    @Query("SELECT s FROM ServiceAreaBean s WHERE s.lienchiang_county = true")
	    List<ServiceAreaBean> findByLienchiangCountyTrue();

}
