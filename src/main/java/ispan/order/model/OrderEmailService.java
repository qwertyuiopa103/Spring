package ispan.order.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ispan.user.model.UserRepository;

@Service
public class OrderEmailService {
	@Autowired
    private JavaMailSender mailSender;
	    @Autowired
	    private OrderRepository orderRepository;

	    public void sendPaymentReminderEmail(int orderId) {
	        Optional<OrderBean> orderOptional = orderRepository.findById(orderId);
	        
	        if (orderOptional.isPresent()) {
	            OrderBean order = orderOptional.get();
	            String userEmail = order.getUser().getUserEmail();
	            
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(userEmail);
	            message.setSubject("心護家_預約付款通知");
	            message.setText("您的預約已成立，請盡速付款。\n\n" +
	                            "首頁網址：http://localhost:5173/#/home");
	            
	            mailSender.send(message);
	        }
	    }
}
