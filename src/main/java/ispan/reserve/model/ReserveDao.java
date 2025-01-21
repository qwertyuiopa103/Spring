package ispan.reserve.model;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReserveDao extends JpaRepository<Reserve, Integer> {
	// 根據 User 和 Caregiver 的 ID 查找
	List<Reserve> findByUserBeanUserIDAndCaregiverBeanCaregiverNO(String userID, int caregiverNO);

    // 根據 User 的 ID 查找
    List<Reserve> findByUserBeanUserID(String userID);

    // 根據 Caregiver 的 ID 查找
    List<Reserve> findByCaregiverBeanCaregiverNO(int caregiverNO);
    
    @Query("SELECT COUNT(r) > 0 "
            + "FROM Reserve r "
            + "WHERE r.caregiverBean.caregiverNO = :caregiverId "
            + "AND r.reserveId != :reserveId " // 排除當前正在更新的預約
            + "AND ( "
            + "    (r.startDate BETWEEN :startDate AND :endDate) "  // 新的開始時間在現有的預約時間範圍內
            + "    OR "
            + "    (r.endDate BETWEEN :startDate AND :endDate) "  // 新的結束時間在現有的預約時間範圍內
            + "    OR "
            + "    (:startDate BETWEEN r.startDate AND r.endDate) "  // 現有的預約開始時間在新的預約時間範圍內
            + "    OR "
            + "    (:endDate BETWEEN r.startDate AND r.endDate) "  // 現有的預約結束時間在新的預約時間範圍內
            + ")")
       boolean isOverlappingWithExistingReservation(
               @Param("caregiverId") int caregiverId,
               @Param("reserveId") int reserveId,
               @Param("startDate") Date startDate,
               @Param("endDate") Date endDate
       );
}
