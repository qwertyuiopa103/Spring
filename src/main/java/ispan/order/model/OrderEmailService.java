package ispan.order.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.service.UserService;

@Service
public class OrderEmailService {
	@Autowired
    private JavaMailSender mailSender;
	    @Autowired
	    private OrderRepository orderRepository;
	    @Autowired
		private UserService userService;
	    public void sendPaymentReminderEmail(int orderId) {
	        Optional<OrderBean> orderOptional = orderRepository.findById(orderId);
	        
	        if (orderOptional.isPresent()) {
	            OrderBean order = orderOptional.get();
	            UserBean user=userService.queryOne(order.getUser().getUserID());
	            
	            String userEmail =user.getUserEmail();
	            System.out.println(user.getUserEmail());
	            
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(userEmail);
	            message.setSubject("心護家_訂單付款通知");
	            message.setText("您的訂單已成立，請七天內盡速付款。\n\n" +
	                            "首頁網址：http://localhost:5173/#/home");
	            
	            mailSender.send(message);
	        }
	    }
}
