package ispan.order.model;


import java.sql.Date;
import java.util.List;


import ispan.caregiver.model.CaregiverBean;
import ispan.user.model.UserBean;

public interface OrderService {
	//新增 
	OrderBean createOrder(OrderBean order);
	//查詢所有
	List<OrderBean> findAllOrders();
	//根據使用者ID查訂單
	List<OrderBean> findOrdersByUsers(UserBean user);
	// 依照Caregiver查詢
    List<OrderBean> findOrdersByCaregiver(CaregiverBean caregiver);	
	//更新
    OrderBean updateOrder(OrderBean order);
	//刪除
    void deleteOrderById(int orderId);
	//根據訂單ID查訂單
    OrderBean getOrderById(int orderId);
    //根據訂單ID改狀態
    boolean updateOrderStatusById(int orderId, String status);
    //根據訂單ID改付款方式
    public OrderBean updatePaymentMethodByOrderId(int orderId, String PaymentMethod);
    //根據訂單ID新增交易號碼(綠界的)
    OrderBean updateTradeNo(int orderId, String tradeNo);
    //根據訂單ID新增交易號碼(自己產生的UUID)
    OrderBean updateMerchantTradeNo(int orderId, String MerchantTradeNo);
    
    //檢查看護有無重疊時間的訂單
    boolean checkForOverlappingOrder(int caregiverNO, int orderId, Date startDate, Date endDate);
    //算總數
    long getCount();
    //根據訂單ID改狀態和取消ID
    OrderBean updateOrderStatusAndCancellationId(int orderId, String status, Integer cancellationId);
    //根據訂單ID找取消ID
    Integer getCancellationIdByOrderId(int orderId);
    //根據MerchantTradeNo改變訂單狀態
	void updateOrderStatusBymerchantTradeNo(String merchantTradeNo, String string);
    //根據MerchantTradeNo改變付款方式
	void updatePaymentMethodBymerchantTradeNo(String merchantTradeNo, String paymentMethod);
	//根據MerchantTradeNo改變TradeN
	int updateTradeNoByMerchantTradeNo(String merchantTradeNo, String tradeNo);
	//自動取消
	void autoCancelUnpaidOrders();
}
