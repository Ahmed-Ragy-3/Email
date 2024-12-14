package email.backend.databaseAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import email.backend.tables.User;
// import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
   // Add custom query methods here if needed
   Optional<User> findById(long id);

   Optional<User> findByName(String name);
   
   Optional<User> findByEmailAddressAndPassword(String address, String password);

   Optional<User> findByEmailAddress(String emailAddress);

}
