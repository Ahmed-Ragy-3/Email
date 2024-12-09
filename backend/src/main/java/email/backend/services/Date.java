package email.backend.services;

import java.time.LocalDateTime;

public class Date {

   private int year;
   private int month;
   private int day;

   private int hour;
   private int minute;

   public Date(int year, int month, int day, int hour, int minute) {
      this.year = year;
      this.month = month;
      this.day = day;
      this.hour = hour;
      this.minute = minute;
   }

   public int getYear() {
      return year;
   }
   public int getMonth() {
      return month;
   }
   public int getDay() {
      return day;
   }
   public int getHour() {
      return hour;
   }
   public int getMinute() {
      return minute;
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

   public boolean isBetween(Date d1, Date d2) {
      return true;
   }

   // public boolean 
}
