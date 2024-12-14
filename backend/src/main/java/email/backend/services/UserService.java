package email.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import email.backend.databaseAccess.MailboxRepository;
import email.backend.DTO.ContactDTO;
import email.backend.databaseAccess.MailRepository;
import email.backend.databaseAccess.UserRepository;

import email.backend.tables.User;

import lombok.AllArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.List;

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
   @Autowired
   private final MailboxRepository mailboxRepository;
   @Autowired
   private final MailRepository mailRepository;

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

   public User login (String address, String password) throws IllegalArgumentException {
      if(!validate(address)) {
         // return new LoginResponse(false, "Invalid Email Address", null);
         throw new IllegalArgumentException("Invalid Email Address");
      }
      
      // Optional<User> user = userRepository.findByEmailAddress(address);
      // Optional<User> user = userRepository.findByPassword(password);
      Optional<User> user = userRepository.findByEmailAddressAndPassword(address, password);
      // System.out.println(user.isPresent());
      if(user.isPresent()) {
         return user.get();
         // return new LoginResponse(true, "Login Successfully", user.get());
      }else {
         throw new IllegalArgumentException("Email Address and Password are not found");
         // return new LoginResponse(false, "Email Address and Password are not found", null);
      }
   }

   public User createNewAccount(String name, String address, String password) throws IllegalArgumentException {
      if(!validate(address)) {
         throw new IllegalArgumentException("Invalid Email Address");
      }
      
      if(userRepository.findByEmailAddress(address).isPresent()) {
         throw new IllegalArgumentException("Email address already exists");
      }
      
      User newUser = new User();
      newUser.setName(name);
      newUser.setEmailAddress(address);
      newUser.setPassword(password);

      userRepository.save(newUser);

      return newUser;
   }

   public User addContact(User user, User contact) {
      // User givenUser = userRepository.findById(user.getId()).get();
      user.getContacts().add(contact);
      return userRepository.save(user);
      // User givenUser = userRepository.findById(user.getId()).get();
      // givenUser.getContacts().add(contact);
      // return userRepository.save(givenUser);
   }

   public List<ContactDTO> getContacts(User user) {
      List<User> contacts = user.getContacts();
      List<ContactDTO> contactsDTO = new ArrayList<>();
      for (User contact : contacts) {
         ContactDTO contactdto = new ContactDTO(contact.getEmailAddress(), contact.getName());
         contactsDTO.add(contactdto);
      }
      return contactsDTO;
   }

   public boolean isContact(User user1, User user2) {
      return true;
   }

   public boolean validate(String emailAddress) {
      // anywords@mail.com
      String[] atSplit = emailAddress.split("@");
      return atSplit.length == 2 && atSplit[1].equals("mail.com") && 
            !atSplit[0].contains(".") && Character.isLetter(atSplit[0].charAt(0));
   }

   public User getUser(Long userId) {
      Optional<User> user = userRepository.findById(userId);
      return user.isPresent() ? user.get() : null;
   }

   // @Transactional
   public void createUser(User user) {
      // validate 
      userRepository.save(user);
   }

   // // @Transactional
   // public void deleteUser(User user) {
   //    // delete all user categories
   //    // for (Mailbox mailbox : user.getMailboxes()) {
   //    //    mailboxService.delete(mailbox, user);
   //    // }
   //    userRepository.delete(user);
   // }
}
