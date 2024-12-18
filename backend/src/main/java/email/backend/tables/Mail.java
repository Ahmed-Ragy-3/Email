package email.backend.tables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import email.backend.services.Date;
import email.backend.services.Importance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   @Column(nullable = false)
   private String subject;

   // @NonNull(message = "Content cannot be null")
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

   // @JsonIgnore
   @OneToMany(mappedBy = "mail", fetch = FetchType.LAZY, orphanRemoval = true)
   private List<Attachment> attachments;

   @ManyToMany
   @JoinTable(
      name = "mail_receivers",
      joinColumns = @JoinColumn(name = "mail_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
   )
   private List<User> receivers;  // Added receivers list

   @Transient
   private List<Long> attachmentIds;

   // Getter for attachmentIds
   public List<Long> getAttachmentIds() {
      if (attachments == null) {
         return List.of(); // Return an empty list if attachments are not initialized
      }
      return attachments.stream().map(Attachment::getId).toList();
   }

}
