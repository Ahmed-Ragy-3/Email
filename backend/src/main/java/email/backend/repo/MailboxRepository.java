package email.backend.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import email.backend.tables.Mailbox;
import email.backend.tables.User;

@Repository
@EnableJpaRepositories
public interface MailboxRepository extends JpaRepository<Mailbox, Long> {
   
   List<Mailbox> findByOwner(User owner);

   Optional<Mailbox> findByOwnerAndName(User owner, String mailboxName);
}
