package ispan.caregiver.model;

import ispan.caregiver.model.ServiceArea;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Integer> {
	 List<ServiceArea> findByTaipeiCityTrue();
	    List<ServiceArea> findByNewTaipeiCityTrue();
	    List<ServiceArea> findByTaoyuanCityTrue();
	    List<ServiceArea> findByTaichungCityTrue();
	    List<ServiceArea> findByTainanCityTrue();
	    List<ServiceArea> findByKaohsiungCityTrue();
	    List<ServiceArea> findByHsinchuCityTrue();
	    List<ServiceArea> findByHsinchuCountyTrue();
	    List<ServiceArea> findByKeelungCityTrue();
	    List<ServiceArea> findByYilanCountyTrue();
	    List<ServiceArea> findByMiaoliCountyTrue();
	    List<ServiceArea> findByChanghuaCountyTrue();
	    List<ServiceArea> findByNantouCountyTrue();
	    List<ServiceArea> findByYunlinCountyTrue();
	    List<ServiceArea> findByChiayiCityTrue();
	    List<ServiceArea> findByChiayiCountyTrue();
	    List<ServiceArea> findByPingtungCountyTrue();
	    List<ServiceArea> findByTaitungCountyTrue();
	    List<ServiceArea> findByHualienCountyTrue();
	    List<ServiceArea> findByPenghuCountyTrue();
	    List<ServiceArea> findByKinmenCountyTrue();
	    List<ServiceArea> findByLienchiangCountyTrue();
}
