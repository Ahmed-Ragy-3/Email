package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import email.backend.services.MailboxService;
import email.backend.services.UserService;
// import email.backend.tables.Mailbox;


import email.backend.tables.User;
import email.backend.tables.Mail;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/mailbox")
public class MailboxController {
   @Autowired
   private MailboxService mailboxService;
   @Autowired
   private UserService userService;
   
   @PutMapping("/create/{userId}")
   public void createMailbox(@PathVariable Long userId) {
      mailboxService.createCategory(userService.getUser(userId));
   }

   @DeleteMapping("/delete/{mailId}")
   public void deleteMailbox(@PathVariable Long mailboxId) {
      mailboxService.delete(mailboxId);
   }

   @PostMapping("/getMails/{mailboxName}")
   public List<Mail> getMailbox(@RequestBody User user, @PathVariable String mailboxName) {
      return mailboxService.getEmailsInMailbox(user, mailboxName);
   }

}
