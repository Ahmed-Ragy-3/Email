package email.backend.tables;

import java.util.ArrayList;
import java.util.List;

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

   @Column(nullable = false, unique = true)
   private String emailAddress;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String password;

   @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Mailbox> mailboxes = new ArrayList<>();

   // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnore
   @OneToMany
   private List<User> contacts = new ArrayList<>();

   @JsonIgnore
   @OneToMany(mappedBy = "sender")
   private List<Mail> sentMails;

}
