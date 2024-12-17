package email.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
// import email.backend.databaseAccess.MailRepository;
import email.backend.services.MailService;
import email.backend.services.UserService;
import email.backend.tables.Mail;

@RestController
@CrossOrigin("*")
@RequestMapping("/mail")
public class MailController {

   @Autowired
   private MailService mailService;
   @Autowired
   private UserService userService;
   // @Autowired
   // private MailboxService mailboxService;
   
   @PutMapping("/create")
   public MailDTO createMail(@RequestBody MailDTO maildDto,
                          @RequestHeader("Authorization") String token) {
      // System.out.println("entered request");
      Mail mail = maildDto.toMail(userService.getUser(token), userService, mailService);
      mailService.createMailToDrafts(mail);
      return new MailDTO(mail);
   }

   @PostMapping("/send")
   public ResponseEntity<?> sendMail(@RequestBody MailDTO mailDto) {
      try {
         Mail mail = mailService.sendMail(userService.getUserFromAddress(mailDto.getSenderAddress()), mailDto);
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body(new MailDTO(mail));
      
      } catch (Exception e) {
         System.out.println(e.getMessage());
         
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @GetMapping("/all")
   public ResponseEntity<?> getAllMails(@RequestHeader("Authorization") String token) {
      try {
         List<MailboxDTO> mailboxes = mailService.getAllMails(userService.getUser(token));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body(mailboxes);
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }
   
   @GetMapping("/getmail")
   public Mail getSingleMail(@RequestParam long id)
   {
         System.out.println("here");
         Mail email = mailService.getMailById(id);
         return email;
   }

   @DeleteMapping("/delete/{mailId}")
   public void deleteMail(@PathVariable Long mailId) {
      mailService.deleteEmail(mailId);
   }

}