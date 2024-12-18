package email.backend.DTO;

import java.util.List;

import email.backend.services.MailService;
import email.backend.services.UserService;
import email.backend.tables.Attachment;
import email.backend.tables.Mail;
import email.backend.tables.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttachedMailDTO {

   private MailDTO mailDto;
   private List<Attachment> attachments;

   public AttachedMailDTO(Mail mail) {
      this.mailDto = new MailDTO(mail);
      this.attachments = mail.getAttachments();
   }

   public Mail toMail(User sender, UserService userService, MailService mailService) {
      Mail mail = mailDto.toMail(sender, userService, mailService);
      mail.setAttachments(attachments);
      return mail;
   }
}
