package email.backend.databaseAccess;

import jakarta.persistence.Id;
import java.util.List;
import email.backend.tables.Mail;

public interface MailboxQueries {
   
   void createMailbox(Id userId, String mailboxName);

   // return categories names
   List<String> getUserCategories(Id userId);

   // List<Mail> getCategoryMails(Id categoryId);

   List<Mail> getMailboxMails(Id userId, String mailboxName);

   List<Mail> getInbox(Id userId);
   List<Mail> getSent(Id userId);
   List<Mail> getDrafts(Id userId);
   List<Mail> getTrash(Id userId);
   List<Mail> getScheduledMails(Id userId);

   void deleteMailbox(Id mailboxId);

   // void changeMailboxName(Id mailboxId, String newMailboxName);
   void changeMailboxName(Id userId,String oldMailboxName, String newMailboxName);

   void copyToMailbox(Id mailboxId, Id mailId);
   void moveToMailbox(Id mailboxId, Id mailId);
   
   void copyToStarred(Id mailId);
   // void moveToStarred(Id mailId);
   
   void moveToTrash(Id mailId);

   void moveToSent(Id mailId);

   void removeFromMailbox(Id mailboxId, Id mailId);

   void removeFromTrash(Id mailId);
}
