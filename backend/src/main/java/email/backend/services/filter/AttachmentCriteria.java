package email.backend.services.filter;

import email.backend.tables.Mail;
import lombok.AllArgsConstructor;
// import lombok.Setter;

import java.util.List;

import java.util.stream.Collectors;

// import org.springframework.stereotype.Service;

// @Service
@AllArgsConstructor
public class AttachmentCriteria implements Criteria {

   private boolean hasAttachment;

   public List<Mail> meetCriteria(List<Mail> mails) {
      return mails.stream()
            .filter(mail -> mail.getAttachments().isEmpty() == !hasAttachment)
            .collect(Collectors.toList());
   }
}
