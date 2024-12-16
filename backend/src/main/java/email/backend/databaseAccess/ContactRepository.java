package email.backend.databaseAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import email.backend.tables.Contact;
import email.backend.tables.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
   Optional<Contact> findByUserAndContactUser(User user, User contactUser);
}