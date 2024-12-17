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
        // System.out.println(emailAddress);
        // for(String recieverAddress : .getReceiversAddresses()) {
        //     // recieverAddress = recieverAddress.split("@mail.com")[0];
        String destination = "/topic/emails/" + recieverAddress;
        //     System.out.println(destination);
        simpMessagingTemplate.convertAndSend(destination, message);
        // }
        System.out.println("Done sending by websockets to reciever");
    }
}