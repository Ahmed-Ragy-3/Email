package email.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import email.backend.DTO.AttachedMailDTO;
import email.backend.DTO.MailDTO;
import email.backend.databaseAccess.ContactRepository;
import email.backend.databaseAccess.MailRepository;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;

@Service
public class MailSenderProxy {
   /**
    * Sending mail operation is done according to several gates
    * 
    * Gate 1: check mail is ready to be sent or not (attributes validations)
    * 
    * Gate 2: check email is scheduled or not 
    * (if yes: delay sending email to Gate 3 but put mail in sender scheduled category)
    * 
    * Gate 3: put mail in database for all users 
    * (put mail in Sent category in sender or friendZone)
    * (if mail goes to one reciever: and reciever is in a friendZone)
    * (if reciever has friendZone with sender: put in it, else: put in Inbox)
    * 
    * Gate 4: send mail using websockets as a notification
   */

   @Autowired
   private TaskScheduler taskScheduler;

   @Autowired
   private MailboxService mailboxService;
   
   @Autowired
   private MailService mailService;

   @Autowired
   private MailRepository mailRepository;
   
   @Autowired
   private MailboxRepository mailboxRepository;

   @Autowired
   private UserService userService;

   @Autowired
   private ContactRepository contactRepository;

   public Mail sendMail(User user, AttachedMailDTO attachedMailDto) {

      Mail mail;
      if(attachedMailDto.getMailDto().getId() == null) {
         mail = attachedMailDto.toMail(user, userService, mailService);
      } else {
         mail = mailRepository.findById(attachedMailDto.getMailDto().getId()).get();
         user.getMailboxes().get(MailboxService.DRAFTS_INDEX).getMails().remove(mail);
         
         mailRepository.save(mail);
         
         mailboxRepository.save(user.getMailboxes().get(MailboxService.DRAFTS_INDEX));
         mail = attachedMailDto.toMail(user, userService, mailService);
      }
      
      System.out.println(mail.getId());
      mailRepository.save(mail);
      sendMail(mail);
      mailRepository.save(mail);
      return mail;
   }


   public boolean isScheduled(Mail mail) {
      return mail.getDate().future();
   }

   /**
    * @param user that have mailbox
    * @param user2 is
    * returns true if user has a friend zone mailbox containing user2
    */
   // private Mailbox getFriendZone(User user, User user2) {
   //    // Friend zone logic
   //    Optional<Contact> contact = contactRepository.findByUserAndContactUser(user, user2);
   //    return contact.map(c -> mailboxService.getMailbox(user, c.getContactName())).orElse(null);
   // }

   private boolean isReady(Mail mail) throws IllegalArgumentException {
      if(mail.getSender() == null) {
         throw new IllegalArgumentException("Invalid Sender Email Address");
      }

      int count = 1;
      for (User reciever : mail.getReceivers()) {
         if(reciever == null) {
            throw new IllegalArgumentException("Invalid reciever Email Address number: " + count);
         }
         count++;
      }

      if(mail.getContent().isEmpty() && mail.getSubject().isEmpty()) {
         throw new IllegalArgumentException("Cannot send empty body and subject");
      }
      
      return true;
   }

   public Mail sendMail(Mail mail) {          // Gate 1
      if(!isReady(mail)) {
         return null;
      }
      
      if(isScheduled(mail)) {
         // sent to scheduled folder
         mailboxService.addTo(mailboxService.getMailbox(mail.getSender(), MailboxService.SCHEDULED_INDEX), mail);

         handleScheduling(mail);
         return mail;
      }
      
      mailService.sendToDatabase(mail);
      return mail;
   }
   
   private Mail handleScheduling(Mail mail) {
      Runnable task = new Runnable() {
         @Override
         public void run() {
            try {
               Mailbox mailbox = mailboxService.getMailbox(mail.getSender(), MailboxService.SCHEDULED_INDEX);
               mailboxService.deleteFrom(mailbox, mail);
               mailboxRepository.save(mailbox);
               mailService.sendToDatabase(mail); // Delegating transactional work to MailService
               System.out.println("Scheduled task executed: " + mail.getContent());
            } catch (Exception e) {
               System.out.println("Error during scheduled mail processing: " + e.getMessage());
            }
         }
      };
      taskScheduler.schedule(task, mail.getDate().toInstant());
      
      return mail;
  }
  


}
