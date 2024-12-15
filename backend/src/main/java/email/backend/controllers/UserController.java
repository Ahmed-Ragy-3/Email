package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.server.ResponseStatusException;

// import email.backend.databaseAccess.UserRepository;
import email.backend.services.UserService;
import email.backend.services.MailboxService;
import email.backend.tables.User;
// import email.backend.tables.Mail;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

   @Autowired
   UserService userService;

   @Autowired
   MailboxService mailboxService;
   
   // @PostMapping("/getMails/{mailboxName}")
   // public List<Mail> getMailbox(@PathVariable String mailboxName, @RequestHeader("Authorization") String authHeader) {
   //    // Extract the token from the Authorization header (assuming it's prefixed with "Bearer ")
   //    String token = authHeader.substring(7); // "Bearer " is 7 characters long

   //    // Decode the token and get the user ID
   //    long userId = userService.extractUserId("iuofwe", token);

   //    // Fetch the user based on the user ID
   //    User user = userService.getUser(userId);

   //    return mailboxService.getEmailsInMailbox(user, mailboxName);
   // }


   @DeleteMapping("/delete/{userId}")
   public void deleteUser(@PathVariable Long userId) {
      // userService.deleteUser(userService.getUser(userId));
   }
 

}
