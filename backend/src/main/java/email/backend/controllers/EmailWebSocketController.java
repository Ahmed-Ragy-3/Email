package email.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import email.backend.tables.Mail;

@Controller
public class EmailWebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/send-email") // Maps to "/app/send-email"
    public void sendEmail(String emailMessage, @Header("email") String email) {
        System.out.println(email);
        String destination = "/topic/emails/"+email;
        System.out.println(destination);
        simpMessagingTemplate.convertAndSend(destination,email);
    }
}
