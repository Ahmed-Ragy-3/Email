package email.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import email.backend.DTO.AttachedMailDTO;
import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.DTO.WebSocketMsgDTO;
import email.backend.controllers.EmailWebSocketController;
import email.backend.repo.AttachmentRepository;
import email.backend.repo.ContactRepository;
import email.backend.repo.MailRepository;
import email.backend.repo.MailboxRepository;
import email.backend.repo.UserRepository;
import email.backend.tables.Attachment;
import email.backend.tables.Contact;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@AllArgsConstructor
@Configuration
@EnableScheduling
@Data
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

   @Autowired
   private AttachmentRepository attachmentRepository;

   public void moveMailsToTrash(Mailbox mailbox, List<Long> ids, User user) throws IllegalArgumentException {
      for (Long mailId : ids) {
         Mail mail = getMailById(mailId);
         if(mail == null) {
            throw new IllegalArgumentException("Email don't exist");
         } else {
            mailboxService.moveToTrash(mailbox, mail, user);
         }
      }
   }

   
   public void moveMailsToMailbox(Mailbox from, Mailbox to, List<Long> mailIds, User user) {
      for (Long mailId : mailIds) {
         Mail mail = getMailById(mailId);
         if(mail == null) {
            throw new IllegalArgumentException("Email don't exist");
         } else {
            mailboxService.moveTo(from, to, mail, user);
         }
      }
   }

   /**
    * create mail and put into drafts category without any validations
    * @param Mail mail 
    */
   @Transactional
   public AttachedMailDTO createDraftMail(AttachedMailDTO attachedMailDto, User user) {
      Mail mail = attachedMailDto.toMail(user, userService, this, attachmentRepository);

      mail = mailRepository.save(mail);

      User sender = mail.getSender();
      
      sender.getMailboxes().get(MailboxService.DRAFTS_INDEX).getMails().add(mail);

      mailboxRepository.save(sender.getMailboxes().get(MailboxService.DRAFTS_INDEX));
      
      userRepository.save(user);
      
      attachedMailDto.getMailDto().setId(mail.getId());

      return attachedMailDto;
   }


   @Transactional
   public AttachedMailDTO editDraftMail(AttachedMailDTO attachedMailDto, User user) throws IllegalArgumentException {

      Optional<Mail> OpMail = mailRepository.findById(attachedMailDto.getMailDto().getId());

      if(OpMail.isPresent()) {
         Mail mail = attachedMailDto.toMail(user, userService, this, attachmentRepository);
   
         mail.setId(attachedMailDto.getMailDto().getId());
         
         mail = mailRepository.save(mail);
   
         User sender = mail.getSender();

         mailboxRepository.save(sender.getMailboxes().get(MailboxService.DRAFTS_INDEX));

         userRepository.save(user);
   
         return attachedMailDto;
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


   public List<Mail> getMailsBySenderId(Long senderId) {
      return mailRepository.findBySender(userService.getUser(senderId));
   }


   @Scheduled(cron = "0 0 0 * * ?")
   public void deleteTrashMails() {
      List<User> users = userService.getAllUsers();

      for (User user : users) {
         Mailbox trash = mailboxService.getMailbox(user, MailboxService.TRASH_INDEX);
         List<Mail> trashMails = trash.getMails();

         Date today = Date.getTodaysDate();

         for (Mail mail : trashMails) {
            if(mail.getDate().getDay() == today.getDay()){
               mailboxService.deleteFromTrash(mail, user);
            }
            
         }
      }
   }

   public Importance getImportanceFromString(String importance) {
      if(importance == null) return Importance.NORMAL;

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
      Optional<Contact> contact = contactRepository.findByUserAndContactUser(user, user2); 
      if(contact.isPresent()) {
         for (Mailbox mailbox : user.getMailboxes()) {
            if(mailbox.getName().equalsIgnoreCase(contact.get().getContactName())) {
               return mailbox;
            }
         }
      }
      return null;
   }


   public void sendToDatabase(Mail mail) {      // Gate 3
      
      Mailbox friendMailbox;
      User sender = mail.getSender();
      MailDTO mailDto = new MailDTO(mail);
      boolean inSent = false;

      String receiverMailboxName = "";
      System.out.println(mail.getContent());
      for (User receiver : mail.getReceivers()) {
         friendMailbox = getFriendZone(sender, receiver);
         System.out.println(receiver.getEmailAddress());
         if(friendMailbox != null) {
            friendMailbox.getMails().add(mail);
            mailboxRepository.save(friendMailbox);
         
         } else if (!inSent) {
            mailboxService.addTo(sender.getMailboxes().get(MailboxService.SENT_INDEX), mail);
            mailboxRepository.save(sender.getMailboxes().get(MailboxService.SENT_INDEX));

            inSent = true;
         }
         
         friendMailbox = getFriendZone(receiver, sender);
         
         if(friendMailbox != null) {
            friendMailbox.getMails().add(mail);
            mailboxRepository.save(friendMailbox);
            receiverMailboxName = friendMailbox.getName();
         
         } else {
            mailboxService.addTo(receiver.getMailboxes().get(MailboxService.INBOX_INDEX), mail);
            mailboxRepository.save(receiver.getMailboxes().get(MailboxService.INBOX_INDEX)); //////////////
            receiverMailboxName = receiver.getMailboxes().get(MailboxService.INBOX_INDEX).getName();
         }
         
         try {
               socketSender.sendEmail(new WebSocketMsgDTO(new MailDTO(mail), receiverMailboxName),
               receiver.getEmailAddress(), mailDto.getSenderAddress());
         } catch(Exception e) {
            System.out.println("Error in sending websocket message");
            System.out.println(e.getMessage());
         }
      }
   }


   public Mail setAttachments(Mail mail, List<Attachment> attachments) {
      mail.setAttachments(attachments);
      mailRepository.save(mail);
      return mail;
   }

}
