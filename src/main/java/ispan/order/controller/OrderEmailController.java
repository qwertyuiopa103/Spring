package ispan.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ispan.order.model.OrderEmailService;

@RestController
@RequestMapping("/api/order/email")
public class OrderEmailController {
    @Autowired
    private OrderEmailService orderEmailService;

    @PostMapping("/paymentReminder/{orderId}")
    public ResponseEntity<String> sendPaymentReminder(@PathVariable int orderId) {
        orderEmailService.sendPaymentReminderEmail(orderId);
        return ResponseEntity.ok("付款提醒信已發送");
    }

}
