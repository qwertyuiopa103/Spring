package ispan.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	
}
