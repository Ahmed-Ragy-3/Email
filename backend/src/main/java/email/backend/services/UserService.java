package email.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.databaseAccess.MailRepository;
import email.backend.databaseAccess.UserRepository;

import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
   
   @Autowired
   private final MailRepository mailRepository;
   @Autowired
   private final UserRepository userRepository;
   @Autowired
   private final MailboxRepository mailboxRepository;

   private final MailboxService mailboxService;

   public User getUser(Long userId) {
      return userRepository.findById(userId).get();
   }

   // @Transactional
   public void createUser(User user) {
      // validate 
      userRepository.save(user);
   }

   // @Transactional
   public void deleteUser(User user) {
      // delete all user categories
      // for (Mailbox mailbox : user.getMailboxes()) {
      //    mailboxService.delete(mailbox, user);
      // }
      userRepository.delete(user);
   }

}
