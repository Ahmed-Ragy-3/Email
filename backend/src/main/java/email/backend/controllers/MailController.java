package email.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import email.backend.tables.Mail;
import email.backend.databaseAccess.MailDataAccess;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin("*")
@RequestMapping("/mail")
public class MailController {
 
   private final MailDataAccess mailDataAccess = new MailDataAccess(); 

   @PutMapping("/create")
   public void putMethodName(@RequestBody Mail mail) {
      
      // return ;
   }
}
