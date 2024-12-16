package email.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import email.backend.DTO.MailDTO;
import email.backend.controllers.EmailWebSocketController;
import email.backend.databaseAccess.ContactRepository;
import email.backend.tables.Contact;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.transaction.Transactional;

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
   private ContactRepository contactRepository;

   @Autowired
   private EmailWebSocketController socketSender;
   

   public boolean isScheduled(Mail mail) {
      return mail.getDate().future();
   }

   /**
    * @param user has user2 as a friendZone
    * @param user2 is
    * returns true if user has a friend zone mailbox containing user2
    */
   public Mailbox getFriendZone(User user, User user2) {
      // List<Mailbox> mailboxes = user.getMailboxes();

      Optional<Contact> contact = contactRepository.findByUserAndContactUser(user, user2);

      if(contact.isPresent()) {
         // for (Mailbox mailbox : mailboxes) {
         //    if(mailbox.getName().equals(contact.get().getContactName()))
         //       return mailbox;
         // }
         return mailboxService.getMailbox(user, contact.get().getContactName());
      }

      return null;
   }

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
         return handleScheduling(mail);
      }
      sendToDatabase(mail);
      sendInstantly(mail);

      return mail;
   }

   private Mail handleScheduling(Mail mail) {    // Gate 2
      
      Runnable task = () -> {
         // remove from scheduled
         sendToDatabase(mail);
         sendInstantly(mail);
      };

      taskScheduler.schedule(task, mail.getDate().toInstant());
    
      return mail;
   }

   @Transactional
   private void sendToDatabase(Mail mail) {      // Gate 3
      
      Mailbox friendMailbox;
      User sender = mail.getSender();
      
      

      for (User receiver : mail.getReceivers()) {
         friendMailbox = getFriendZone(sender, receiver);

         if(friendMailbox != null) { 
            friendMailbox.getMails().add(mail);
         } else {
            mailboxService.addTo(sender.getMailboxes().get(MailboxService.SENT_INDEX), mail);
         }

         friendMailbox = getFriendZone(receiver, sender);

         if(friendMailbox != null) {
            friendMailbox.getMails().add(mail);
         } else {
            mailboxService.addTo(receiver.getMailboxes().get(MailboxService.INBOX_INDEX), mail);
         }
      }
   }

   private void sendInstantly(Mail mail) {          // Gate 4
      // send email by websockets
      MailDTO mailDto = new MailDTO(mail);
      try {
         socketSender.sendEmail(mailDto, mailDto.getSenderAddress());
      } catch(Exception e) {
         System.out.println("Error in sending websocket message");
         System.out.println(e.getMessage());
      }
   }
}
