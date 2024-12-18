package email.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import email.backend.DTO.ContactDTO;
import email.backend.services.UserService;


@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

   @Autowired
   private UserService userService;

   @PutMapping("/contact/add")
   public ResponseEntity<?> addContact(
      @RequestBody ContactDTO contactDto,
      @RequestHeader("Authorization") String token) {
      
      try {
         ContactDTO newContact = userService.addContact(userService.getUser(token), contactDto);
         
         return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(newContact);
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PutMapping("/contact/edit")
   public ResponseEntity<?> editContact(
      @RequestBody ContactDTO contactDto,
      @RequestHeader("Authorization") String token) {
      
      try {
         ContactDTO contact = userService.editContact(userService.getUser(token), contactDto);
         
         return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(contact);
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @PostMapping("/contact/delete")
   public ResponseEntity<?> deleteContact(
      @RequestBody ContactDTO contactDto,
      @RequestHeader("Authorization") String token ) {
      try {
         userService.deleteContact(userService.getUser(token), contactDto);
          return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body("Contact Deleted Successfully");
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   @GetMapping("/contacts")
   public ResponseEntity<?> getContacts(@RequestHeader("Authorization") String token) {
      try {
         List<ContactDTO> contacts = userService.getContacts(userService.getUser(token));
          return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(contacts);
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      }
   }

   // @PostMapping("/delete")
   // public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token ) {
   //    try {
   //       userService.deleteUser(userService.getUser(token));
   //       return ResponseEntity
   //          .status(HttpStatus.ACCEPTED)
   //          .body("User Deleted Successfully");
   //    } catch (Exception e) {
   //       return ResponseEntity
   //          .status(HttpStatus.BAD_REQUEST)
   //          .body(e.getMessage());
   //    }
   // }
}
