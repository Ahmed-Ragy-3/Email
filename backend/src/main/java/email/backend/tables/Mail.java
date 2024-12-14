package email.backend.tables;

import email.backend.services.Date;
import email.backend.services.Importance;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Mail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   @Column(nullable = false)
   private String subject;

   @Column(nullable = false)
   private String content;

   @ManyToOne
   @JoinColumn(name = "sender_id", nullable = false)
   private User sender;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Importance importance;

   @JsonIgnore
   @ManyToMany(mappedBy = "mails", fetch = FetchType.LAZY)
   private List<Mailbox> mailboxes;

   @Embedded
   private Date date;

   @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Attachment> attachments;

   @ManyToMany
   @JoinTable(
      name = "mail_receivers",
      joinColumns = @JoinColumn(name = "mail_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
   )
   private List<User> receivers;  // Added receivers list

}
