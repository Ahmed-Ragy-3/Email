package email.backend.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import email.backend.DTO.MailboxDTO;
import email.backend.repo.MailRepository;
import email.backend.repo.MailboxRepository;
import email.backend.repo.UserRepository;
import email.backend.services.filter.AttachmentCriteria;
import email.backend.services.filter.Criteria;
import email.backend.services.filter.DateCriteria;
import email.backend.services.filter.ImportanceCriteria;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.transaction.Transactional;
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
   
   @Autowired
   private final MailRepository mailRepository;

   @Transactional
   public void addTo(Mailbox mailbox, Mail mail) {
      mailbox.getMails().add(mail);
      mailboxRepository.save(mailbox);
   }

   
   @Transactional
   public void copyTo(Mailbox mailbox, Mail mail) {
      addTo(mailbox, mail);
   }

   
   @Transactional
   public void moveTo(Mailbox from, Mailbox to, Mail mail, User user) throws IllegalArgumentException {
      if(from.getOwner().getId() == user.getId()) {
         System.out.println(to.getOwner().getId() == user.getId());
         if(to.getOwner().getId() == user.getId()) {
            
            if(from.getMails().contains(mail)) {
               deleteFrom(from, mail);
            } else {
               throw new IllegalArgumentException("mail don't even exist in the source mailbox");
            }

            if(to.getMails().contains(mail)) {
               throw new IllegalArgumentException("mail already exists in the destination mailbox");
            }
            copyTo(to, mail);
         
         } else {
            throw new IllegalArgumentException("the to mailbox don't belong to given user");
         }
      
      } else {
         throw new IllegalArgumentException("the from mailbox don't belong to given user");
      }
   }


   public void moveMailsToMailbox(Mailbox from, Mailbox to, ArrayList<Long> mailIds, User user) {
      for (Long mailId : mailIds) {
         Optional<Mail> opMail = mailRepository.findById(mailId);
         if(!opMail.isPresent()) {
            throw new IllegalArgumentException("Email don't exist");
         } else {
            Mail mail = opMail.get();
            moveTo(from, to, mail, user);
         }
      }
      userRepository.save(user);
   }

   
   @Transactional
   public void deleteFrom(Mailbox mailbox, Mail mail) {
      mailbox.getMails().remove(mail);
      mailboxRepository.save(mailbox);
   }


   public void moveToTrash(Mailbox mailbox, Mail mail, User user) throws IllegalArgumentException {
      if (mailbox.getOwner().getId() == user.getId()) {
         moveTo(mailbox, getMailbox(user, TRASH_INDEX), mail, user);
         mailboxRepository.save(mailbox);
      } else {
         throw new IllegalArgumentException("The from mailbox don't belong to given user");
      }
   }


   public void moveMailsToTrash(Mailbox mailbox, List<Long> ids, User user) throws IllegalArgumentException {
      for (Long mailId : ids) {
         Optional<Mail> opMail = mailRepository.findById(mailId);
         if(!opMail.isPresent()) {
            throw new IllegalArgumentException("Email don't exist");
         } else {
            Mail mail = opMail.get();
            moveToTrash(mailbox, mail, user);
         }
      }
   }

   
   @Transactional 
   public void deleteFromTrash(Mail mail, User user) throws IllegalArgumentException { // delete from trash only
      if(getMailbox(user, TRASH_INDEX).getMails().contains(mail)) {
         deleteFrom(getMailbox(user, TRASH_INDEX), mail); 
      } else {
         throw new IllegalArgumentException("this mail doesn't belong to the trash of the user");
      }
   }


   public Mailbox getMailbox(Long mailboxId) {
      Optional<Mailbox> mailbox = mailboxRepository.findById(mailboxId); 
      return mailbox.isPresent() ? mailbox.get() : null;
   }

   
   public Mailbox getMailbox(User user, String name) {
      return mailboxRepository.findByOwnerAndName(user, name).get();
   }


   public Mailbox getMailbox(User user, int index) throws IllegalArgumentException {
      if(user.getMailboxes().size() <= index) {
         throw new IllegalArgumentException("In getMailbox: Unable to access mail box of index" + index);
      }
      return user.getMailboxes().get(index);
   }

   
   @Transactional
   public Mailbox createMailbox(User user, String name) throws IllegalArgumentException {

      Optional<Mailbox> opMailbox = mailboxRepository.findByOwnerAndName(user, name);

      if(opMailbox.isPresent()) {
         throw new IllegalArgumentException("Mailbox already exists");
      }

      Mailbox mailbox = new Mailbox();
      mailbox.setName(name); 
      mailbox.setOwner(user);
      mailboxRepository.save(mailbox);
      
      user.getMailboxes().add(mailbox);
      userRepository.save(user);

      return mailbox;
   }


   @Transactional
   public Mailbox editMailbox(User user, MailboxDTO mailboxDto) throws IllegalArgumentException{

      Optional<Mailbox> opMailbox = mailboxRepository.findByOwnerAndName(user, mailboxDto.getName());

      if(opMailbox.isPresent()) {
         throw new IllegalArgumentException("Mailbox Name already exists");
      }

      Mailbox mailbox = getMailbox(mailboxDto.getId());
      mailbox.setName(mailboxDto.getName());

      mailboxRepository.save(mailbox);
      userRepository.save(user);

      return mailbox;
   }


   public void deleteMailbox(User user, MailboxDTO mailboxDto) throws IllegalArgumentException {
      Mailbox mailbox = getMailbox(mailboxDto.getId());
      
      if(mailbox != null) {

         while (!mailbox.getMails().isEmpty()) {

            Mail element = mailbox.getMails().get(0);

            moveToTrash(mailbox, element, user);
         }
         
         user.getMailboxes().remove(mailbox);
         
         userRepository.save(user);

      } else {
         throw new IllegalArgumentException("Mailbox doesn't exist");
      }
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
         criteria.meetCriteria(filteredMails);
      }

      return filteredMails;
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
   
         case "Subject":
            mails.sort(new Comparator<Mail>() {
               @Override
               public int compare(Mail m1, Mail m2) {
                  return m1.getSubject().compareToIgnoreCase(m2.getSubject());
               }
            });
            break;
   
         case "Sender":
            mails.sort(new Comparator<Mail>() {
               @Override
               public int compare(Mail m1, Mail m2) {
                  return m1.getSender().getEmailAddress().compareToIgnoreCase(m2.getSender().getEmailAddress());
               }
            });
            break;
   
         default:
            throw new IllegalArgumentException("Invalid sort key: " + sortKey);
      }
      return mails;
   }
   

}