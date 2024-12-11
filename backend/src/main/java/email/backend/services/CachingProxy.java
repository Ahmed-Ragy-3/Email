package email.backend.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
// import jakarta.persistence.Id;

@Service
public class CachingProxy {
   
   private HashMap<Long, User> usersCache = new HashMap<>();
   private HashMap<Long, Mailbox> mailboxCache = new HashMap<>();
   private HashMap<Long, Mail> mailCache = new HashMap<>();

   public User getUser(Long userId) {
      return usersCache.get(userId);
   }

   public Mailbox getMailbox(Long mailboxId) {
      return mailboxCache.get(mailboxId);
   }

   public Mail getMail(Long mailId) {
      return mailCache.get(mailId);
   }

   public void putUser(User user) {
      usersCache.putIfAbsent(user.getId(), user);
   }
}
