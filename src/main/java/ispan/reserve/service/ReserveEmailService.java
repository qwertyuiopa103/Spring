package ispan.reserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReserveEmailService {
	@Autowired
    private JavaMailSender mailSender;
	
	@Transactional
	public void sendWorkRequestEmail(String to) {
//		System.out.println("mailSender is null: " + (mailSender == null));
//		System.out.println("email:"+to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("心護家_工作請求");
        message.setText("您有新的工作邀請，請到網站裡登入查看工作訊息");
        mailSender.send(message);
    }
	@Transactional
	public void sendWorkCancelEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("心護家_服務請求");
        message.setText("您的服務請求已經被拒絕，請到網站裡重新預約");
        mailSender.send(message);
    }
}