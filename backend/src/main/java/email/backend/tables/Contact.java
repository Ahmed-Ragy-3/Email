package email.backend.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "contacts")
@Getter
@Setter
public class Contact {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @ManyToOne
   @JoinColumn(name = "contact_id", nullable = false)
   private User contact;

   @Column(nullable = true)
   private String contactName;
}
