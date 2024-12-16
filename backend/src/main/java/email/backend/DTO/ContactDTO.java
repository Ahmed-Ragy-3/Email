package email.backend.DTO;


import email.backend.tables.Contact;
import email.backend.tables.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
   private String name;
   private String emailAddress;

   public ContactDTO (Contact contact) {
      this.name = contact.getContactName();
      this.emailAddress = contact.getContactUser().getEmailAddress();
   }

   public Contact getContact(User owner, User contactt) {
      Contact contact = new Contact();
      
      contact.setContactName(name);
      contact.setUser(owner);
      contact.setContactUser(contactt);

      return contact;
   }
}