package email.backend.services.filter;

import email.backend.tables.Mail;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SenderCriteria implements Criteria {

   private String sender;

   @Override
   public List<Mail> meetCriteria(List<Mail> mails) {
      return mails.stream()
                  .filter(mail -> mail.getSender().getEmailAddress().equalsIgnoreCase(sender))
                  .collect(Collectors.toList());
   }
}
