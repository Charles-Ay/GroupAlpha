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

public class Pandemic_Director extends JPanel
{
	//CLASS WIDE SCOPE AREA
	private final int WIDTH = 800, HEIGHT = 700;//size of JPanel
	private final int LAG_TIME = 50; // 250 time in milliseconds between re-paints of screen
	private Timer time;//Timer class object that will fire events every LAG_TIME interval
	private final int IMG_DIM =10; //size of ball to be drawn

	//REVISION July 14 : create an array of Ball objects here in class scope
	private final int ARRAY_SIZE = 500;//default to 500
	private Ball [] people = new Ball[ARRAY_SIZE];

	//REVSION NEEDED HERE: need to use the Ball class to create two Ball objects
	// with different starting locations
	//private int x, y, offsetX, offsetY; //used to position ball on JPanel

	//constructor
	public Pandemic_Director()
	{
		//create Timer and register a listener for it.
		this.time = new Timer(LAG_TIME, new BounceListener() );

		//REVISION JULY 15
		//use a loop to populate the people with balls with random positions
		//Set the color of the first ball to RED
		people[0] = new Ball(IMG_DIM, Color.RED,WIDTH, HEIGHT);
		//now set color of remaining balls to BLUE
		Color color = Color.BLUE;//color to pass in to Ball constructor

		for(int i = 1; i < people.length; i++)
		{
			if(i%25==0)
			{
				people[i] = new Ball(IMG_DIM, Color.YELLOW,WIDTH, HEIGHT);
			}
			else
			{
			people[i] = new Ball(IMG_DIM, color, WIDTH, HEIGHT);
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
		for(int i = 0; i < people.length; i++)
		{
			//get the color
			g.setColor(people[i].getColor());
			g.fillOval(people[i].getxCoord(), people[i].getyCoord(),  people[i].getDiameter(), people[i].getDiameter());
		}
		//draw a circle shape


	}//end paintComponent over-ride


	//INNER CLASS GOES HERE

	private class BounceListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			
			for(int i = 0; i < people.length; i++)
			{
				calcPosition(people[i]);
			}

			int deltaX;//difference in pixels of the x coordinates of the two balls being compared.
			int deltaY;//difference in pixels of the y coordinates of the two balls being compared.

			//temp variables to hold the x and y coords of both balls in the pair.
			//The balls will be referred to as firstBall and secondBall
			int firstBallX,  firstBallY, secondBallX, secondBallY;

			//outer loop gets the firstBall of the pair and its coordinates.
			for(int i = 0; i < people.length -1; i++)//LCC to length-1 to avoid out of bounds
			{
				//get the x and y co-ords of  first ball of the pair
				firstBallX = people[i].getxCoord();
				firstBallY = people[i].getyCoord();

				//Inner loop gets the second ball of the pair
				//start inner loop counter at i+1 so we don't compare the first ball to itself.
				for(int j = i+1; j < people.length; j++)
				{
					secondBallX = people[j].getxCoord();
					secondBallY = people[j].getyCoord();

					//now calculate deltaX and deltaY for the pair of balls
					deltaX = firstBallX - secondBallX;
					deltaY = firstBallY - secondBallY;
					//square them to get rid of negative values, then add them and take square root of total
					// and compare it to ball diameter held in IMG_DIM
					if(Math.sqrt(deltaX *deltaX + deltaY * deltaY) <= IMG_DIM)//if true, they have touched
					{
						//REVSION HERE: not using the xFlag and yFlag anymore, so now we  adjust
						// the xIncrement and yIncrement by multiplying by -1
						people[i].setxIncrement(people[i].getxIncrement() * -1);
						people[i].setyIncrement(people[i].getyIncrement() * -1);

						//now do the secondBall
						people[j].setxIncrement(people[j].getxIncrement() * -1);
						people[j].setyIncrement(people[j].getyIncrement() * -1);

						//ALSO, to get a bit of directional change generate a new set of random values for the xIncrement
						//  and yIncrement of each ball involved in the collision and assign them.
						int firstBallnewxIncrement = (int)(Math.random()*11 - 5);
						int firstBallnewyIncrement = (int)(Math.random()*11 - 5);
						int secondBallnewxIncrement = (int)(Math.random()*11 - 5);
						int secondBallnewyIncrement = (int)(Math.random()*11 - 5);

						//this will prevent balls from "getting stuck" on the borders.
						people[i].setxIncrement(firstBallnewxIncrement);
						people[i].setyIncrement(firstBallnewyIncrement);
						people[j].setxIncrement(secondBallnewxIncrement);
						people[j].setyIncrement(secondBallnewyIncrement);

						//IN VERSION FIVE change the color of a blue ball to red.
						if(people[i].getColor().equals(Color.RED) && people[j].getColor().equals(Color.BLUE))
						{
							//change second ball to color of first ball
							people[j].setColor(people[i].getColor());
						}
						if(people[j].getColor().equals(Color.RED) && people[i].getColor().equals(Color.BLUE))
						{
							//second ball is red, so change first ball to color of second ball
							people[i].setColor(people[j].getColor());;
						}
					}//end if
				}//end inner for
			}//end outer loop

			//call repaint(), which in turn calls paintComponent()
			repaint();

		}//end method

	}//end inner class

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
		frame.add(new Pandemic_Director() );
		frame.pack();//shrinks the JFrame to the smallest size possible to conserve
		             //screen real estate. Comment it out to see its effect
		frame.setVisible(true);

	}//end main

}//end class}