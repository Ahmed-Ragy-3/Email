package email.backend.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import email.backend.databaseAccess.MailboxRepository;
import email.backend.databaseAccess.UserRepository;
import email.backend.services.filter.AttachmentCriteria;
import email.backend.services.filter.Criteria;
import email.backend.services.filter.DateCriteria;
import email.backend.services.filter.ImportanceCriteria;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailboxService {

   public static final int INBOX_INDEX = 0;
   public static final int DRAFTS_INDEX = 1;
   public static final int SENT_INDEX = 2;
   public static final int TRASH_INDEX = 3;
   public static final int STARRED_INDEX = 4;
   public static final int SCHEDULED_INDEX = 5;
   public static final int SPAM_INDEX = 6;
   
   @Autowired
   private final UserRepository userRepository;
   @Autowired
   private final MailboxRepository mailboxRepository;

   public List<Mail> getEmailsInMailbox(User user, String mailboxName) {
      Mailbox mailbox = mailboxRepository.findByOwnerAndName(user, mailboxName);
      return new ArrayList<Mail>(mailbox.getMails());
   }

   public void addTo(Mailbox mailbox, Mail mail) {
      mailbox.getMails().add(mail);
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
      Optional<Mailbox> mailbox = mailboxRepository.findById(mailboxId); 
      return mailbox.isPresent() ? mailbox.get() : null;
   }
   
   public Mailbox getMailbox(User user, String name) {
      return mailboxRepository.findByOwnerAndName(user, name);
   }
   public Mailbox getMailbox(User user, int index) throws IllegalArgumentException {
      if(user.getMailboxes().size() <= index) {
         throw new IllegalArgumentException("In getMailbox: Unable to access mail box of index" + index);
      }
      return user.getMailboxes().get(index);
   }
   
   public void createCategory(User user, String name) {
      Mailbox mailbox = new Mailbox();
      mailbox.setName(name);
      mailbox.setOwner(user);
      mailboxRepository.save(mailbox);
      
      user.getMailboxes().add(mailbox);
      userRepository.save(user);
   }

   public void createFriendZone(User user) {
      //
   }
   
   public void rename(Mailbox mailbox, String newName) {
      mailbox.setName(newName);
   }
   
   // public List<Mail> search(String regex) {
   //    return mailboxRepository.searchMailsByKeyword(regex);
   // }


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