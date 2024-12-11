package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
   
   @PutMapping("/create/{userId}")
   public void createMailbox(@PathVariable Long userId) {
      mailboxService.createCategory(userService.getUser(userId));
   }

   @DeleteMapping("/delete/{mailId}")
   public void deleteMailbox(@PathVariable Long mailboxId) {
      mailboxService.delete(mailboxId);
   }

}
