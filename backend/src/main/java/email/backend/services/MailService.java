package email.backend.services;

import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import email.backend.databaseAccess.MailRepository;
// import email.backend.databaseAccess.UserRepository;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.databaseAccess.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
// import java.util.Set;

@Service
@AllArgsConstructor
public class MailService {

   @Autowired
   private MailRepository mailRepository;
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private MailboxRepository mailboxRepository;

   @Autowired
   private UserService userService;

   /**
    * create mail and put into drafts category without any validations
    * @param Mail mail 
    */
    @Transactional
   public Mail createMailToDrafts(Mail mail) {
      
      User sender = mail.getSender();
      // System.out.println("1 =================" + mail.getContent() + "===================" );
      sender.getMailboxes().get(MailboxService.DRAFTS_INDEX).getMails().add(mail);
      // System.out.println("2 =================" + mail.getContent() + "===================" );
      // System.out.println(sender.getId());
      // System.out.println(sender.getName());
      // System.out.println(sender.getEmailAddress());
      // System.out.println(sender.getPassword());
      // System.out.println("2 =================" + mail.getContent() + "===================" );
      
      // userRepository.save(sender);
      
      // System.out.println("3 =================" + mail.getContent() + "===================" );
      // mailRepository.save(mail);
      return mail;
   }

   public boolean isReady() {
      
      return true;
   }

   public Mail getMailById(Long mailId){
      return mailRepository.findById(mailId).get();
   }

   public List<Mail> getEmailsInMailbox(User user, String mailboxName) {
      Mailbox mailbox = mailboxRepository.findByOwnerAndName(user, mailboxName);
      return new ArrayList<Mail>(mailbox.getMails());
   }

   // we can use this to get sent mailbox
   public List<Mail> getMailsBySenderId(Long senderId) {
      return mailRepository.findBySender(userService.getUser(senderId));
   }

   public void deleteEmail(Long mailId) {
      if (!mailRepository.existsById(mailId)) {
         throw new IllegalArgumentException("Mail not found");
      }
      mailRepository.deleteById(mailId);
   }

   public void sendMails(Mail mail) {
      // MailSenderProxy mailSenderProxy;

      mailRepository.save(mail);
   }

   public Importance getImportanceFromString(String importance) {
      if(importance == null) return Importance.NORMAL;

      importance = importance.toLowerCase();

      switch (importance) {
         case "URGENT":
            return Importance.URGENT;
         case "IMPORTANT":
            return Importance.IMPORTANT;
         case "NORMAL":
            return Importance.NORMAL;
         case "DELAYABLE":
            return Importance.DELAYABLE;
      }

      return Importance.NORMAL;
   }

}
