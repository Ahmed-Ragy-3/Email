package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import email.backend.databaseAccess.MailRepository;
import email.backend.services.MailService;
import email.backend.services.MailboxService;
import email.backend.tables.Mail;

@RestController
@CrossOrigin("*")
@RequestMapping("/mail")
public class MailController {

   @Autowired
   private MailService mailService;
   @Autowired
   private MailboxService mailboxService;
   
   @PutMapping("/create")
   public void createMail(@RequestBody Mail mail) {
      // mailService.;
   }

   @DeleteMapping("/delete/{mailId}")
   public void deleteMail(@PathVariable Long mailId) {
      // mailService.deleteById(mailId);
   }

}