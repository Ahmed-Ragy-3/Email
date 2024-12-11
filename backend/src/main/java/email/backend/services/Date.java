package email.backend.services;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Embeddable
@AllArgsConstructor
@Getter
public class Date {

   private int year;
   private int month;
   private int day;

   private int hour;
   private int minute;

   
   public Date(LocalDateTime dateTime) {
      this.year = dateTime.getYear();
      this.month = dateTime.getMonthValue();
      this.day = dateTime.getDayOfMonth();
      this.hour = dateTime.getHour();
      this.minute = dateTime.getMinute();
   }

   public static Date getTodaysDate() {
      LocalDateTime localTime = LocalDateTime.now();
      return new Date(
         localTime.getYear(), 
         localTime.getMonthValue(), 
         localTime.getDayOfMonth(), 
         localTime.getHour(), 
         localTime.getMinute() 
      );
   }

   @Override
   public String toString() {
      return "%s / %s / %s | %s: %s ".formatted(year, month, day, hour, minute);
   }

   public boolean isBetween(Date date1, Date date2) {
      if(year < date1.year || year > date2.year)            return false;
      else if(month < date1.month || month > date2.month)   return false;
      else if(day < date1.day || day > date2.day)           return false;
      
      return true;
   }

}