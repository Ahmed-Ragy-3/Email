package email.backend.DTO;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
   private Long id;
   private String emailAddress;
   private String name;
   private String password;
}
