import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BouncingBall extends JPanel
{
	//CLASS WIDE SCOPE AREA
	private final int WIDTH = 800, HEIGHT = 700;//size of JPanel
	private final int LAG_TIME = 50; // 250 time in milliseconds between re-paints of screen
	private Timer time;//Timer class object that will fire events every LAG_TIME interval
	private final int IMG_DIM =10; //size of ball to be drawn
	
	//REVISION July 14 : create an array of Ball objects here in class scope
	private final int ARRAY_SIZE = 500;//default to 500
	private Ball [] ballArray = new Ball[ARRAY_SIZE];
	
	//REVSION NEEDED HERE: need to use the Ball class to create two Ball objects
	// with different starting locations 
	//private int x, y, offsetX, offsetY; //used to position ball on JPanel
	
	//constructor
	public BouncingBall()
	{
		//create Timer and register a listener for it.
		this.time = new Timer(LAG_TIME, new BounceListener() );
		
		//REVISION JULY 15
		//use a loop to populate the ballArray with balls with random positions
		//Set the color of the first ball to RED
		ballArray[0] = new Ball(IMG_DIM, Color.RED,WIDTH, HEIGHT);
		//now set color of remaining balls to BLUE
		Color color = Color.BLUE;//color to pass in to Ball constructor	
		
		for(int i = 1; i < ballArray.length; i++)
		{
			if(i%25==0)                                                                                           
			{
				ballArray[i] = new Ball(IMG_DIM, Color.YELLOW,WIDTH, HEIGHT);
			}
			else
			{
			ballArray[i] = new Ball(IMG_DIM, color, WIDTH, HEIGHT);	
			}	
		}//end for
		
		//set preferred size of panel using an ANONYMOUS Dimension object
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT) );
		this.setBackground(Color.WHITE);
		
		//start the timer so that it starts creating ActionEvent baby objects. 
		this.time.start();	
		
		
	}//end constructor
	
	//OVER-RIDE the JPanel's paintComponent() method
	public void paintComponent(Graphics g)//The Graphics object 'g' is your paint brush
	{
		//call super version of this method to "throw the bucket of paint onto the canvas"
		// and cover up any previous image. 
		//NOTE: try commenting this out to see the effect of not repainting.
		super.paintComponent(g);
		
		//set brush color
		g.setColor(Color.PINK);
		//REIVSISION HERE: need to access the Ball object's state values in the call to
		// fillOval
		//REVISION JULY 15: iterate through the loop to paint the balls onto the panel
		// and set the color using the Ball object's color value
		for(int i = 0; i < ballArray.length; i++)
		{
			//get the color
			g.setColor(ballArray[i].getColor());
			g.fillOval(ballArray[i].getxCoord(), ballArray[i].getyCoord(),  ballArray[i].getDiameter(), ballArray[i].getDiameter());
		}
		//draw a circle shape
		
		
	}//end paintComponent over-ride


	//INNER CLASS GOES HERE
	
	private class BounceListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// on each Timer event (every 20 milliseconds) re-calculate the co-ordinates
			// of where the ball shape will be drawn next. 
			
			//Simplified code...Now we just call calcPosition() for each ball object, which
			//will update their positions.
			
			//REVISION JULY 14: put this in a loop to call calcPosition() on each ball in the array
			for(int i = 0; i < ballArray.length; i++)
			{
				calcPosition(ballArray[i]);
			}
			
			//NEXT STEP for version 4: use a nested for loop to check for collisions. Eventually, put this code into its own method. 
			//It could return a boolean true if a collision occurs and xIncrement and yIncrement values
			// will be flipped from positive to negative or negative to positive, depending on their
			//current state.
			
			//Proximity detection: 
			//calculate the difference in the x and y cords ( deltaX and deltaY ) for EACH PAIR OF BALLS IN THE ARRAY.
			//Start with the first ball and compare it against every other ball in the array. Then do the second ball and check it against
			// the remaining balls in the array, and continue until every pair of balls has been checked for proximity.
			//For this we need a NESTED FOR LOOP
			
			int deltaX;//difference in pixels of the x coordinates of the two balls being compared.
			int deltaY;//difference in pixels of the y coordinates of the two balls being compared.
			
			//temp variables to hold the x and y coords of both balls in the pair.
			//The balls will be referred to as firstBall and secondBall
			int firstBallX,  firstBallY, secondBallX, secondBallY;
			
			//outer loop gets the firstBall of the pair and its coordinates.
			for(int i = 0; i < ballArray.length -1; i++)//LCC to length-1 to avoid out of bounds
			{
				//get the x and y co-ords of  first ball of the pair
				firstBallX = ballArray[i].getxCoord();
				firstBallY = ballArray[i].getyCoord();
				
				//Inner loop gets the second ball of the pair
				//start inner loop counter at i+1 so we don't compare the first ball to itself.
				for(int j = i+1; j < ballArray.length; j++)
				{
					secondBallX = ballArray[j].getxCoord();
					secondBallY = ballArray[j].getyCoord();
					
					//now calculate deltaX and deltaY for the pair of balls
					deltaX = firstBallX - secondBallX;
					deltaY = firstBallY - secondBallY;
					//square them to get rid of negative values, then add them and take square root of total
					// and compare it to ball diameter held in IMG_DIM
					if(Math.sqrt(deltaX *deltaX + deltaY * deltaY) <= IMG_DIM)//if true, they have touched
					{
						//REVSION HERE: not using the xFlag and yFlag anymore, so now we  adjust
						// the xIncrement and yIncrement by multiplying by -1
						ballArray[i].setxIncrement(ballArray[i].getxIncrement() * -1);
						ballArray[i].setyIncrement(ballArray[i].getyIncrement() * -1);
						
						//now do the secondBall
						ballArray[j].setxIncrement(ballArray[j].getxIncrement() * -1);
						ballArray[j].setyIncrement(ballArray[j].getyIncrement() * -1);
						
						//ALSO, to get a bit of directional change generate a new set of random values for the xIncrement
						//  and yIncrement of each ball involved in the collision and assign them.
						int firstBallnewxIncrement = (int)(Math.random()*11 - 5);
						int firstBallnewyIncrement = (int)(Math.random()*11 - 5);
						int secondBallnewxIncrement = (int)(Math.random()*11 - 5);
						int secondBallnewyIncrement = (int)(Math.random()*11 - 5);
						
						//this will prevent balls from "getting stuck" on the borders.
						ballArray[i].setxIncrement(firstBallnewxIncrement);
						ballArray[i].setyIncrement(firstBallnewyIncrement);
						ballArray[j].setxIncrement(secondBallnewxIncrement);
						ballArray[j].setyIncrement(secondBallnewyIncrement);
						
						
						//IN VERSION FIVE change the color of a blue ball to red.
						if(ballArray[i].getColor().equals(Color.RED) && ballArray[j].getColor().equals(Color.BLUE))
						{
							//change second ball to color of first ball
							ballArray[j].setColor(ballArray[i].getColor());
						}
						if(ballArray[j].getColor().equals(Color.RED) && ballArray[i].getColor().equals(Color.BLUE))
						{
							//second ball is red, so change first ball to color of second ball
							ballArray[i].setColor(ballArray[j].getColor());;
						}
					}//end if
				}//end inner for				
			}//end outer loop
			
			//call repaint(), which in turn calls paintComponent() 
			repaint();
			
		}//end method
		
	}//end inner class
	
	/*
	Method Name: calcPosition()
	Purpose: Calculates new position of the ball on the JPanel by incrementing by one the x and y coords
	         of the Ball object passed in. If ball is near a boundary it will change the flags as required.
	         This method is called each time the Timer object fires an ActionEvent object.
	Accepts: a Ball object
	Returns: nothing...void method. It just adjusts the x and y co-ordinates of each ball object passed to it based
	         on the settings of the boolean xFlag and yFlag direction controlling variables.
	
	UPDATED JULY 15: modified this method to use the xIncrment and yIncrement values from the
	Ball object. We can dispense with the xFlag and yFlag here and just multiply the incrementX 
	 and incrementY values by -1 if we need to reverse direction.
	
	*/
	public void calcPosition(Ball ball)
	{
		
		//check if near boundary. If so, then apply negative operator to the relevant increment
		//Changed the operators to >= and <= from == to fix the "disappearing ball" problem
		if(ball.getxCoord() >= WIDTH - ball.getDiameter() )
		{
			//we are at right side, so change xIncrement to a negative
			ball.setxIncrement(ball.getxIncrement() * -1);
		}
		if(ball.getxCoord() <= 0)//changed operator to <=
		{
			//if true, we're at left edge, flip the flag
			ball.setxIncrement(ball.getxIncrement() * -1);;
		}
		if(ball.getyCoord() >= HEIGHT - ball.getDiameter() )
		{
			ball.setyIncrement(ball.getyIncrement() * -1);
		}
		if(ball.getyCoord() <= 0)
		{
			//if true, we're at left edge, flip the flag
			ball.setyIncrement(ball.getyIncrement() * -1);;
		}
		//adjust the ball positions using the getters and setters
		ball.setxCoord(ball.getxCoord() + ball.getxIncrement());
		ball.setyCoord(ball.getyCoord() + ball.getyIncrement());
		/*
		int xTemp, yTemp;	
		
		if(ball.isxFlag())
		{
			//if the xFlag is true we will increment
			xTemp = ball.getxCoord();
			
			//REVSION JULY 15. Changed this so that the setXCoord() method makes a sub call to
			// the getter for the xIncrement value and used that value to update positions
			ball.setxCoord(xTemp + ball.getxIncrement());//use PRE-FIX MODE to make sure the increment occurs first
		}
		else
		{
			//if this is true, we're heading left
			xTemp = ball.getxCoord();
			ball.setxCoord(xTemp + ball.getxIncrement() );
		}
		
		if(ball.getxCoord() == WIDTH - ball.getDiameter() )
		{
			//if true, we've hit right edge, flip the flag
			ball.setxFlag(false);
		}
		if(ball.getxCoord() == 0)
		{
			//if true, we're at left edge, flip the flag
			ball.setxFlag(true);
		}
		
		//repeat for the y co-ordinates
		if(ball.isyFlag())
		{
			yTemp = ball.getyCoord();
			ball.setyCoord(yTemp + ball.getyIncrement());//we have not hit the bottom yet, so increment using PRE-FIX
		}
		else
		{
			//if this block is run, we're heading back to top
			yTemp = ball.getyCoord();
			ball.setyCoord(yTemp + ball.getyIncrement());
		}
		
		if(ball.getyCoord() == HEIGHT - ball.getDiameter() )
		{
			//if true, we've hit bottom, flip the flag
			ball.setyFlag(false);
		}
		if(ball.getyCoord() == 0)
		{
			//if true, we're at the top, flip the flag
			ball.setyFlag(true);
		}
		
		*/
		
	}//end calcPosition
	
	public static void main(String[] args)
	{
		// create a JFrame to hold the JPanel
		JFrame frame = new JFrame("Just Follow the Bouncing Ball");
		
		//boilerplate
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout() );//ANONYMOUS object
		frame.setSize(1200,1000);
		frame.setLocationRelativeTo(null);
		
		//set background color of contentPane
		frame.getContentPane().setBackground(Color.BLUE);
		
		//create an ANONYMOUS object of the class and add the JPanel to the JFrame
		frame.add(new BouncingBall() );
		
		frame.pack();//shrinks the JFrame to the smallest size possible to conserve
		             //screen real estate. Comment it out to see its effect
		frame.setVisible(true);		

	}//end main

}//end class}