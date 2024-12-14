package email.backend.tables;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(nullable = false)
   private String emailAddress;

   // @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String password;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Mailbox> mailboxes = new HashSet<>();

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Contact> contacts = new HashSet<>();

   @OneToMany(mappedBy = "sender")
   @JsonIgnore
   private List<Mail> sentMails;

}
