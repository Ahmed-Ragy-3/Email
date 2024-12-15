package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import email.backend.DTO.MailDTO;
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
      System.out.println("entered request");
      Mail mail = maildDto.toMail(userService.getUser(token), userService, mailService);
      mailService.createMailToDrafts(mail);
      return new MailDTO(mail);
   }

   @DeleteMapping("/delete/{mailId}")
   public void deleteMail(@PathVariable Long mailId) {
      mailService.deleteEmail(mailId);
   }

}