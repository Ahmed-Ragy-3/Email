package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import email.backend.tables.Mail;

@Controller
public class EmailWebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/send-email") // Maps to "/app/send-email"
    public void sendEmail(String emailMessage, @Header("email") String emailAddress) {
        System.out.println(emailAddress);
        String destination = "/topic/emails/" + emailAddress;
        
        System.out.println(destination);
        simpMessagingTemplate.convertAndSend(destination, emailAddress);
    }
}
