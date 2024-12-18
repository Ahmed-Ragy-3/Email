package email.backend.services;

// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.util.Set;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.DTO.WebSocketMsgDTO;
import email.backend.controllers.EmailWebSocketController;
import email.backend.databaseAccess.MailRepository;
// import email.backend.databaseAccess.UserRepository;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.databaseAccess.UserRepository;
import email.backend.databaseAccess.ContactRepository;
import email.backend.tables.Contact;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
// import email.backend.tables.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Configuration
@EnableScheduling
public class MailService {

   @Autowired
   private MailRepository mailRepository;

   @Autowired
   private ContactRepository contactRepository;
   
   @Autowired
   private MailboxRepository mailboxRepository;
   
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserService userService;

   @Autowired
   private MailboxService mailboxService;
   
   @Autowired
   private EmailWebSocketController socketSender;


   /**
    * create mail and put into drafts category without any validations
    * @param Mail mail 
    */
   @Transactional
   public MailDTO createDraftMail(MailDTO mailDto, User user) {
      Mail mail = mailDto.toMail(user, userService, this);

      mail = mailRepository.save(mail);

      User sender = mail.getSender();
      
      sender.getMailboxes().get(MailboxService.DRAFTS_INDEX).getMails().add(mail);

      mailboxRepository.save(sender.getMailboxes().get(MailboxService.DRAFTS_INDEX));
      
      userRepository.save(user);
      
      mailDto.setId(mail.getId());

      return mailDto;
   }

   @Transactional
   public MailDTO editDraftMail(MailDTO mailDto, User user) throws IllegalArgumentException {

      Optional<Mail> OpMail = mailRepository.findById(mailDto.getId());

      if(OpMail.isPresent()) {
         Mail mail = mailDto.toMail(user, userService, this);
   
         mail.setId(mailDto.getId());
         
         mail = mailRepository.save(mail);
   
         User sender = mail.getSender();

         // should old mail from list of drafts ?

         mailboxRepository.save(sender.getMailboxes().get(MailboxService.DRAFTS_INDEX));

         userRepository.save(user);
   
         return mailDto;
      } else {
         throw new IllegalArgumentException("Mail doesn't exists");
      }
      
   }

   public List<MailboxDTO> getAllMails(User user) {
      List<MailboxDTO> mailboxesDto = new ArrayList<>();

      for (Mailbox mailbox : user.getMailboxes()) {
         MailboxDTO dto = new MailboxDTO();
         
         dto.setId(mailbox.getId());
         dto.setName(mailbox.getName());
         
         List<MailDTO> mailsDto = new ArrayList<>();
         for (Mail mail : mailbox.getMails()) {
            mailsDto.add(new MailDTO(mail));
         }

         dto.setMails(mailsDto);
         
         mailboxesDto.add(dto);
      }

      return mailboxesDto;
   }

   
   public Mail getMailById(Long mailId){
      return mailRepository.findById(mailId).get();
   }

   public List<Mail> getEmailsInMailbox(User user, String mailboxName) {
      Mailbox mailbox = mailboxRepository.findByOwnerAndName(user, mailboxName).get();
      return new ArrayList<Mail>(mailbox.getMails());
   }

   // we can use this to get sent mailbox
   public List<Mail> getMailsBySenderId(Long senderId) {
      return mailRepository.findBySender(userService.getUser(senderId));
   }

   @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
   public void deleteTrashMails() {
      List<User> users = userService.getAllUsers();

      for (User user : users) {
         Mailbox trash = mailboxService.getMailbox(user, MailboxService.TRASH_INDEX);
         List<Mail> trashMails = trash.getMails();

         Date today = Date.getTodaysDate();

         for (Mail mail : trashMails) {
            if(mail.getDate().getDay() == today.getDay()){
               mailboxService.delete(mail, user);
            }
            
         }
      }
      // we never actualy delete from data base
   }

   public Importance getImportanceFromString(String importance) {
      if(importance == null) return Importance.NORMAL;

      // importance = importance.toLowerCase();

      switch (importance.toLowerCase()) {
         case "urgent":
            return Importance.URGENT;
         case "important":
            return Importance.IMPORTANT;
         case "normal":
            return Importance.NORMAL;
         case "delayable":
            return Importance.DELAYABLE;
      }

      return Importance.NORMAL;
   }


   private Mailbox getFriendZone(User user, User user2) {
      // Friend zone logic
      Optional<Contact> contact = contactRepository.findByUserAndContactUser(user, user2);
      return contact.map(c -> mailboxService.getMailbox(user, c.getContactName())).orElse(null);
   }

   public void sendToDatabase(Mail mail) {      // Gate 3
      
      Mailbox friendMailbox;
      User sender = mail.getSender();
      MailDTO mailDto = new MailDTO(mail);

      boolean inSent = false;

      String receiverMailboxName = "";
      
      
      for (User receiver : mail.getReceivers()) {
         friendMailbox = getFriendZone(sender, receiver);
         
         if(friendMailbox != null) {
            friendMailbox.getMails().add(mail);
            mailboxRepository.save(friendMailbox); // /////////
         } else if (!inSent) {
            
            mailboxService.addTo(sender.getMailboxes().get(MailboxService.SENT_INDEX), mail);
            mailboxRepository.save(sender.getMailboxes().get(MailboxService.SENT_INDEX)); ////////////
            inSent = true;
         }
         
         friendMailbox = getFriendZone(receiver, sender);
         
         if(friendMailbox != null) {
            friendMailbox.getMails().add(mail);
            mailboxRepository.save(friendMailbox); //////////////
            receiverMailboxName = friendMailbox.getName();
         } else {
            mailboxService.addTo(receiver.getMailboxes().get(MailboxService.INBOX_INDEX), mail);
            mailboxRepository.save(receiver.getMailboxes().get(MailboxService.INBOX_INDEX)); //////////////
            receiverMailboxName = receiver.getMailboxes().get(MailboxService.INBOX_INDEX).getName();
         }
         
         try {
            for (String recieverAddress : mailDto.getReceiversAddresses()) {
               socketSender.sendEmail(new WebSocketMsgDTO(new MailDTO(mail), receiverMailboxName),
               recieverAddress, mailDto.getSenderAddress());
            }
         } catch(Exception e) {
            System.out.println("Error in sending websocket message");
            System.out.println(e.getMessage());
         }
      }
   }


}
