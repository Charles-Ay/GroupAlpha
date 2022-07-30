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
	
	int xIncrement;
	int yIncrement;
	
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
	
	public Person(int widthValue, int heightValue, Color c) {
		isAlive = true;	
		statusColour = c;
		
		boolean loopflag = true;
		int randomX, randomY;
		
		while(loopflag)
		{
			//generate a random value using widthValue
			randomX = (int)(Math.random() * widthValue);
			if(randomX >= 0 && randomX <= widthValue)
			{
				//we have a valid x value, assign it to xCoord
				this.xCoordinate = randomX;
				//System.out.println("STUB:Valid random xCoord value of " + randomX);
				loopflag = false;
			}
		}//end while
		loopflag = true;
		
		while(loopflag)
		{
			//repeat for yCoord
			randomY = (int)(Math.random() * heightValue);
			if(randomY >= 0 && randomY <= heightValue)
			{
				//we have a valid y value, assign it to yCoord
				this.yCoordinate = randomY;
				//System.out.println("STUB:Valid random yCoord value of " + randomY);
			  loopflag = false;
			}
		}//end while
		loopflag = true;
		
		this.changeIncrement();
	}
	
	public void increment() {
		this.xCoordinate += this.xIncrement;
		this.yCoordinate += this.yIncrement;
	}
	
	public void changeIncrement() {
		boolean loopflag = true;
		while(loopflag)
		{
			this.xIncrement = (int)(Math.random()*12 - 6);
			this.yIncrement = (int)(Math.random()*12 - 6);		
			if(this.xIncrement ==0 && this.xIncrement ==0)
			{
			  //run it again
				this.xIncrement = (int)(Math.random()*12 - 6);
				this.yIncrement = (int)(Math.random()*12 - 6);
			}
			else
			{
				loopflag = false;
			}
		}//end loop
	}
	
//	public void xIncrement(int i) {
//		// value of i in range -5 to +5
//		xCoordinate += i;
//	}
//	
//	public void yIncrement(int i) {
//		// value of i in range -5 to +5
//		xCoordinate += i;
//	}
}