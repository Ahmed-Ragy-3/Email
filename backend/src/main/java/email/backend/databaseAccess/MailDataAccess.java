package email.backend.databaseAccess;

// import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import email.backend.tables.User;
import email.backend.tables.Mail;
import jakarta.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;

// interface MailRepository extends JpaRepository<Mail, Long> {
//    // Add custom query methods here if needed
// }

public class MailDataAccess implements MailQueries {

   private JpaRepository<Mail, Long> repo;

   // User ahmed = new User();
   // ahmed.getName();
   

   @Override
   public List<Mail> getInbox(Id userId) {
      // repo.findById(null);
      throw new UnsupportedOperationException("Unimplemented method 'getInbox'");
   }

   @Override
   public List<Mail> getSent(Id userId) {
      throw new UnsupportedOperationException("Unimplemented method 'getSent'");
   }

   @Override
   public List<Mail> getDrafts(Id userId) {
      throw new UnsupportedOperationException("Unimplemented method 'getDrafts'");
   }

   @Override
   public List<Mail> getTrash(Id userId) {
      throw new UnsupportedOperationException("Unimplemented method 'getTrash'");
   }

   @Override
   public List<Mail> getCategoryMails(Id categoryId, Id userId) {
      throw new UnsupportedOperationException("Unimplemented method 'getCategoryMails'");
   }

   @Override
   public void removeMail(Mail m) {
      throw new UnsupportedOperationException("Unimplemented method 'removeMail'");
   }

   @Override
   public void addMail(Mail m) {
      throw new UnsupportedOperationException("Unimplemented method 'addMail'");
   }

   
}
