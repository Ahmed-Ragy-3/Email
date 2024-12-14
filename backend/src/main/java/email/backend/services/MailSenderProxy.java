package email.backend.services;

import org.springframework.stereotype.Service;

import email.backend.tables.Mail;
import lombok.Setter;

@Service
public class MailSenderProxy {
   
   private static final String DEFAULT_CATEGORY = "Inbox";   
   
   @Setter
   Mail mail;

   public boolean isScheduled() {
      
      return mail.getDate().future();
   }

   // public boolean () {
   //    return true;
   // }

   public boolean isReady() {
      return true;
   }

   // public boolean sendMail() {
   //    if(!isReady()) return false;

   //    if(isScheduled()) {
         
   //    }else {

   //    }
   // }
}
