package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import email.backend.databaseAccess.UserRepository;
import email.backend.services.UserService;
// import email.backend.services.UserService;
import email.backend.tables.User;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

   @Autowired
   UserService userService;
   
   @PutMapping("/create")
   public void createUser(@RequestBody User user) {
      userService.createUser(user);
   }

   @PutMapping("/edit/{userId}")
   public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
      // Get the logged-in user's username
      // String username = SecurityContextHolder.getContext().getAuthentication().getName();
      
      // Check if the user exists in the database
      // User existingUser = userRepository.findById(userId)
      //          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

      // Ensure the logged-in user is authorized to edit this user (optional, add your logic)
      // if (!existingUser.getName().equals(username)) {
      //    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this user.");
      // }

      // Update the necessary fields
      // existingUser.setName(updatedUser.getName());
      // existingUser.setEmailAddress(updatedUser.getEmailAddress());
      // Update other fields as needed, but keep unchanged fields intact

      // Save the updated user
      // userRepository.save(existingUser);

      return ResponseEntity.ok("User updated successfully");
   }

   
   @DeleteMapping("/delete/{userId}")
   public void deleteUser(@PathVariable Long userId) {
      userService.deleteUser(userService.getUser(userId));
   }

}
