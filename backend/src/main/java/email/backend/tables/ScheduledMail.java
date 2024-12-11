package email.backend.tables;

import email.backend.services.Importance;
import jakarta.persistence.Column;
import java.util.List;
import email.backend.services.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@Setter
@Getter
@Entity
public class ScheduledMail {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   private String content;

   @ManyToOne
   @JoinColumn(name = "sender_id", nullable = false)
   private User sender;
   
   private String subject;
   
   @Column(nullable = false)
   private Importance importance;   // normal

   @ManyToOne
   @JoinColumn(name = "mailbox_id") // Owning side
   private Mailbox mailbox;
   
   @Transient
   private Date date;

   @Transient
   private List<Attachment> attachments;

   @Transient
   private List<User> recievers;
}
