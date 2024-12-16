package email.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import email.backend.services.Date;
import email.backend.services.MailService;
import email.backend.services.UserService;
import email.backend.tables.Mail;
import email.backend.tables.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

   private Long id;
   private String subject;
   private String content;
   private String senderAddress;
   private String importance;
   private String dateString;
   // private List<Long> attachmentsIds = new ArrayList<>();
   private List<String> receiversAddresses = new ArrayList<>();

   /**
    * Constructor to map Mail entity to MailDTO.
    */
   public MailDTO(Mail mail) {
      this.id = mail.getId();
      this.subject = mail.getSubject();
      this.content = mail.getContent();
      this.senderAddress = mail.getSender().getEmailAddress();
      this.importance = mail.getImportance().toString();
      this.dateString = mail.getDate().toString();
      
      if (mail.getReceivers() != null) {
         for (User user : mail.getReceivers()) {
            this.receiversAddresses.add(user.getEmailAddress());
         }
      }
      // this.attachments = mail.getAttachments();
   }

   /**
    * Converts MailDTO back to Mail entity.
    *
    * @param sender       Sender of the email
    * @param userService  User service for fetching users
    * @param mailService  Mail service for fetching importance
    * @return A new Mail object
    */
   public Mail toMail(User sender, UserService userService, MailService mailService) {
      Date date;
      if(dateString == null || dateString.equalsIgnoreCase("now") || dateString.isEmpty()) {
         date = Date.getTodaysDate();
      }else {
         date = Date.getDateFromString(dateString);
      }

      List<User> receivers = new ArrayList<>();
      for (String userAddress : this.getReceiversAddresses()) {
         receivers.add(userService.getUserFromAddress(userAddress));
      }
      System.out.println(this.content + "===========================");

      return Mail.builder()
            .content(this.content)
            .subject(this.subject)
            .sender(sender)
            .importance(mailService.getImportanceFromString(this.importance))
            .date(date)
            .receivers(receivers)
            .build();
            // .attachments(attachments)
   }
}
