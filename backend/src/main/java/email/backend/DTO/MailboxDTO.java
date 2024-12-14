package email.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
public class MailboxDTO {
   private Long id;
   private String name;
   private UserDTO owner;
   private List<MailDTO> mails = new ArrayList<>();

   // @JsonIgnore
   // public void addMail(MailDTO mail){
   //    mails.add(mail);
   // }
}