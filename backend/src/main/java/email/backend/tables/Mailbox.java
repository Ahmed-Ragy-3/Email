package email.backend.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Mailbox {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(nullable = false)
   private String name;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @ManyToMany
   @JoinTable(
      name = "mail_mailbox",
      joinColumns = @JoinColumn(name = "mailbox_id"),
      inverseJoinColumns = @JoinColumn(name = "mail_id")
   )
   private Set<Mail> mails = new HashSet<>();
}
