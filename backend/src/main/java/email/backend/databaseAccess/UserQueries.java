package email.backend.databaseAccess;

import java.util.List;

import email.backend.services.Address;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import jakarta.persistence.Id;

public interface UserQueries {

   List<Mailbox> getMailBoxes(Id userId);

   User getUser(Id userId); 
   User getUser(Address userAddress);
   User getUser(Address userAddress, String password);
   
   void createUser(User user);
   void removeUser(User user);

   // List<Contact> getContacts(Id userId);
   void addContact(User user1, User user2, String contactName);
   void renameContact(Id contactId);
   void removeContact(User user1, User user2);

   
}
