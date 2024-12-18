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

import email.backend.DTO.AttachedMailDTO;
import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.services.MailSenderProxy;
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
   public ResponseEntity<?> createDraftMail(@RequestBody AttachedMailDTO attachedMailDto, 
   @RequestHeader("Authorization") String token) {
      try {
         AttachedMailDTO dto = mailService.createDraftMail(attachedMailDto, userService.getUser(token));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body(dto);
      
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PostMapping("/edit")
   public ResponseEntity<?> editDraftMail(@RequestBody MailDTO mailDto, 
   @RequestHeader("Authorization") String token) {
      try {
         MailDTO dto = mailService.editDraftMail(mailDto, userService.getUser(token));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body(dto);
      
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PostMapping("/send")
   public ResponseEntity<?> sendMail(
      @RequestBody AttachedMailDTO attachedMailDto,
      @RequestHeader("Authorization") String token
   ) {
      try {
         MailDTO mailDto = attachedMailDto.getMailDto();
         
         System.out.println(mailDto.getContent());
         
         Mail mail = mailSenderProxy.sendMail(
         userService.getUserFromAddress(mailDto.getSenderAddress()), 
         attachedMailDto);
         
         
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
   public AttachedMailDTO getSingleMail(@RequestParam long id) {
      // System.out.println("here");
      Mail mail = mailService.getMailById(id);
      return new AttachedMailDTO(mail);
   }

   @DeleteMapping("/moveToTrash/{fromMailboxId}")
   public ResponseEntity<?> moveToTrash(
      @RequestHeader("Authorization") String token, 
      @RequestBody MailDTO mailDto,
      @PathVariable Long fromMailboxId
   ) {

      try {
         mailboxService.moveToTrash(  
            mailboxService.getMailbox(fromMailboxId),
            mailService.getMailById(mailDto.getId()),
            userService.getUser(token)
         );
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Moved to trash");
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
         mailboxService.deleteFromTrash(mailService.getMailById(mailDto.getId()), userService.getUser(token));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Deleted the mail permenantly");
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }
  
   @PostMapping("/moveTo/{fromMailboxId}/{toMailboxId}")
      public ResponseEntity<?> moveMailToMailbox( 
                                    @RequestHeader("Authorization") String token,
                                    @RequestBody MailDTO mailDto,
                                    @PathVariable Long fromMailboxId,
                                    @PathVariable Long toMailboxId) {
      try {
         mailboxService.moveTo(  mailboxService.getMailbox(fromMailboxId),
                                 mailboxService.getMailbox(toMailboxId),
                                 mailService.getMailById(mailDto.getId()),
                                 userService.getUser(token));
         return ResponseEntity
               .status(HttpStatus.ACCEPTED)
               .body("Moved Successfully");
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