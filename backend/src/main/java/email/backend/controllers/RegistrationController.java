package email.backend.controllers;

// import java.util.ArrayList;
// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import email.backend.DTO.ContactDTO;
import email.backend.DTO.JwtUtil;
// import email.backend.DTO.MailboxDTO;
import email.backend.DTO.UserDTO;

// import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import email.backend.services.UserService;
import email.backend.tables.User;

// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/register")
public class RegistrationController {

   @Autowired
   private UserService userService;

   @PostMapping("/login")
   public ResponseEntity<String> login(@RequestBody UserDTO userdto) {
      try {
         User user = userService.login(userdto.getEmailAddress(), userdto.getPassword());
         userdto.setName(user.getName());
         String token = JwtUtil.generateToken(userdto);

         return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .header("Authorization", "Bearer " + token) // Add the token to the Authorization header
            .body("Login successful"); // Optional response body message
      
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PostMapping("/create")
   public ResponseEntity<String> createNewAccount(@RequestBody UserDTO userdto) {
      // enter name in userdto
      try {
         User user = userService.createNewAccount(userdto.getName(), userdto.getEmailAddress(), userdto.getPassword());
         String token = JwtUtil.generateToken(userdto);
         System.out.println(token);

         return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .header("Authorization", "Bearer " + token) // Add the token to the Authorization header
            .body("Account created successfully"); // Optional response body message
      
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }
}
