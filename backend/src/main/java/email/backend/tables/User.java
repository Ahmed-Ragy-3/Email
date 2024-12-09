package email.backend.tables;

import java.util.ArrayList;
import java.util.List;

import email.backend.services.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;

@Setter
@Entity
@Getter
public class User {

   public User(long id, Address emailAddress, String name, String password, List<Mail> mails, List<Mailbox> mailboxes) {
      this.id = id;
      this.emailAddress = emailAddress;
      this.name = name;
      this.password = password;
      this.mails = mails;
      this.mailboxes = mailboxes;
   }
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   @Embedded
   private Address emailAddress;
   
   @Column(nullable = false)
   @Getter
   private String name;

   @Column(nullable = false)
   private String password;
   
   @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Mail> mails;

   @Transient
   private List<Mailbox> mailboxes = new ArrayList<>();
   
}