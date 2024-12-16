package email.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import email.backend.DTO.ContactDTO;
import email.backend.DTO.JwtUtil;
import email.backend.DTO.UserDTO;
import email.backend.databaseAccess.ContactRepository;
import email.backend.databaseAccess.UserRepository;
import email.backend.tables.Contact;
import email.backend.tables.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
   
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ContactRepository contactRepository;

   @Autowired
   private MailboxService mailboxService;

   public String encodePassword(String rawPassword) {
      return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
   }

   public boolean checkPassword(String rawPassword, String storedHashedPassword) {
      return BCrypt.checkpw(rawPassword, storedHashedPassword);
   }

   public User login(String address, String password) throws IllegalArgumentException {
      if(!validate(address)) {
         throw new IllegalArgumentException("Invalid Email Address");
      }
      Optional<User> user = userRepository.findByEmailAddress(address);
      if(user.isPresent()) {
         if (!checkPassword(password, user.get().getPassword())) {
            throw new IllegalArgumentException("Wrong Password");
         } else {
            return user.get();
         }
      } else {
         throw new IllegalArgumentException("Email Address doesn't exist");
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

      String encodedPassword = encodePassword(password);
      newUser.setPassword(encodedPassword);

      userRepository.save(newUser);

      mailboxService.createMailbox(newUser, "Inbox");
      mailboxService.createMailbox(newUser, "Drafts");
      mailboxService.createMailbox(newUser, "Sent");
      mailboxService.createMailbox(newUser, "Trash");
      mailboxService.createMailbox(newUser, "Starred");
      mailboxService.createMailbox(newUser, "Scheduled");
      mailboxService.createMailbox(newUser, "Spam");
      
      return userRepository.save(newUser);
   }

   @Transactional
   public ContactDTO addContact(User user, ContactDTO contactDto) {
      if(!validate(contactDto.getEmailAddress())) {
         throw new IllegalArgumentException("Invalid Email Address");
      }

      User contact = getUserFromAddress(contactDto.getEmailAddress());
      
      if(contactRepository.findByUserAndContactUser(user, contact).isPresent()) {
         throw new IllegalArgumentException("Contact already exists");
      }
      
      Contact newContact = contactDto.getContact(user, contact);

      if(newContact.getContactUser() != null) {

         contactRepository.save(newContact);
         user.getContacts().add(newContact);
         userRepository.save(user);

         return contactDto;
      } else {
         throw new IllegalArgumentException("Email Address doesn't exist");
      }
   }

   @Transactional
   public ContactDTO editContact(User user, ContactDTO contactDto) {

      Contact contact = contactRepository.findByUserAndContactUser(user, getUserFromAddress(contactDto.getEmailAddress())).get();
      
      contact.setContactName(contactDto.getName());

      contactRepository.save(contact);

      return contactDto;
   }

   @Transactional
   public void deleteContact(User user, ContactDTO contactDto) {
      User contact = getUserFromAddress(contactDto.getEmailAddress());

      Optional<Contact> givenContact = contactRepository.findByUserAndContactUser(user, contact);
      
      if(!givenContact.isPresent()) {
         throw new IllegalArgumentException("Contact doesn't exist");
      } else {
         user.getContacts().remove(givenContact.get());
         userRepository.save(user);
      }
   }

   public List<ContactDTO> getContacts(User user) {
      List<Contact> contacts = user.getContacts();

      List<ContactDTO> contactsDTO = new ArrayList<>();

      for (Contact contact : contacts) {
         contactsDTO.add(new ContactDTO(contact));
      }

      return contactsDTO;
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
   
   public User getUserFromAddress(String userAddress) {
      Optional<User> user = userRepository.findByEmailAddress(userAddress);
      return user.isPresent() ? user.get() : null;
   }

   public User getUser(String token) {
      System.out.println("entered get user by token");
      return getUser(JwtUtil.getUserFromToken(token));
   }
   
   public User getUser(UserDTO userDto) {
      System.out.println("entered get user by dto");
      Optional<User> user = userRepository.findById(userDto.getId());
      return user.isPresent() ? user.get() : null;
   }

   // @Transactional
   public void deleteUser(User user) {
      userRepository.delete(user);
   }
}
