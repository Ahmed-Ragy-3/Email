package email.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import email.backend.repo.AttachmentRepository;
import email.backend.repo.MailRepository;
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

   public Mail toMail(User sender, UserService userService, MailService mailService , AttachmentRepository attachmentRepository) {
      Mail mail = mailDto.toMail(sender, userService, mailService);
      MailRepository mailRepository = mailService.getMailRepository();
      mail.setAttachments(new ArrayList<>());
      mailRepository.save(mail);
      for (Attachment attachment : attachments) {
         attachment.setMail(mail);
         attachmentRepository.save(attachment);
         mail.getAttachments().add(attachment);
      }
      mailRepository.save(mail);
      // mail.setAttachments(attachments);
      return mail;
   }
}
