package email.backend.services.abstraction;

import java.util.List;

import email.backend.DTO.ContactDTO;
import email.backend.DTO.UserDTO;
import email.backend.tables.User;

public interface IUserService {

   String encodePassword(String rawPassword);

   boolean checkPassword(String rawPassword, String storedHashedPassword);

   User login(String address, String password) throws IllegalArgumentException;

   User createNewAccount(String name, String address, String password) throws IllegalArgumentException;

   ContactDTO addContact(User user, ContactDTO contactDto);

   ContactDTO editContact(User user, ContactDTO contactDto);

   void deleteContact(User user, ContactDTO contactDto);

   List<ContactDTO> getContacts(User user);

   boolean validate(String emailAddress);

   User getUser(Long userId);

   User getUserFromAddress(String userAddress);

   User getUser(String token);

   User getUser(UserDTO userDto);

   List<User> getAllUsers();
}
