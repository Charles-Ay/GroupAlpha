import java.awt.Color;
import java.util.Random;

/**
 * @author Maximus Slabon
 *
 */
public class Person {
	Random rand = new Random();
	
	// will be true if the person had the virus before
	private boolean healthy = true;
	// will be true if the person has natural immunity
	private boolean naturalImmunity = true;
	
	// will hold this persons location on screen
	int xCoordinate;
	int yCoordinate;
	int xIncrement;
	int yIncrement;
	
	int cycleCounter = 0;
	
	// will hold the current statusColor of the person
	private Color statusColor; // statusCode?
	public Color getStatusColor() {return statusColor;}
	
	// will hold the health status of the person
	private Health healthStatus;
	public Health getHealthStatus() {return healthStatus;}
	public enum Health{
		UNINFECTED,
		INFECTED,
		INFECTED_RECOVERD,
		DEAD
	}
	
	// will hold the immunity of the person
	private Immunity immunity;
	public Immunity getImmunity() {return immunity;}

	public enum Immunity{
		NO_IMMUNITY,
		ONE_SHOT,
		TWO_SHOTS,
		THREE_SHOTS,
		NATURAL
	}
	
	public Person(Health health, Immunity immunity) {
		this(health, immunity, false);
	}
	
	public Person(Health health, Immunity immunity, boolean naturalImmunity) {
		this.healthStatus = health;
		this.immunity = immunity;
		this.naturalImmunity = naturalImmunity;
		this.updateStatusColor();
		this.changeIncrement();
		if(health == Health.INFECTED)this.healthy = false;
	}
	
	public Person(Health health, Immunity immunity, int x, int y) {
		this(health, immunity, x, y, false);
	}
	
	public Person(Health health, Immunity immunity, int x, int y, boolean naturalImmunity) {
		this.healthStatus = health;
		this.immunity = immunity;
		generateXY(x, y);
		this.naturalImmunity = naturalImmunity;
		this.updateStatusColor();
		this.changeIncrement();
		if(health == Health.INFECTED)this.healthy = false;
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
	
	private void generateXY(int widthValue, int heightValue) {
		
		int randomX, randomY;
		boolean loopflag1 = true;
		
		while(loopflag1)
		{
			//generate a random value using widthValue
			randomX = (int)(Math.random() * widthValue);
			if(randomX >= 0 && randomX <= widthValue - 10)
			{
				//we have a valid x value, assign it to xCoord
				this.xCoordinate = randomX;
				//System.out.println("STUB:Valid random xCoord value of " + randomX);
				loopflag1 = false;
			}
		}//end while
		
		//reset flag1 to true to start second loop
		loopflag1 = true;
		while(loopflag1)
		{
			//repeat for yCoord
			randomY = (int)(Math.random() * heightValue);
			if(randomY >= 0 && randomY <= heightValue - 10)
			{
				//we have a valid y value, assign it to yCoord
				this.yCoordinate = randomY;
				//System.out.println("STUB:Valid random yCoord value of " + randomY);
			  loopflag1 = false;
			}
		}//end while
	}
	
	private void updateStatusColor() {
		switch(immunity) {
			case NO_IMMUNITY:
				statusColor = Color.BLUE;
				break;
			case ONE_SHOT:
				statusColor = Color.CYAN;
				break;
			case TWO_SHOTS:
				statusColor = Color.YELLOW;
				break;
			case THREE_SHOTS:
				statusColor = Color.MAGENTA;
				break;
		}
		
		switch(healthStatus) {
			case INFECTED:
				statusColor = Color.RED;
				healthy = false;
				break;
			case INFECTED_RECOVERD:
				naturalImmunity = true;
				healthy = true;
				statusColor = Color.GREEN;
				break;
			case DEAD:
				statusColor = Color.BLACK;
				break;
			case UNINFECTED:
				healthy = true;
				break;
		}
	}
	
	//returns true if cycle is over
	public void cycle() {
		if(healthStatus == Health.INFECTED) {
			if(cycleCounter >= 150) {
				deathRNG();
				if (healthStatus != Health.DEAD) {
					healthStatus = Health.INFECTED_RECOVERD;
					naturalImmunity = true;
					cycleCounter = 0;
				}
			}
			else ++cycleCounter;
		}
		this.updateStatusColor();
	}
	
	private void deathRNG() {
		if(healthStatus != Health.DEAD) {
			// Obtain a number between [1 - 100].
			int n = rand.nextInt(100) + 1;
			
			
			//mild immunity is one shot, nautalImmunity or recovered
			if(healthStatus != Health.DEAD && ((immunity == Immunity.NO_IMMUNITY && naturalImmunity == true)||immunity == Immunity.ONE_SHOT))
				if(n <= 3)healthStatus = Health.DEAD;
			else {
				switch(immunity) {
					case NO_IMMUNITY:
						if(n <= 10)healthStatus = Health.DEAD;
						break;
					case ONE_SHOT:
						if(n <= 7)healthStatus = Health.DEAD;
						break;
					case TWO_SHOTS:
						if(n <= 3)healthStatus = Health.DEAD;
						break;
					case THREE_SHOTS:
						if(n == 1)healthStatus = Health.DEAD;
						break;
				}
			}
		}
		this.updateStatusColor();
	}

	public void infectionRNG(Person otherPerson) {
		//Check if either person is infect but not both
		//if(n <= 80)this.healthStatus = Health.INFECTED;
		if((otherPerson.healthStatus == Health.INFECTED || this.healthStatus == Health.INFECTED) && (  (!otherPerson.healthy && this.healthy) ||(otherPerson.healthy && !this.healthy))) {
			// Obtain a number between [1 - 100].
			int n = rand.nextInt(100) + 1;
			
			//other person is infected
			if(otherPerson.healthStatus == Health.INFECTED && !this.healthy) {
				if(this.immunity == Person.Immunity.NO_IMMUNITY) {
					if(n <= 80){
						this.healthStatus = Health.INFECTED;
						this.healthy = false;
					}
				}
				else if(this.immunity == Person.Immunity.ONE_SHOT || this.naturalImmunity) {
					if(this.naturalImmunity) {
						if(n <= 40) {
							this.healthStatus = Health.INFECTED;
							this.healthy = false;
						}
					}
					else if(n <= 60){
						this.healthStatus = Health.INFECTED;
						this.healthy = false;
					}
				}
				else if(this.immunity == Person.Immunity.TWO_SHOTS) {
					if(n <= 30){
						this.healthStatus = Health.INFECTED;
						this.healthy = false;
					}
				}
				else if(this.immunity == Person.Immunity.THREE_SHOTS) {
					if(n <= 10){
						this.healthStatus = Health.INFECTED;
						this.healthy = false;
					}
				}
				this.updateStatusColor();
			}
			//this person is infected
			else {
				if(otherPerson.immunity == Person.Immunity.NO_IMMUNITY) {
					if(n <= 80){
						otherPerson.healthStatus = Health.INFECTED;
						otherPerson.healthy = false;
					}
				}
				else if(otherPerson.immunity == Person.Immunity.ONE_SHOT || otherPerson.naturalImmunity) {
					if(otherPerson.naturalImmunity){
						otherPerson.healthStatus = Health.INFECTED;
						otherPerson.healthy = false;
					}
					if(n <= 60){
						otherPerson.healthStatus = Health.INFECTED;
						otherPerson.healthy = false;
					}
				}
				else if(otherPerson.immunity == Person.Immunity.TWO_SHOTS) {
					if(n <= 30){
						otherPerson.healthStatus = Health.INFECTED;
						otherPerson.healthy = false;
					}
				}
				else if(otherPerson.immunity == Person.Immunity.THREE_SHOTS) {
					if(n <= 10){
						otherPerson.healthStatus = Health.INFECTED;
						otherPerson.healthy = false;
					}
				}
				otherPerson.updateStatusColor();
			}
		}
	}
	
	//temporary keep
	public Color getColor()
	{
		return statusColor;
	}

	public void setColor(Color color)
	{
		this.statusColor = color;
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

	public boolean isHealthy()
	{
		return healthy;
	}

	public void setHealthy(boolean healthy)
	{
		this.healthy = healthy;
	}

	public boolean isNaturalImmunity()
	{
		return naturalImmunity;
	}

	public void setNaturalImmunity(boolean naturalImmunity)
	{
		this.naturalImmunity = naturalImmunity;
	}

	public int getCycleCounter()
	{
		return cycleCounter;
	}

	public void setCycleCounter(int cycleCounter)
	{
		this.cycleCounter = cycleCounter;
	}

	public void setHealthStatus(Health healthStatus)
	{
		this.healthStatus = healthStatus;
	}

	public void setImmunity(Immunity immunity)
	{
		this.immunity = immunity;
	}
}