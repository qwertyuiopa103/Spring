package ispan.caregiver.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ispan.user.model.UserBean;

import java.util.List;
import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<CaregiverBean, Integer> {
    List<CaregiverBean> findByCGstatus(String status);
    long countByCGstatus(String status);
    
    @Query("SELECT c FROM CaregiverBean c JOIN c.user u WHERE c.CGstatus = :status")
    List<CaregiverBean> findAllWithUserByStatus(@Param("CGstatus") String CGstatus);
    
    @Query("SELECT COUNT(c) FROM CaregiverBean c WHERE c.CGstatus = 'PENDING'")
    long countPendingApplications();
    
    CaregiverBean findByUserUserID(String userID);
    
    
}

