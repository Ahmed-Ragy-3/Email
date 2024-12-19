package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import email.backend.DTO.MailboxDTO;
import email.backend.services.MailboxService;
import email.backend.services.UserService;
import email.backend.tables.Mailbox;


@RestController
@CrossOrigin("*")
@RequestMapping("/mailbox")
public class MailboxController {
   
   @Autowired
   private MailboxService mailboxService;
   
   @Autowired
   private UserService userService;
   
   @PutMapping("/add")
   public ResponseEntity<?> createMailbox(
      @RequestBody MailboxDTO mailboxDto,
      @RequestHeader("Authorization") String token
   ) {

      try {
         System.out.println(mailboxDto.getName());
         Mailbox mailbox = mailboxService.createMailbox(userService.getUser(token), mailboxDto.getName());
         mailboxDto.setId(mailbox.getId());
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(mailboxDto);
      
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
   }

   @PostMapping("/edit")
   public ResponseEntity<?> editMailbox(
      @RequestBody MailboxDTO mailboxDto,
      @RequestHeader("Authorization") String token
   ) {            

      try {
         mailboxService.editMailbox(userService.getUser(token), mailboxDto);
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(mailboxDto);
      
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
   }

   @PostMapping("/delete")
   public ResponseEntity<?> deleteMailbox(
      @RequestBody MailboxDTO mailboxDto,
      @RequestHeader("Authorization") String token
   ) {                           
      try {
         mailboxService.deleteMailbox(userService.getUser(token), mailboxDto);
         
         return ResponseEntity.status(HttpStatus.ACCEPTED).body("Mailbox Deleted Successfully");
      
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
   }
}
