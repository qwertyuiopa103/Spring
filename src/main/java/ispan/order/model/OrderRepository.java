package ispan.order.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ispan.caregiver.model.CaregiverBean;
import ispan.user.model.UserBean;


public interface OrderRepository extends JpaRepository<OrderBean, Integer> {
	// 查詢是否存在指定的 User
    @Query("SELECT COUNT(u) > 0 FROM UserBean u WHERE u.userID = :userID")
    boolean existsUserById(@Param("userID") String userID);

   // 查詢是否存在指定的 Caregiver
   @Query("SELECT COUNT(c) > 0 FROM CaregiverBean c WHERE c.caregiverNO = :caregiverNO")
    boolean existsCaregiverById(@Param("caregiverNO") Integer caregiverNO);

	List<OrderBean> findByCaregiver(CaregiverBean caregiver);
	List<OrderBean> findByUser(UserBean user);
	// 根據訂單 ID 查詢
    OrderBean findByOrderId(int orderId);
    
    // 檢查看護有無重疊時間的訂單
    @Query("SELECT COUNT(o) > 0 "
            + "FROM OrderBean o "
            + "WHERE o.caregiver.caregiverNO = :caregiverNO "
            + "AND o.orderId != :orderId " // 排除當前正在更新的預約
            + "AND ( "
            + "    (o.startDate BETWEEN :startDate AND :endDate) "  // 新的開始時間在現有的預約時間範圍內
            + "    OR "
            + "    (o.endDate BETWEEN :startDate AND :endDate) "  // 新的結束時間在現有的預約時間範圍內
            + "    OR "
            + "    (:startDate BETWEEN o.startDate AND o.endDate) "  // 現有的預約開始時間在新的預約時間範圍內
            + "    OR "
            + "    (:endDate BETWEEN o.startDate AND o.endDate) "  // 現有的預約結束時間在新的預約時間範圍內
            + ")")
    boolean isOverlappingWithExistingOrder(
            @Param("caregiverNO") int caregiverNO,
            @Param("orderId") int orderId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    
    @Query("SELECT COUNT(o) FROM OrderBean o ")
    long countOrderBean();


}
