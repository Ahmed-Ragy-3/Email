package email.backend.services;

// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
// import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import email.backend.DTO.MailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.databaseAccess.MailRepository;
// import email.backend.databaseAccess.UserRepository;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {

   @Autowired
   private MailRepository mailRepository;
   @Autowired
   private MailboxRepository mailboxRepository;
   @Autowired
   private MailSenderProxy mailSenderProxy;

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

   @Transactional
   public Mail sendMail(User user, MailDTO mailDto) {

      Mail mail;
      if(mailDto.getId() == null) {
         mail = mailDto.toMail(user, userService, this);
      } else {
         mail = mailRepository.findById(mailDto.getId()).get();
         user.getMailboxes().get(MailboxService.DRAFTS_INDEX).getMails().remove(mail);
      }

      mailSenderProxy.sendMail(mail);
      return mail;
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



}
