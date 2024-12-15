package email.backend.services;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
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
   
   public static Date getDateFromSTring(String dateString) {
      LocalDateTime now = LocalDateTime.now();

      switch (dateString) {
         case "5-mins":
            return new Date(now.plusMinutes(5));
         case "10-mins":
            return new Date(now.plusMinutes(10));
         case "30-mins":
            return new Date(now.plusMinutes(30));
         
         case "1-hour":
            return new Date(now.plusHours(1));
         case "2-hours":
            return new Date(now.plusHours(2));
         case "3-hours":
            return new Date(now.plusHours(3));
         case "6-hours":
            return new Date(now.plusHours(6));
         case "12-hours":
            return new Date(now.plusHours(12));
         case "1-day":
            return new Date(now.plusDays(1));
         default:
            throw new IllegalArgumentException("Unsupported date string: " + dateString);
      }
   }

   public boolean future() {
      return this.compareTo(getTodaysDate()) > 0;
   }

   @Override
   public String toString() {
      return "%s / %s / %s  |  %s: %s ".formatted(day, month, year, hour, minute);
   }

   public boolean isBetween(Date date1, Date date2) {
      if(year < date1.year || year > date2.year)            return false;
      else if(month < date1.month || month > date2.month)   return false;
      else if(day < date1.day || day > date2.day)           return false;
      
      return true;
   }

   public int compareTo(Date that) {
      if (this.year < that.year)          return -1;
      else if(this.year > that.year)      return 1;
      
      else if(this.month < that.month)    return -1;
      else if(this.month > that.month)    return 1;

      else if(this.day < that.day)        return -1;
      else if(this.day > that.day)        return 1;
      
      else if(this.hour < that.hour)      return -1;
      else if(this.hour > that.hour)      return 1;
      
      else if(this.minute < that.minute)  return -1;
      else if(this.minute > that.minute)  return 1;
      
      return 0;
   }
}