package email.backend.services.filter;

import email.backend.tables.Mail;
import lombok.AllArgsConstructor;
import email.backend.services.Importance;
import java.util.List;
import java.util.stream.Collectors;
// import org.springframework.stereotype.Service;

// @Service
@AllArgsConstructor
public class ImportanceCriteria implements Criteria {
   
   private Importance importance;

   public List<Mail> meetCriteria(List<Mail> mails) {
      
      return mails.stream()
                  .filter(mail -> mail.getImportance() == importance)
                  .collect(Collectors.toList());
   }
}
