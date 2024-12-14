package email.backend.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Mailbox {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(nullable = false)
   private String name;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "user_id",nullable = false)
   private User owner;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
      name = "mail_mailbox",
      joinColumns = @JoinColumn(name = "mailbox_id"),
      inverseJoinColumns = @JoinColumn(name = "mail_id")
   )
   private List<Mail> mails = new ArrayList<>();
}
