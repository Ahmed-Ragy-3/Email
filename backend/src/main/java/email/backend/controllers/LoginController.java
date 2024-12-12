package email.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import email.backend.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
@RequestMapping("/login")
public class LoginController {

   @Autowired
   private UserService userService;

   @GetMapping("/enter")
   public Object enterUser(
      @RequestParam("emailAddress") String emailAddress,
      @RequestParam("password") String password
   ) {
      return userService.login(emailAddress, password);
   
   }

}
