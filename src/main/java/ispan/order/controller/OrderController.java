package ispan.order.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ispan.caregiver.model.CaregiverBean;
import ispan.order.dto.OrderStatusUpdateDTO;
import ispan.order.model.OrderBean;
import ispan.order.model.OrderEmailService;
import ispan.order.model.OrderService;
import ispan.user.model.UserBean;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController //返回的數據自動轉換成 JSON 格式。
@RequestMapping("/orders") //設定這個控制器的基礎路徑為 /orders
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
	@Autowired //自動注入 OrderService
	private OrderService orderService;
	@Autowired
	private OrderEmailService orderEmailService;
	//新增
	@PostMapping("/createOrder")
	public ResponseEntity<String> createOrder(@RequestBody OrderBean order) {
	    try {
	        // 創建訂單並保存
	        OrderBean createdOrder = orderService.createOrder(order);
	        orderEmailService.sendPaymentReminderEmail(createdOrder.getOrderId());
	        // 返回創建的 OrderBean (成功的回應)
	        return new ResponseEntity<>("訂單新增成功", HttpStatus.CREATED);
	        
	        
	    } catch (IllegalArgumentException e) {
	        // 捕獲到錯誤，返回具體的錯誤訊息
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        // 捕獲其他錯誤，回傳通用錯誤訊息
	        return new ResponseEntity<>("新增訂單失敗，請稍後再試", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
//
//	@GetMapping("/AllOrders")
//	public ResponseEntity<List<OrderBean>> findAllOrders(
//	    @RequestParam(defaultValue = "1") int page,  // 頁碼，預設為第1頁
//	    @RequestParam(defaultValue = "10") int pageSize  // 每頁顯示數量，預設為10
//	) {
//	    List<OrderBean> allOrders = orderService.findAllOrders(page, pageSize);
//	    return new ResponseEntity<>(allOrders, HttpStatus.OK);
//	}
	 // 根據訂單 ID 查詢訂單
    @GetMapping("/OrderByOrderId/{orderId}")
    public ResponseEntity<OrderBean> getOrderById(@PathVariable int orderId) {
        // 使用 Service 查詢訂單
        OrderBean order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
	//根據使用者查詢訂單
	@GetMapping("/OrderByUser")
	public ResponseEntity<List<OrderBean>>findOrdersByUser(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = userDetails.getUsername(); // 假設 username 存儲的是 userID
		UserBean user = new UserBean();  // 創建 UserBean 實例，假設已經有方法查找 User
        user.setUserID(userId);
        List<OrderBean> orders = orderService.findOrdersByUsers(user);
        return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	// 根據 Caregiver 查詢訂單
    @GetMapping("/OrderByCaregiver/{caregiverNO}")
    public ResponseEntity<List<OrderBean>> findOrdersByCaregiver(@PathVariable int caregiverNO) {
        CaregiverBean caregiver = new CaregiverBean();  // 創建 CaregiverBean 實例
        caregiver.setCaregiverNO(caregiverNO);
        List<OrderBean> orders = orderService.findOrdersByCaregiver(caregiver);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<?> updateOrderStatusAndCancellationId(
    	    @PathVariable int orderId,
    	    @RequestBody(required = false) OrderStatusUpdateDTO updateDTO) {
    	    
    	    if (updateDTO == null) {
    	        return ResponseEntity.badRequest().body("請求體不能為空");
    	    }
    	    
    	    try {
    	        OrderBean updatedOrder = orderService.updateOrderStatusAndCancellationId(
    	            orderId, 
    	            updateDTO.getStatus(), 
    	            updateDTO.getCancellationId()
    	        );
    	        return ResponseEntity.ok(updatedOrder);
    	    } catch (Exception e) {
    	        return ResponseEntity.badRequest().body(e.getMessage());
    	    }
    	}
    
    
    //更新
//    @PutMapping("/UpdateOrder/{orderId}")
//    public ResponseEntity<String> updateOrder(@PathVariable int orderId, @RequestBody OrderBean order) {
//        order.setOrderId(orderId);
//        try {
//            OrderBean uOrder = orderService.updateOrder(order);
//            if (uOrder != null) {
//                return new ResponseEntity<>("更新成功", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("找不到該訂單", HttpStatus.NOT_FOUND);
//            }
//        } catch (IllegalArgumentException e) {
//            // 捕獲到錯誤，返回具體的錯誤訊息
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            // 捕獲其他錯誤，回傳通用錯誤訊息
//            return new ResponseEntity<>("新增訂單失敗，請稍後再試", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    
//    //刪除
//    @DeleteMapping("/deleteOrder/{orderId}")
//	public ResponseEntity<Void>deleteOrderById(@PathVariable int orderId){
//    	try {
//			orderService.deleteOrderById(orderId);
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//    }
 
    
    
    
    }


