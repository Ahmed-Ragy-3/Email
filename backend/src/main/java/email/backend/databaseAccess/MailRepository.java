package email.backend.databaseAccess;

import email.backend.tables.Mail;
import email.backend.tables.User;
// import email.backend.services.Importance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface MailRepository extends JpaRepository<Mail, Long> {

   List<Mail> findById(long id);

   List<Mail> findBySender(User sender);

   // List<Mail> findByMailboxes_Id(Long mailboxId); 

   // List<Mail> findByMailboxes_User_Id(Long userId);

   // List<Mail> findByDate_DateAfter(Date date);

}
