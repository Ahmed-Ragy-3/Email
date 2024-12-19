package email.backend.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import email.backend.tables.Mail;
import email.backend.tables.User;

@EnableJpaRepositories
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

   List<Mail> findById(long id);

   List<Mail> findBySender(User sender);
}
