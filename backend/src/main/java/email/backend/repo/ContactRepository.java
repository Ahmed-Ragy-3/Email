package email.backend.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import email.backend.tables.Contact;
import email.backend.tables.User;

@Repository
@EnableJpaRepositories
public interface ContactRepository extends JpaRepository<Contact, Long> {
   Optional<Contact> findByUserAndContactUser(User user, User contactUser);
}