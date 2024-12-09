package email.backend.services;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
   
   private String emailAddress;
   
   public Address(String emailAddress) {
      this.emailAddress = emailAddress;
   }
   
   public String getEmailAddress() {
      return this.emailAddress;
   }
   
   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
   }
   
   // what to do ?
   public boolean validate() {
      return true;
   }
}
