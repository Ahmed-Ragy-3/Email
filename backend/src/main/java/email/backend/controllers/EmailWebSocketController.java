package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import email.backend.DTO.WebSocketMsgDTO;

@Controller
public class EmailWebSocketController {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/send-email") // Maps to "/app/send-email"
    public void sendEmail(@RequestBody WebSocketMsgDTO message, String recieverAddress, @Header("email") String emailAddress) {
        String destination = "/topic/emails/" + recieverAddress;
        simpMessagingTemplate.convertAndSend(destination, message);
        System.out.println("Done sending by websockets to reciever");
    }
}