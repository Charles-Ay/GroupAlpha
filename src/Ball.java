/**
 * Program Name: Ball.java Version V5 
 * Purpose:: this is a modified class to create a Ball object that will be used in the
 *         BouncingBall application.
 *         
 * Coder: Bill Pulling for Section 02
 * Date: Jul 30, 2014, UPDATED July 5 2021
 *       UPDATED July 14, 2021:
 *        1)added a color parameter to Ball class.
 *        2) set all data members to private
 *        
 *        UPDATED July 15, 2021
 *        1) added two more data members, xIncrement to determine how far to move in x direction,
 *           and yIncrement, how far to move in y direction. Both of these values will range from 
 *           -5 to +5 and will be generated randomly.
 *        2) removed the data members xFlag and yFlag as they are now redundant.  
 *        2) added getters and setters for xIncrement and yIncrement
 *        
 *        Latest Date: Wed July 13, 2022 for Sec02/03/01/04
 */        

import java.awt.*;

public class Ball
{
	
	
	//REVISION: July 14: set all data members to private so that getters and setters will be
	// used by the Bouncing_BallV4 class
	

	private int xCoord;
	private int yCoord;
	private int diameter;
	//REVISION July 14: add a color data member
	private Color color;
	
	//flags for ball direction and their getters and setters REMOVED FOR THIS VERSION
	//private boolean xFlag; //if true, we move to the right by incrementing xCoord
	               //if false, we move left by decrementing xCoord.
	//private boolean yFlag; //if true, we move down by incrementing yCoord
	               //if false, we move up by decrementing yCoord
	
	//NOTE: for version 5 of the Ball class, add two more data members, xIncrement and yIncrement.  
	//These will hold int values in the range of -5 to +4, and will be used in the calcPosition() method
	//to adjust the direction of the ball object after a collision .
	private int xIncrement;
	private int yIncrement;
	
	//constructor
	//NOTE: in version 4, add a constructor so that the x and y starting co-ordinates are 
	//generated randomly within given lower and upper limits of the drawing area width and height
	
	//four arg constructor where x and y starting position is passed in.
	public Ball(int x, int y, int diam, Color color)
	{
		//assign parameters
		this.xCoord =x;
		this.yCoord = y;
		this.diameter = diam;
		
		//this.xFlag = true;
		//this.yFlag = true;
		//Added July 14
		this.color = color;
		//Added July 15
		//Generating the xIncrement and yIncrement...put in some code to make sure that both are not zero
		//put in some code to make sure that both are not zero, because the ball will not move
		boolean loopFlag = true;
		while(loopFlag)
		{
			this.xIncrement = (int)(Math.random()*10 - 5);//note: this only goes to +4...adjust this to get +5
			this.yIncrement = (int)(Math.random()*10 - 5);		
			if(this.xIncrement ==0 && this.xIncrement ==0)
			{
			  //run it again
				this.xIncrement = (int)(Math.random()*10 - 5);
				this.yIncrement = (int)(Math.random()*10 - 5);
			}
			else
			{
				loopFlag = false;
			}
		}//end loop
		
	}//end constructor
	
	//4 arg constructor where ball x and y are generated randomly based on the bounds of the drawing area.
	//random Value = (int)( Math.random() *(highValue + lowValue + 1) + lowValue)
	//
	public Ball(int diam, Color color, int widthValue, int heightValue)
	{
		this.diameter = diam;
		this.color = color;
		//this.xFlag = true;
		//this.yFlag = true;
		//generate random starting position values bounded by widthValue and heightValue.
		//NOTE: lowest value returned needs to be zero, so we need to generate two separate random values
		// and use the width and length values of the JPanel as the multiplier in Math.random() call.
		//ALSO, we need to make sure that the ball is completely INSIDE the right edge and ABOVE the bottom edge, so 
		// we should SUBTRACT the DIAMETER of the ball object from the randomly generated value.
		// FUrther complication is that the resulting values could go negative if a random value LESS THAN DIAMETER
		// is generated, so we need to account for this possibility. We can do a RANGE TEST on the VALUES to 
		// VALIDATE THEM. IF we get an invalid value we have to generate another one.
		int randomX, randomY;
		boolean loopflag1 = true;
		
	  //loop 
		while(loopflag1)
		{
			//generate a random value using widthValue
			randomX = (int)(Math.random() * widthValue);
			if(randomX >= 0 && randomX <= widthValue - this.diameter)
			{
				//we have a valid x value, assign it to xCoord
				this.xCoord = randomX;
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
			if(randomY >= 0 && randomY <= heightValue - this.diameter)
			{
				//we have a valid y value, assign it to yCoord
				this.yCoord = randomY;
				//System.out.println("STUB:Valid random yCoord value of " + randomY);
			  loopflag1 = false;
			}
		}//end while
		
		//Added July 15 to get the values for the increments
		boolean loopFlag = true;
		while(loopFlag)
		{
			this.xIncrement = (int)(Math.random()*11 - 5);
			this.yIncrement = (int)(Math.random()*11 - 5);		
			if(this.xIncrement ==0 && this.xIncrement ==0)
			{
			  //run it again
				this.xIncrement = (int)(Math.random()*11 - 5);
				this.yIncrement = (int)(Math.random()*11 - 5);
			}
			else
			{
				loopFlag = false;
			}
		}//end loop
						
	}//end random constructor
	
	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	//getters and setters
	public int getxCoord()
	{
		return xCoord;
	}
	public int getyCoord()
	{
		return yCoord;
	}
	public int getDiameter()
	{
		return diameter;
	}
	
	public void setxCoord(int xCoord)
	{
		this.xCoord = xCoord;
	}

	public void setyCoord(int yCoord)
	{
		this.yCoord = yCoord;
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
}
//end class