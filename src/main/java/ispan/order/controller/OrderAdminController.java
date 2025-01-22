package ispan.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ispan.order.model.OrderBean;
import ispan.order.model.OrderService;
//test
@RestController //返回的數據自動轉換成 JSON 格式。
@RequestMapping("/api/ordersAdmin") //設定這個控制器的基礎路徑為 /orders

public class OrderAdminController {
	@Autowired //自動注入 OrderService
	private OrderService orderService;
	

	//新增
	@PostMapping("/createOrder")
	public ResponseEntity<String> createOrder(@RequestBody OrderBean order) {
	    try {
	        // 創建訂單並保存
	        OrderBean createdOrder = orderService.createOrder(order);

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

	@GetMapping("/AllOrders")
	public ResponseEntity<List<OrderBean>> findAllOrders(
	    @RequestParam(defaultValue = "1") int page,  // 頁碼，預設為第1頁
	    @RequestParam(defaultValue = "10") int pageSize  // 每頁顯示數量，預設為10
	) {
	    List<OrderBean> allOrders = orderService.findAllOrders(page, pageSize);
	    return new ResponseEntity<>(allOrders, HttpStatus.OK);
	}
	
	 @PutMapping("/UpdateOrder/{orderId}")
	    public ResponseEntity<String> updateOrder(@PathVariable int orderId, @RequestBody OrderBean order) {
	        order.setOrderId(orderId);
	        try {
	            OrderBean uOrder = orderService.updateOrder(order);
	            if (uOrder != null) {
	                return new ResponseEntity<>("更新成功", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("找不到該訂單", HttpStatus.NOT_FOUND);
	            }
	        } catch (IllegalArgumentException e) {
	            // 捕獲到錯誤，返回具體的錯誤訊息
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            // 捕獲其他錯誤，回傳通用錯誤訊息
	            return new ResponseEntity<>("新增訂單失敗，請稍後再試", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    
	    //刪除
	    @DeleteMapping("/deleteOrder/{orderId}")
		public ResponseEntity<Void>deleteOrderById(@PathVariable int orderId){
	    	try {
				orderService.deleteOrderById(orderId);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	    }
	    
}
