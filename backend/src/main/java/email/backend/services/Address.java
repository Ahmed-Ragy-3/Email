package email.backend.services;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
   
   private String emailAddress;
   
   public Address() {

   }
   
   public String getEmailAddress() {
      return this.emailAddress;
   }
   
   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
   }
   
   // what to do ?
   public boolean validate() {
      // anywords@mail.com
      String[] atSplit = emailAddress.split("@");
      return atSplit.length == 2 && atSplit[1].equals("@mail.com") && 
             !atSplit[0].contains(".") && Character.isLetter(atSplit[0].charAt(0));
   }
}
