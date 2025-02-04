package ispan.caregiver.model;

import ispan.caregiver.model.ServiceAreaBean;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceAreaBean, Integer> {
	 List<ServiceAreaBean> findByTaipeiCityTrue();
	    List<ServiceAreaBean> findByNewTaipeiCityTrue();
	    List<ServiceAreaBean> findByTaoyuanCityTrue();
	    List<ServiceAreaBean> findByTaichungCityTrue();
	    List<ServiceAreaBean> findByTainanCityTrue();
	    List<ServiceAreaBean> findByKaohsiungCityTrue();
	    List<ServiceAreaBean> findByHsinchuCityTrue();
	    List<ServiceAreaBean> findByHsinchuCountyTrue();
	    List<ServiceAreaBean> findByKeelungCityTrue();
	    List<ServiceAreaBean> findByYilanCountyTrue();
	    List<ServiceAreaBean> findByMiaoliCountyTrue();
	    List<ServiceAreaBean> findByChanghuaCountyTrue();
	    List<ServiceAreaBean> findByNantouCountyTrue();
	    List<ServiceAreaBean> findByYunlinCountyTrue();
	    List<ServiceAreaBean> findByChiayiCityTrue();
	    List<ServiceAreaBean> findByChiayiCountyTrue();
	    List<ServiceAreaBean> findByPingtungCountyTrue();
	    List<ServiceAreaBean> findByTaitungCountyTrue();
	    List<ServiceAreaBean> findByHualienCountyTrue();
	    List<ServiceAreaBean> findByPenghuCountyTrue();
	    List<ServiceAreaBean> findByKinmenCountyTrue();
	    List<ServiceAreaBean> findByLienchiangCountyTrue();

}
