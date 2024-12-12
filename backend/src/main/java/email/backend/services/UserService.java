package email.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import email.backend.databaseAccess.MailboxRepository;
// import email.backend.databaseAccess.MailRepository;
import email.backend.databaseAccess.UserRepository;

// import email.backend.tables.Mail;
// import email.backend.tables.Mailbox;
import email.backend.tables.User;

// import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
// import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

// import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

   private String secretKey = "your-secret-key";  // Use a more secure secret key in production

    // Method to extract user ID from JWT
   public long extractUserId(String token) {
      DecodedJWT decodedJWT = decodeToken(token);
      return Long.parseLong(decodedJWT.getSubject());
   }

   // Method to decode and verify the token
   private DecodedJWT decodeToken(String token) {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);
      JWTVerifier verifier = JWT.require(algorithm)
               .build();
      return verifier.verify(token);
   }

   // Method to check token expiration
   public boolean isTokenExpired(String token) {
      DecodedJWT decodedJWT = decodeToken(token);
      return decodedJWT.getExpiresAt().before(new Date());
   }

   // @Autowired
   // private final MailRepository mailRepository;
   @Autowired
   private final UserRepository userRepository;
   // @Autowired
   // private final MailboxRepository mailboxRepository;

   // private final MailboxService mailboxService;

   // public Mailbox getMailbox(Long userId, String mailboxName){   
   //    return mailboxService.getMailbox(mailboxName);
   // }

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
