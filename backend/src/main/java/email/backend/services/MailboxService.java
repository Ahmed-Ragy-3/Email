package email.backend.services;

import java.util.ArrayList;
// import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
// import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import email.backend.databaseAccess.MailboxRepository;
// import email.backend.databaseAccess.MailRepository;
// import email.backend.databaseAccess.UserRepository;
import email.backend.services.filter.*;

@Service
@AllArgsConstructor
public class MailboxService {
   
   // private final MailRepository mailRepository;
   // private final UserRepository userRepository;
   private final MailboxRepository mailboxRepository;

   public List<Mail> getEmailsInMailbox(User user, String mailboxName) {
      Mailbox mailbox = mailboxRepository.findByUserAndName(user, mailboxName);
      return new ArrayList<Mail>(mailbox.getMails());
   }

   // @Transactional
   public void moveTo(Mailbox from, Mailbox to, Mail mail) {
      copyTo(mail, to);
      deleteFrom(mail, from);
   }

   // @Transactional
   public void deleteFrom(Mail mail, Mailbox mailbox) {
      mailbox.getMails().remove(mail);
      mail.getMailboxes().remove(mailbox);
    
   }

   // @Transactional
   public void copyTo(Mail mail, Mailbox mailbox) {
      mailbox.getMails().add(mail);
      mail.getMailboxes().add(mailbox);

   }

   // @Transactional
   // public void delete(Mailbox mailbox, User user) {
   //    for (Mail mail : mailbox.getMails()) {
   //       mail.getMailboxes().remove(mailbox);
   //    }
   //    user.getMailboxes().remove(mailbox);
   //    mailboxRepository.delete(mailbox);
   // }

   // @Transactional
   public void delete(Long mailboxId) {
      // for (Mail mail : mailbox.getMails()) {
      //    mail.getMailboxes().remove(mailbox);
      // }
      // user.getMailboxes().remove(mailbox);
      mailboxRepository.delete(getMailbox(mailboxId));
   }

   public Mailbox getMailbox(Long mailboxId) {
      return mailboxRepository.findById(mailboxId).get();
   }
   
   public void createCategory(User user) {
      Mailbox mailbox = new Mailbox();
      mailbox.setUser(user);
      mailbox.setName("New Category");
      mailboxRepository.save(mailbox);
   }

   public void createFriendZone(User user) {
      //
   }
   
   public void rename(Mailbox mailbox, String newName) {
      mailbox.setName(newName);
   }
   
   public List<Mail> search(String regex) {
      return mailboxRepository.searchMailsByKeyword(regex);
   }


   public List<Mail> filter(Long mailboxId, Importance importance, Boolean attachment, Date date1, Date date2) {
      List<Mail> filteredMails = new ArrayList<>();
      Criteria criteria;
      
      if(importance != null) {
         criteria = new ImportanceCriteria(importance);
         filteredMails = criteria.meetCriteria(filteredMails);
      }
      
      if(attachment != null) {
         criteria = new AttachmentCriteria(attachment.booleanValue());
         criteria.meetCriteria(filteredMails);
      }
      
      if(date1 != null) {
         criteria = new DateCriteria(date1, date2);  
      }

      return null;
   }


   public List<Mail> sortBy(List<Mail> mails, String sortKey) {
      switch (sortKey) {
         case "Date":
            mails.sort(new Comparator<Mail>() {
               @Override
               public int compare(Mail m1, Mail m2) {
                  return m1.getDate().compareTo(m2.getDate());
               }
            });
            break;
         
         case "Importance":
            mails.sort(new Comparator<Mail>() {
               @Override
               public int compare(Mail m1, Mail m2) {
                  return m1.getImportance().compareTo(m2.getImportance());
               }
            });
            break;
         default:
            break;
      }
      return mails;
   }

}