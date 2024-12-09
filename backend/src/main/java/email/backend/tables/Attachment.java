package email.backend.tables;
import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Attachment {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   private String name;
   private Blob file;
   private long mailId;
}
