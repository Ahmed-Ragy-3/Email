package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import email.backend.DTO.JwtUtil;
import email.backend.DTO.UserDTO;
import email.backend.tables.User;
import email.backend.services.UserService;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/register")
public class RegistrationController {

   @Autowired
   private UserService userService;

   @PostMapping("/login")
   public ResponseEntity<String> login(@RequestBody UserDTO userdto) {
      try {
         User user = userService.login(userdto.getEmailAddress(), userdto.getPassword());
         userdto.setName(user.getName());
         userdto.setId(user.getId());
         
         String token = JwtUtil.generateToken(userdto);

         return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .header("Authorization", token)
            .body("Login successful");
      
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PutMapping("/create")
   public ResponseEntity<String> createNewAccount(@RequestBody UserDTO userdto) {
      System.out.println(userdto);
      // enter name in userdto
      try {
         User user = userService.createNewAccount(userdto.getName(), userdto.getEmailAddress(), userdto.getPassword());
         System.out.println(user);
         userdto.setId(user.getId());
         String token = JwtUtil.generateToken(userdto);
         System.out.println(token);

         return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Authorization", token)
            .body("Account created successfully");
      
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }
}
