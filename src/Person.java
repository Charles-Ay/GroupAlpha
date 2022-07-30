import java.awt.Color;

/**
 * @author Maximus Slabon
 *
 */
public class Person {
	
	boolean isAlive;
	boolean isInfected;
	int immunityStatus;

	// will hold this persons location on screen
	int xCoordinate;
	int yCoordinate;
	
	// will hold the current statusColor of the person
	Color statusColour; // statusCode?
		// no immunity = blue
		// one shot immunity = cyan
		// two shot immunity = yellow
		// three shot immunity = magenta
		// infected that recovered = green
		// infected = red
		// dead = black
		
	// default constructor for person class
	public Person() {
		isAlive = true;
	}
	
	public void xIncrement(int i) {
		// value of i in range -5 to +5
		xCoordinate += i;
	}
	
	public void yIncrement(int i) {
		// value of i in range -5 to +5
		xCoordinate += i;
	}
}