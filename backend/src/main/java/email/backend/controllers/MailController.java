package email.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.services.MailSenderProxy;
// import email.backend.databaseAccess.MailRepository;
import email.backend.services.MailService;
import email.backend.services.MailboxService;
import email.backend.tables.Mail;
import email.backend.services.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/mail")
public class MailController {

   @Autowired
   private MailService mailService;
   
   @Autowired
   private MailSenderProxy mailSenderProxy;
   
   @Autowired
   private UserService userService;
   @Autowired
   private MailboxService mailboxService;
   
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
         Mail mail = mailSenderProxy.sendMail(userService.getUserFromAddress(mailDto.getSenderAddress()), mailDto);
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

   @DeleteMapping("/moveToTrash")
   public ResponseEntity<?> moveToTrash( @RequestHeader("Authorization") String token, 
                                    @RequestBody MailDTO mailDto,
                                    @RequestBody MailboxDTO FromMailboxDto) {
      try {
         mailboxService.moveToTrash(  mailboxService.getMailbox(FromMailboxDto.getId()),
                                      mailService.getMailById(mailDto.getId()));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Move from" + FromMailboxDto.getName() + " to trash");
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @DeleteMapping("/delete")
   public ResponseEntity<?> delete( @RequestHeader("Authorization") String token, 
                                    @RequestBody MailDTO mailDto) {
      try {
         mailboxService.delete(mailService.getMailById(mailDto.getId()));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Deleted the mail permenantly");
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }
  
   @PostMapping("/moveTo")
      public ResponseEntity<?> moveMailToMailbox( @RequestHeader("Authorization") String token, 
                                    @RequestBody MailDTO mailDto,
                                    @RequestBody MailboxDTO FromMailboxDto,
                                    @RequestBody MailboxDTO ToMailboxDto) {
      try {
         mailboxService.moveTo(  mailboxService.getMailbox(FromMailboxDto.getId()),
                                 mailboxService.getMailbox(ToMailboxDto.getId()),
                                 mailService.getMailById(mailDto.getId()));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Move from" + FromMailboxDto.getName() + " to " + ToMailboxDto.getName());
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

    // @PostMapping("/copyTo")
   //    public ResponseEntity<?> copyMailToMailbox( @RequestHeader("Authorization") String token, 
   //                                  @RequestBody MailDTO mailDto,
   //                                  @RequestBody MailboxDTO mailboxDto) {
   //    try {
   //       mailboxService.copyTo(mailboxService.getMailbox(mailboxDto.getId()), mailService.getMailById(mailDto.getId()));
   //       return ResponseEntity
   //             .status(HttpStatus.ACCEPTED)
   //             .body("Copied to" + mailboxDto.getName());
   //    } catch (Exception e) {
   //       return ResponseEntity
   //          .status(HttpStatus.BAD_REQUEST)
   //          .body(e.getMessage());
   //    }
   // }
}