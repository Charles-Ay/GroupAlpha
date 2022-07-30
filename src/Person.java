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
	
	int cycleCounter = 0;
	
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
	
	public Person(Color c, int widthValue, int heightValue) {
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
	
	public void setDead() {
		this.isAlive = false;
		//colour black
		this.xIncrement = 0;
		this.yIncrement = 0;
		
	}
	
	
	
	
	
	//added temporarily
	
	public Color getColor()
	{
		return statusColour;
	}

	public void setColor(Color color)
	{
		this.statusColour = color;
	}

	//getters and setters
	public int getxCoord()
	{
		return xCoordinate;
	}
	public int getyCoord()
	{
		return yCoordinate;
	}
//	public int getDiameter()
//	{
//		return diameter;
//	}
	
	public void setxCoord(int xCoord)
	{
		this.xCoordinate = xCoord;
	}

	public void setyCoord(int yCoord)
	{
		this.yCoordinate = yCoord;
	}
  
	public int getxIncrement()
	{
		return xIncrement;
	}

	public void setxIncrement(int xIncrement)
	{
		this.xIncrement = xIncrement;
	}

	public int getyIncrement()
	{
		return yIncrement;
	}

	public void setyIncrement(int yIncrement)
	{
		this.yIncrement = yIncrement;
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