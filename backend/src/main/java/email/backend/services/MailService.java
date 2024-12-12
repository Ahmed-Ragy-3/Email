package email.backend.services;

import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import lombok.AllArgsConstructor;
import email.backend.databaseAccess.MailRepository;
// import email.backend.databaseAccess.UserRepository;
import email.backend.databaseAccess.MailboxRepository;

import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
// import java.util.Set;

@Service
@AllArgsConstructor
public class MailService {

   private final MailRepository mailRepository;
   // private final UserRepository userRepository;
   private final MailboxRepository mailboxRepository;

   private final UserService userService;

   /**
    * Send an email from one user to multiple recipients.
    *
    * @param senderId    ID of the sender
    * @param recipientIds List of recipient IDs
    * @param subject     Subject of the email
    * @param content     Email content
    * @param importance  Importance level of the email
    * @return Sent Mail entity
    * @throws IllegalArgumentException if sender or recipients are not found
    */
   // public Mail sendEmail(Long senderId, List<Long> recipientIds, String subject, String content, Importance importance) {
   //    Optional<User> senderOptional = userRepository.findById(senderId);
   //    if (senderOptional.isEmpty()) {
   //       throw new IllegalArgumentException("Sender not found");
   //    }

   //    List<User> recipients = userRepository.findAllById(recipientIds);
   //    if (recipients.isEmpty()) {
   //       throw new IllegalArgumentException("No valid recipients found");
   //    }

   //    // Create a new Mail entity
   //    Mail mail = Mail.builder()
   //    .content(content)
   //    .sender(someUserInstance)  // assume someUserInstance is an existing User object
   //    .subject(subject)
   //    .importance(importance)
   //    .date(new Date(LocalDateTime.now()))   // assume someDateInstance is a Date object
   //    .attachments(someAttachmentList) // assume someAttachmentList is a List<Attachment>
   //    .receivers(recipients) // assume someReceiverList is a List<User>
   //    .build();

   //    // Optionally save recipient information in a transient field
   //    // mail.setRecievers(recipients);

   //    return mailRepository.save(mail);
   // }

   public boolean isReady() {
      
      return true;
   }

   public Mail getMailById(Long mailId){
      return mailRepository.findById(mailId).get();
   }

   public List<Mail> getEmailsInMailbox(User user, String mailboxName) {
      Mailbox mailbox = mailboxRepository.findByUserAndName(user, mailboxName);
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

   public void createMail(Mail mail) {
      mailRepository.save(mail);
   }

}
