package email.backend.databaseAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.data.repository.query.Param;

// import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;

@EnableJpaRepositories
public interface MailboxRepository extends JpaRepository<Mailbox, Long> {
   
   // List<Mailbox> findByUser(User user);
 
   List<Mailbox> findByOwner(User owner);
   
   Optional<Mailbox> findByOwnerAndName(User owner, String mailboxName);

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
