package ispan.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ispan.order.model.OrderService;

@RestController
@RequestMapping("/api/OrderNoAuth")
public class OrderNoAuthContronller {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/Count")
	public long getCount() {
		return orderService.getCount();
	}
	
	// 根據訂單ID查詢取消ID
    @GetMapping("/getCancellationIdByOrderId/{orderId}")
    public ResponseEntity<Integer> getCancellationIdByOrderId(@PathVariable int orderId) {
        Integer cancellationId = orderService.getCancellationIdByOrderId(orderId);
        if (cancellationId != null) {
            return ResponseEntity.ok(cancellationId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	
}
