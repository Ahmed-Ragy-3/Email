package email.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import email.backend.tables.Mailbox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailboxDTO {
   private Long id;
   private String name;
   // private UserDTO owner;
   private List<MailDTO> mails = new ArrayList<>();

   public MailboxDTO (Mailbox mailbox) {
      this.id = mailbox.getId();
      this.name = mailbox.getName();
      // to be added
   }
}