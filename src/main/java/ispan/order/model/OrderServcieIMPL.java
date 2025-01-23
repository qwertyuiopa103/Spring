package ispan.order.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ispan.caregiver.model.CaregiverBean;
import ispan.order.dto.OrderStatusUpdateDTO;
import ispan.orderCancel.model.OrderCancelBean;
import ispan.orderCancel.model.OrderCancelRepository;
import ispan.user.model.UserBean;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class OrderServcieIMPL implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderCancelRepository orderCancelRepository;

	//新增
	@Override
	public OrderBean createOrder(OrderBean order) {
		// 檢查 User 是否存在
	    if (order.getUser() == null || !orderRepository.existsUserById(order.getUser().getUserID())) {
	        throw new IllegalArgumentException("不存在這名使用者\n搞清楚再來");
	    }

	    // 檢查 Caregiver 是否存在
	    if (order.getCaregiver() == null || !orderRepository.existsCaregiverById(order.getCaregiver().getCaregiverNO())) {
	        throw new IllegalArgumentException("沒有這個看護kora");
	    }
		return orderRepository.save(order);
	}
	//全部
	@Override
	public List<OrderBean> findAllOrders() {
	    // 查詢並返回所有的訂單資料
	    return orderRepository.findAll();  // 直接查詢所有訂單
	}
//	//根據使用者查訂單
	@Override
	public List<OrderBean> findOrdersByUsers(UserBean user) {
		return orderRepository.findByUser(user);
	}
	//根據看護查訂單
	@Override
	public List<OrderBean> findOrdersByCaregiver(CaregiverBean caregiver) {
		return orderRepository.findByCaregiver(caregiver);
	}
	//更新
	@Override
	public OrderBean updateOrder(OrderBean order) {
		// 檢查 User 是否存在
	    if (order.getUser() == null || !orderRepository.existsUserById(order.getUser().getUserID())) {
	        throw new IllegalArgumentException("不存在這名使用者\n搞清楚再來");
	    }

	    // 檢查 Caregiver 是否存在
	    if (order.getCaregiver() == null || !orderRepository.existsCaregiverById(order.getCaregiver().getCaregiverNO())) {
	        throw new IllegalArgumentException("沒有這個看護kora");
	    }
		// 更新 User
	    if (order.getUser() != null) {
	        order.setUser(order.getUser());
	    }

	    // 更新 Caregiver
	    if (order.getCaregiver() != null) {
	        order.setCaregiver(order.getCaregiver());
	    }

	    // 更新時間
	    if (order.getStartDate() != null && order.getEndDate() != null) {
	        if (order.getEndDate().before(order.getStartDate())) {
	            throw new IllegalArgumentException("End date cannot be before start date.");
	        }
	        order.setStartDate(order.getStartDate());
	        order.setEndDate(order.getEndDate());
	    }

	    // 更新金額
	    if (order.getTotalPrice() > 0) {
	        order.setTotalPrice(order.getTotalPrice());
	    }

	    // 更新狀態
	    if (order.getStatus() != null && !order.getStatus().isEmpty()) {
	        order.setStatus(order.getStatus());
	    }
	 // 更新付款方式
	    if (order.getPaymentMethod() != null && !order.getPaymentMethod().isEmpty()) {
	        order.setPaymentMethod(order.getPaymentMethod());
	    } 
	    // 保存更新後的訂單
	    return orderRepository.save(order);
	}
	//刪除
	@Override
	public void deleteOrderById(int orderId) {
		if(orderRepository.existsById(orderId))
		orderRepository.deleteById(orderId);
	}
	 // 根據訂單 ID 查詢訂單
    @Override
    public OrderBean getOrderById(int orderId) {
        // 使用 repository 查詢資料庫中的訂單
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
    }
 // 根據訂單 ID 更新訂單狀態
    @Override
    public boolean updateOrderStatusById(int orderId, String status) {
        try {
            OrderBean order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
            
            order.setStatus(status);  // 設置新狀態
            orderRepository.save(order);  // 保存更新後的訂單
            return true;
        } catch (Exception e) {
            System.out.println("更新訂單狀態時發生錯誤：" + e.getMessage());
            return false;
        }
    }
    @Override
    public OrderBean updatePaymentMethodByOrderId(int orderId, String PaymentMethod) {
        OrderBean order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單未找到，ID: " + orderId));

        order.setPaymentMethod(PaymentMethod);  // 更新付款方式
        return orderRepository.save(order);        // 保存更新後的訂單
    }
    @Override
    public boolean checkForOverlappingOrder(int caregiverNO, int orderId, Date startDate, Date endDate) {
        // 調用 repository 中的查詢方法檢查是否有重疊的訂單
        return orderRepository.isOverlappingWithExistingOrder(caregiverNO, orderId, startDate, endDate);
    }
    
    @Override
    public long getCount() {
    	return  orderRepository.count();
    }
    @Override
    public OrderBean updateOrderStatusAndCancellationId(int orderId, String status, Integer cancellationId) {
        // 找訂單
        OrderBean order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("找不到此訂單"));

        // 更新狀態
        if (status != null && !status.trim().isEmpty()) {
            order.setStatus(status);
        }

        // 更新取消訂單ID
        if (cancellationId != null) {
            OrderCancelBean cancelBean = orderCancelRepository.findById(cancellationId)
                .orElseThrow(() -> new RuntimeException("找不到取消訂單記錄"));
            order.setCancellation(cancelBean);
        }

        // 打印傳入參數
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status); // 使用傳入的 status 參數
        System.out.println("Cancellation ID: " + cancellationId); // 使用傳入的 cancellationId 參數

        return orderRepository.save(order);
    }
    @Override
    public Integer getCancellationIdByOrderId(int orderId) {
        Optional<OrderBean> order = orderRepository.findById(orderId);
        return order.map(OrderBean::getCancellation).map(OrderCancelBean::getCancellationId).orElse(null);
    }

}

