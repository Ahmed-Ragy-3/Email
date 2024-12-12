package email.backend.databaseAccess;

import email.backend.tables.Mail;
import email.backend.tables.User;
import email.backend.services.Importance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface MailRepository extends JpaRepository<Mail, Long> {
   // Find all mails sent by a specific user
   List<Mail> findBySender(User sender);

   // Find all mails with a specific subject
   List<Mail> findBySubject(String subject);

   // Find all mails by importance
   List<Mail> findByImportance(Importance importance);

   // Find all mails in a specific mailbox
   List<Mail> findByMailboxes_Id(Long mailboxId);

   // Find mails sent to a specific recipient (if recipients are stored in a relation)
   List<Mail> findByMailboxes_User_Id(Long userId);

   // Custom query example: Find mails sent after a specific date
   // List<Mail> findByDate_DateAfter(Date date);

}
