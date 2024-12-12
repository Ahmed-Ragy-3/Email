package email.backend.databaseAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;

import java.util.List;

@EnableJpaRepositories
public interface MailboxRepository extends JpaRepository<Mailbox, Long> {
   List<Mailbox> findByUser(User user);

   Mailbox findByUserAndName(User user, String mailboxName);

   // find all mails that contains a certain string
   // @Query("""
   //    SELECT m
   //    FROM Mail m
   //    LEFT JOIN m.sender s
   //    LEFT JOIN m.mailboxes mb
   //    LEFT JOIN mb.user r
   //    WHERE m.subject LIKE %:keyword%
   //       OR m.content LIKE %:keyword%
   //       OR s.emailAddress.emailAddress LIKE %:keyword%
   //       OR r.emailAddress.emailAddress LIKE %:keyword%
   // """)
   // List<Mail> searchMailsByKeyword(@Param("keyword") String keyword);
}
