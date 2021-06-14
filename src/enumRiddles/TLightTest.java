package enumRiddles;

enum TLight {
	   // Each instance provides its implementation to abstract method
	   RED(30,"GREEN"),
	   AMBER(10,"RED"),
	   GREEN(30,"AMBER");
	 
	   
	   private final int seconds;     // Private variable
	   private final String next;
	   TLight(int seconds, String next) {          // Constructor
	      this.seconds = seconds;
	      this.next=next;
	   }
	 
	   int getSeconds() {             // Getter
	      return seconds;
	   }
	   String next() {
		   return next;
	   }
	}
	   
	public class TLightTest {
	   public static void main(String[] args) {
	      for (TLight light : TLight.values()) {
	         System.out.printf("%s: %d seconds, next is %s\n", light,
	               light.getSeconds(), light.next());
	      }
	   }
	}