package email.backend.databaseAccess;
import jakarta.persistence.Id;
import java.util.List;

import email.backend.tables.Mail;

public interface MailQueries {
   List<Mail> getInbox(Id userId);
   List<Mail> getSent(Id userId);
   List<Mail> getDrafts(Id userId);
   List<Mail> getTrash(Id userId);

   List<Mail> getCategoryMails(Id categoryId, Id userId);
   
   void removeMail(Mail m);

   void addMail(Mail m);
}
