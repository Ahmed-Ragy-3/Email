package email.backend.services.filter;

import email.backend.services.Date;
import email.backend.tables.Mail;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;
import java.util.List;

// import org.springframework.stereotype.Service;

// @Service
@AllArgsConstructor
public class DateCriteria implements Criteria {

   private Date date1;
   private Date date2;

   public List<Mail> meetCriteria(List<Mail> mails) {
         
      return mails.stream()
                     .filter(mail -> mail.getDate().isBetween(date1, date2) == true)
                     .collect(Collectors.toList());
      }
}
