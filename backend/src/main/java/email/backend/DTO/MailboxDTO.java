package email.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MailboxDTO {
   private Long id;
   private String name;
   // private UserDTO owner;
   private List<MailDTO> mails = new ArrayList<>();

}