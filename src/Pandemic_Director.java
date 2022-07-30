import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.Action;
import javax.swing.JButton;
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
	private Person [] people = new Person[ARRAY_SIZE];

	//REVSION NEEDED HERE: need to use the Ball class to create two Ball objects
	// with different starting locations
	//private int x, y, offsetX, offsetY; //used to position ball on JPanel

	// will be used to track percentages of the population
	int population;
	double unvaccinated;
	double firstShot;
	double secondShot;
	double thirdShot;

	//constructor
	public Pandemic_Director()
	{
		//create Timer and register a listener for it.
		this.time = new Timer(LAG_TIME, new BounceListener() );

		//REVISION JULY 15
		//use a loop to populate the people with balls with random positions
		//Set the color of the first ball to RED
		people[0] = new Person(Color.RED,WIDTH, HEIGHT);

		for(int i = 1; i < people.length; i++)
		{
			if(i%25==0)
			{
				people[i] = new Person(Color.YELLOW,WIDTH, HEIGHT);
			}
			else
			{
			people[i] = new Person(Color.BLUE, WIDTH, HEIGHT);
			}
		}//end for

		//set preferred size of panel using an ANONYMOUS Dimension object
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT) );
		this.setBackground(Color.WHITE);

		//start the timer so that it starts creating ActionEvent baby objects.
		this.time.start();


	}//end constructor

	public JPanel buildOptionsPane() {

		JPanel Pandemic_Options_Pane = new JPanel();
		
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");

		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				time.stop();
			}
		});

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				time.start();
			}
		});
		
		Pandemic_Options_Pane.add(start);
		Pandemic_Options_Pane.add(stop);
		
		return Pandemic_Options_Pane;
	}
	
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
			g.setColor(Color.BLUE);
			g.fillOval(people[i].getxCoord(), people[i].getyCoord(), 10, 10);
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

			int deltaX;//difference in pixels of the x coordinates of the two Persons being compared.
			int deltaY;//difference in pixels of the y coordinates of the two Persons being compared.

			//temp variables to hold the x and y coords of both Persons in the pair.
			//The Persons will be referred to as firstPerson and secondPerson
			int firstPersonX,  firstPersonY, secondPersonX, secondPersonY;

			//outer loop gets the firstPerson of the pair and its coordinates.
			for(int i = 0; i < people.length -1; i++)//LCC to length-1 to avoid out of bounds
			{
				//get the x and y co-ords of  first Person of the pair
				firstPersonX = people[i].getxCoord();
				firstPersonY = people[i].getyCoord();

				//Inner loop gets the second Person of the pair
				//start inner loop counter at i+1 so we don't compare the first Person to itself.
				for(int j = i+1; j < people.length; j++)
				{
					secondPersonX = people[j].getxCoord();
					secondPersonY = people[j].getyCoord();

					//now calculate deltaX and deltaY for the pair of Persons
					deltaX = firstPersonX - secondPersonX;
					deltaY = firstPersonY - secondPersonY;
					//square them to get rid of negative values, then add them and take square root of total
					// and compare it to Person diameter held in IMG_DIM
					if(Math.sqrt(deltaX *deltaX + deltaY * deltaY) <= IMG_DIM)//if true, they have touched
					{
						//REVSION HERE: not using the xFlag and yFlag anymore, so now we  adjust
						// the xIncrement and yIncrement by multiplying by -1
						people[i].setxIncrement(people[i].getxIncrement() * -1);
						people[i].setyIncrement(people[i].getyIncrement() * -1);

						//now do the secondPerson
						people[j].setxIncrement(people[j].getxIncrement() * -1);
						people[j].setyIncrement(people[j].getyIncrement() * -1);

						//ALSO, to get a bit of directional change generate a new set of random values for the xIncrement
						//  and yIncrement of each Person involved in the collision and assign them.
						int firstPersonnewxIncrement = (int)(Math.random()*11 - 5);
						int firstPersonnewyIncrement = (int)(Math.random()*11 - 5);
						int secondPersonnewxIncrement = (int)(Math.random()*11 - 5);
						int secondPersonnewyIncrement = (int)(Math.random()*11 - 5);

						//this will prevent Persons from "getting stuck" on the borders.
						people[i].setxIncrement(firstPersonnewxIncrement);
						people[i].setyIncrement(firstPersonnewyIncrement);
						people[j].setxIncrement(secondPersonnewxIncrement);
						people[j].setyIncrement(secondPersonnewyIncrement);

						//IN VERSION FIVE change the color of a blue Person to red.
						if(people[i].getColor().equals(Color.RED) && people[j].getColor().equals(Color.BLUE))
						{
							//change second Person to color of first Person
							people[j].setColor(people[i].getColor());
						}
						if(people[j].getColor().equals(Color.RED) && people[i].getColor().equals(Color.BLUE))
						{
							//second Person is red, so change first Person to color of second Person
							people[i].setColor(people[j].getColor());;
						}
					}//end if
				}//end inner for
			}//end outer loop

			//call repaint(), which in turn calls paintComponent()
			repaint();

		}//end method

	}//end inner class
<<<<<<< HEAD

	public void calcPosition(Person person)
=======
	
	public void calcPosition(Ball ball)
>>>>>>> 55c38c35a5b7fa2bb2d1d4296b79f3762710e8a2
	{

		//check if near boundary. If so, then apply negative operator to the relevant increment
		//Changed the operators to >= and <= from == to fix the "disappearing ball" problem
		if(person.getxCoord() >= WIDTH - 10 )
		{
			//we are at right side, so change xIncrement to a negative
			person.setxIncrement(person.getxIncrement() * -1);
		}
		if(person.getxCoord() <= 0)//changed operator to <=
		{
			//if true, we're at left edge, flip the flag
			person.setxIncrement(person.getxIncrement() * -1);;
		}
		if(person.getyCoord() >= HEIGHT - 10 )
		{
			person.setyIncrement(person.getyIncrement() * -1);
		}
		if(person.getyCoord() <= 0)
		{
			//if true, we're at left edge, flip the flag
			person.setyIncrement(person.getyIncrement() * -1);;
		}
		//adjust the person positions using the getters and setters
		person.setxCoord(person.getxCoord() + person.getxIncrement());
		person.setyCoord(person.getyCoord() + person.getyIncrement());


	}//end calcPosition

	public static void main(String[] args)
	{
		// create a JFrame to hold the JPanel
		JFrame frame = new JFrame("Pandemic Simulator");

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