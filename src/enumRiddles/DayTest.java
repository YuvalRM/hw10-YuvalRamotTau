package enumRiddles;

enum Day {
	   MONDAY(1,"TUESDAY"),
	   TUESDAY(2,"WEDNESDAY"),
	   WEDNESDAY(3,"THURSDAY"),
	   THURSDAY(4,"FRIDAY"),
	   FRIDAY(5,"SATURDAY"),
	   SATURDAY(6,"SUNDAY"),
	   SUNDAY(7,"MONDAY");
	   private final int num;     // Private variable
	   private final String next;
	   Day(int num,String next){
		   this.num=num;
		   this.next=next;
	   }
	   public String next(){return next;}
	 
	   int getDayNumber() {
	      return num;
	   }
	}
	   
	public class DayTest {
	   public static void main(String[] args) {
	      for (Day day : Day.values()) {
	         System.out.printf("%s (%d), next is %s\n", day, day.getDayNumber(), day.next());
	      }
	   }
	}
