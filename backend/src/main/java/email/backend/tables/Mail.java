package email.backend.tables;

import email.backend.services.Date;
import email.backend.services.Importance;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
public class Mail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   private String content;

   @ManyToOne
   @JoinColumn(name = "sender_id", nullable = false)
   private User sender;

   private String subject;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Importance importance;

   @ManyToMany(mappedBy = "mails")
   private Set<Mailbox> mailboxes;// = new HashSet<>();

   // user -> list mailboxe_id
   // mailbox -> list mail_id
   // mail -> 

   @Embedded
   private Date date;

   @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Attachment> attachments;
}
