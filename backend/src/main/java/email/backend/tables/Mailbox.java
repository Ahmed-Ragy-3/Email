package email.backend.tables;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;

@Setter
@Getter
@Entity
public abstract class Mailbox {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(nullable = false)
   private String name;
   
   @Transient
   private List<Mailbox> mails;
   @Transient
   private User user;
}
