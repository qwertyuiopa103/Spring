package ispan.order.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ispan.caregiver.model.CaregiverBean;
import ispan.user.model.UserBean;
//import jakarta.transaction.Transactional;


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

    @Transactional
	@Modifying
	@Query("DELETE FROM OrderBean c WHERE c.user.userID = :userID")
	void deleteByUserID(String userID);
    
    @Transactional
   	@Modifying
   	@Query("DELETE FROM OrderBean c WHERE c.caregiver.caregiverNO = :caregiverNO")
   	void deleteBycaregiverNO(int caregiverNO);
    
    @Query("SELECT COUNT(o) FROM OrderBean o WHERE o.user.userID = :userID AND o.status NOT IN ('完成', '已取消')")
    long countIncompleteOrders(@Param("userID") String userID);
    
    @Query("SELECT COUNT(o) FROM OrderBean o WHERE o.caregiver.caregiverNO = :caregiverNO AND o.status NOT IN ('完成', '已取消')")
    long countOrderstatuscaregiverNO(@Param("caregiverNO") Integer caregiverNO);

    //根據MerchantTradeNo改變訂單狀態
    @Modifying
    @Transactional
    @Query("UPDATE OrderBean o SET o.status = :status WHERE o.MerchantTradeNo = :merchantTradeNo")
    int updateOrderStatusByMerchantTradeNo(String merchantTradeNo, String status);
    //根據MerchantTradeNo改變付款方式
        @Modifying
        @Transactional
        @Query("UPDATE OrderBean o SET o.paymentMethod = :paymentMethod WHERE o.MerchantTradeNo = :merchantTradeNo")
        int updatePaymentMethodByMerchantTradeNo(String merchantTradeNo, String paymentMethod);
      //根據MerchantTradeNo改變TradeNo
        @Modifying
        @Query("UPDATE OrderBean o SET o.TradeNo = :tradeNo WHERE o.MerchantTradeNo = :merchantTradeNo")
        int updateTradeNoByMerchantTradeNo(String merchantTradeNo, String tradeNo);
     // 查詢超過 7 天仍未付款的訂單
        @Query("SELECT o FROM OrderBean o WHERE o.orderDate <= :cutoffDate AND o.status = '未付款'")
        List<OrderBean> findUnpaidOrdersBefore(@Param("cutoffDate") Date cutoffDate);
    }
	




