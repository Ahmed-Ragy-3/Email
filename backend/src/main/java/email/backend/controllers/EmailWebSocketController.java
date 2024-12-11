package email.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import email.backend.tables.Mail;

@Controller
public class EmailWebSocketController {

    @MessageMapping("/send-email") // Maps to "/app/send-email"
    @SendTo("/topic/emails")       // Broadcasts to "/topic/emails"
    public Mail sendEmail(Mail emailMessage) {
        // Logic to save email to the database or process the email
        return emailMessage; // This will be sent to all subscribers
    }
}
