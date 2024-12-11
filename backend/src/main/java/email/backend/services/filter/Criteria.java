package email.backend.services.filter;

import email.backend.tables.Mail;
import java.util.List;

// filter design pattern
public interface Criteria {
   public List<Mail> meetCriteria(List<Mail> mails);
}
