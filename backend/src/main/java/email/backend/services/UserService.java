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
import lombok.NoArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
// import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.Optional;

// import java.util.List;

@AllArgsConstructor
class LoginResponse {
   boolean valid;
   String message;
   User user;
}

@Service
@AllArgsConstructor
public class UserService {
   
   // @Autowired
   // private String secretKey = "your-secret-key";
   
   @Autowired
   private final UserRepository userRepository;
   // @Autowired
   // private final MailboxRepository mailboxRepository;

   // private final MailboxService mailboxService;

    // Method to extract user ID from JWT
   public long extractUserId(String secretKey, String token) {
      DecodedJWT decodedJWT = decodeToken(secretKey, token);
      return Long.parseLong(decodedJWT.getSubject());
   }

   // Method to decode and verify the token
   private DecodedJWT decodeToken(String secretKey, String token) {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);
      JWTVerifier verifier = JWT.require(algorithm)
               .build();
      return verifier.verify(token);
   }

   // Method to check token expiration
   public boolean isTokenExpired(String secretKey, String token) {
      DecodedJWT decodedJWT = decodeToken(secretKey, token);
      return decodedJWT.getExpiresAt().before(new Date());
   }

   // public Mailbox getMailbox(Long userId, String mailboxName){   
   //    return mailboxService.getMailbox(mailboxName);
   // }

   public LoginResponse login(String address, String password) {
      if(!validate(address)) {
         return new LoginResponse(false, "Invalid Email Address", null);
      }

      Optional<User> user = userRepository.findByEmailAddressAndPassword(address, password);
      if(!user.isPresent()) {
         return new LoginResponse(true, "Login Successfully", user.get());
      }else {
         return new LoginResponse(false, "Email Address and Password are not found", null);
      }
   }

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

   public boolean validate(String emailAddress) {
      // anywords@mail.com
      String[] atSplit = emailAddress.split("@");
      return atSplit.length == 2 && atSplit[1].equals("@mail.com") && 
            !atSplit[0].contains(".") && Character.isLetter(atSplit[0].charAt(0));
   }

}
