package ispan.caregiver.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ispan.user.model.UserBean;

public interface CaregiverRepository extends JpaRepository<CaregiverBean, Integer> {

	@Transactional
	@Modifying
	@Query("DELETE FROM CaregiverBean c WHERE c.user.userID = :userID")
	void deleteByUserID(String userID);
	
	@Query("SELECT u.caregiverNO FROM CaregiverBean u WHERE u.user.userID = :userID")
	Optional<Integer> findCaregiverNOByUserID(String userID);
	
	List<CaregiverBean> findTop3ByServicesAndServiceAreaOrderByCaregiverNOAsc(String services, ServiceArea serviceArea);
	List<CaregiverBean> findTop3ByServicesOrderByCaregiverNOAsc(String services);
	
}
