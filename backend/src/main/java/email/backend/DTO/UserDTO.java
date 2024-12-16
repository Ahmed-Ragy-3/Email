package email.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
   private Long id;
   private String emailAddress;
   private String name;
   private String password;

   private List<ContactDTO> contacts = new ArrayList<>();
}
