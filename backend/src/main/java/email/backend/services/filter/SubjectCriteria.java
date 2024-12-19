package email.backend.services.filter;

import email.backend.tables.Mail;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SubjectCriteria implements Criteria {

   private String keyword;

   @Override
   public List<Mail> meetCriteria(List<Mail> mails) {
      return mails.stream()
                  .filter(mail -> mail.getSubject().toLowerCase().contains(keyword.toLowerCase()))
                  .collect(Collectors.toList());
   }
}
