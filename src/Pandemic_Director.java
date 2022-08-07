import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.Timer;


//import Person.Health;
//import Person.Immunity;

public class Pandemic_Director extends JPanel
{
	//CLASS WIDE SCOPE AREA
	private final int WIDTH = 800, HEIGHT = 700;//make size of screen
	private final int LAG_TIME = 50; // 250 time in milliseconds between re-paints of screen
	private Timer time;//Timer class object that will fire events every LAG_TIME interval
	private final int IMG_DIM = 10; //size of ball to be drawn

	//REVISION July 14 : create an array of Ball objects here in class scope
	private final int ARRAY_SIZE = 501; //get input || Last person is infected
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
	
	JPanel simulation;

	JSpinner spinUnvaccinated;
	JSpinner spinShot1;
	JSpinner spinShot2;
	JSpinner spinShot3;
	JSpinner spinNatural;
	
	int unvacAmt;
	int shot1Amt;
	int shot2Amt;
	int shot3Amt;
	int naturalImmuAmt;
	
	JLabel infectedLbl, nonVaccinatedLbl, oneShotLbl, twoShotsLbl, threeShotsLbl, naturalReinfectedLbl, recoveredLbl, diedLbl;
	
	
	//constructor
	public Pandemic_Director()
	{
		//create Timer and register a listener for it.
		this.time = new Timer(LAG_TIME, new BounceListener() );
		this.setLayout(new BorderLayout());
		//template for making new people
		//use the ARRAY_SIZE and percentages gathered from input to calculate amount of people in each category
		//create amount of people using "cutoff" that is (ARRAY_SIZE/percentage + last cutoff value)
		int tempPercentage = 15;
		int tempCutoffInt = ARRAY_SIZE * (tempPercentage / 100);
		for (int i = 0; i < people.length; i++) {
			if (i <= tempCutoffInt) {
				people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.NO_IMMUNITY, WIDTH, HEIGHT);
			}
			else if (i <= tempCutoffInt) {
				people[i] = new Person(Person.Health.INFECTED, Person.Immunity.NO_IMMUNITY, WIDTH, HEIGHT);
			}
			else {
				people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.THREE_SHOTS, WIDTH, HEIGHT);
			}
		}

		JPanel statsDisplay = new JPanel();
		statsDisplay.setLayout(new GridLayout(8, 1));
		
		int infected = 0; 
		int nonVaccinated = 0; 
		int oneShot = 0; 
		int twoShots = 0; 
		int threeShots = 0; 
		int naturalReinfected = 0; 
		int recovered = 0; 
		int died = 0;
		
		for (int i = 0; i < people.length; i++) {
			switch (people[i].getHealthStatus()) {
				case INFECTED:
					infected++;
					switch (people[i].getImmunity()) {
						case NO_IMMUNITY:
							nonVaccinated++;
							break;
						case ONE_SHOT:
							oneShot++;
							break;
						case TWO_SHOTS:
							twoShots++;
							break;
						case THREE_SHOTS:
							threeShots++;
							break;
						case NATURAL:
							naturalReinfected++;
							break;
					}
					break;
				case DEAD:
					died++;
					break;
				case UNINFECTED:
					//and have been infected before
					recovered++;
					break;
			}
		}
		
		infectedLbl = new JLabel("Number of Infected People Total: " + infected);
		nonVaccinatedLbl = new JLabel("Number of Infected People (Not Vaccinated): " + nonVaccinated);
		oneShotLbl = new JLabel("Number of Infected People (1 Shot): " + oneShot);
		twoShotsLbl = new JLabel("Number of Infected People (2 Shots): " + twoShots);
		threeShotsLbl = new JLabel("Number of Infected People (3 Shots): " + threeShots);
		naturalReinfectedLbl = new JLabel("Number of Re-Infected People: " + naturalReinfected);
		recoveredLbl = new JLabel("Number of People that have Recovered: " + recovered);
		diedLbl = new JLabel("Number of Dead People: " + died);
		
		
		
		statsDisplay.add(infectedLbl);
		statsDisplay.add(nonVaccinatedLbl);
		statsDisplay.add(oneShotLbl);
		statsDisplay.add(twoShotsLbl);
		statsDisplay.add(threeShotsLbl);
		statsDisplay.add(naturalReinfectedLbl);
		statsDisplay.add(recoveredLbl);
		statsDisplay.add(diedLbl);
		
		this.add(statsDisplay, BorderLayout.WEST);
		
		// options pane
		JPanel pandemicOptionsPane = new JPanel();
		
		pandemicOptionsPane.setLayout(new BorderLayout());
		
			// will hold options for collecting user input for populations
			JPanel populationOptions = new JPanel();
			// grid layout?
			populationOptions.setLayout(new GridLayout(0,2));
			
			JLabel lblUnvacced = new JLabel("Unvaccinated Population %: ");
			spinUnvaccinated = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
			
			populationOptions.add(lblUnvacced);
			populationOptions.add(spinUnvaccinated);
			
			JLabel lblShot1 = new JLabel("First Dose Vaccinated Population %: ");
			spinShot1 = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
			
			populationOptions.add(lblShot1);
			populationOptions.add(spinShot1);
			
			JLabel lblShot2 = new JLabel("Second Dose Vaccinated Population %: ");
			spinShot2 = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
			
			populationOptions.add(lblShot2);
			populationOptions.add(spinShot2);
			
			JLabel lblShot3 = new JLabel("Third Dose Vaccinated Population %: ");
			spinShot3 = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
			
			populationOptions.add(lblShot3);
			populationOptions.add(spinShot3);
			
			JLabel lblNatural = new JLabel("Recovered with Natural Immunity Population %: ");
			spinNatural = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
			
			populationOptions.add(lblNatural);
			populationOptions.add(spinNatural);
			
			pandemicOptionsPane.add(populationOptions, BorderLayout.CENTER);
		
			// will hold buttons for starting, stopping and restarting simulation
			JPanel buttonsPane = new JPanel();
			
			// flow layout
			
			JButton start = new JButton("Start");
			JButton stop = new JButton("Stop");
			JButton restart = new JButton("Restart");
			
			stop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					time.stop();
				}
			});

			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					
					// CHECK IF PROGRAM WAS RUNNING
					
					// RESUME -> timer.Start();
					
					// ELSE MAKE NEW POPULATION
					generatePopulation();
				}
			});
			
			restart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					generatePopulation();
				}
			});
			
			buttonsPane.add(start);
			buttonsPane.add(stop);
			buttonsPane.add(restart);
			
			pandemicOptionsPane.add(buttonsPane, BorderLayout.SOUTH);
		
		this.add(pandemicOptionsPane, BorderLayout.NORTH);
		// end of options pane
		

		simulation = new JPanel() {
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
					if(people[i].getColor() == Color.RED) {
						int o = 0;
					}
					g.setColor(people[i].getColor());
					g.fillOval(people[i].getxCoord(), people[i].getyCoord(), IMG_DIM, IMG_DIM);
				}
				//draw a circle shape
			}//end paintComponent over-ride
			
		};
		simulation.setPreferredSize(new Dimension(WIDTH, HEIGHT) );
		simulation.setBackground(Color.WHITE);
		
		this.add(simulation, BorderLayout.CENTER);
		
		//this.setPreferredSize(new Dimension(WIDTH, HEIGHT) );
		//this.setBackground(Color.WHITE);

		//start the timer so that it starts creating ActionEvent baby objects.
		this.time.stop();
		
	}//end constructor

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
						
						if(people[j].getHealthStatus() == Person.Health.INFECTED||people[i].getHealthStatus() == Person.Health.INFECTED) {

							int x = 10;
						}
						people[j].infectionRNG(people[i]);
						int t = 0;
					}//end if
				}//end inner for
			}//end outer loop

			//call repaint(), which in turn calls paintComponent()
			repaint();
			
			updateStatusCounts();
		}//end method

	}//end inner class
//<<<<<<< HEAD
	
	public void generatePopulation() {
		
		try {
			//Assign user based on entered values
			unvacAmt = (people.length / 100) *  ((Integer) spinUnvaccinated.getValue());
			shot1Amt = (people.length / 100) *  ((Integer) spinShot1.getValue());
			shot2Amt = (people.length / 100) *  ((Integer) spinShot2.getValue());
			shot3Amt = (people.length / 100) *  ((Integer) spinShot3.getValue());
			naturalImmuAmt = (people.length / 100) *  ((Integer) spinNatural.getValue());
			
			int shot1Count = 0;
			int shot2Count = 0;
			int shot3Count = 0;
			int naturalImmuAmtCount = 0;
			
			int total = ((Integer) spinUnvaccinated.getValue())
					+ ((Integer) spinShot1.getValue())
					+ ((Integer) spinShot2.getValue())
					+ ((Integer) spinShot3.getValue())
					+ ((Integer) spinNatural.getValue());
			
			if(total != 100)throw new ArithmeticException();
			
			for(int i = 0; i < people.length; ++i) {
				if(i < unvacAmt) {
					people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.NO_IMMUNITY, WIDTH, HEIGHT);
				}
				else {
					if(shot1Count < shot1Amt) {
						people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.ONE_SHOT, WIDTH, HEIGHT);
						++shot1Count;
					}
					else if(shot2Count < shot2Amt) {
						people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.TWO_SHOTS, WIDTH, HEIGHT);
						++shot2Count;
					}
					else if(shot3Count < shot3Amt) {
						people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.THREE_SHOTS, WIDTH, HEIGHT);
						++shot3Count;
					}
					else if(shot3Count < shot3Amt) {
						people[i] = new Person(Person.Health.UNINFECTED, Person.Immunity.THREE_SHOTS, WIDTH, HEIGHT);
						++shot3Count;
					}
					else if(naturalImmuAmtCount  < naturalImmuAmt) {
						people[i] = new Person(Person.Health.INFECTED_RECOVERD, Person.Immunity.NO_IMMUNITY, WIDTH, HEIGHT, true);
						++naturalImmuAmtCount ;
					}
				}
			}
			
			people[ARRAY_SIZE - 1] = new Person(Person.Health.INFECTED, Person.Immunity.NO_IMMUNITY, WIDTH, HEIGHT, true);
			time.start();
		}
		//too many values
		catch(ArithmeticException ex) {
				JOptionPane.showMessageDialog(null, "PLEASE ENSURE THAT ALL FEILDS ADD UP TO 100", "ERROR: TOTAL PERCENT", JOptionPane.INFORMATION_MESSAGE);
		}
		//if user enters a letter
		catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "PLEASE ENTER VALID DATA", "ERROR: INVALID DATA", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void updateStatusCounts() {

		int infected = 0;
		int nVacc = 0;
		int one = 0;
		int two = 0;
		int three = 0;
		int reI = 0;
		int recov = 0;
		int dead = 0;
		
		// update population counts
		for(int i = 0; i < people.length; i++) {
			Person temp = people[i];
			
			switch(temp.getHealthStatus()) {
			case INFECTED:
				infected++;
				
				switch(temp.getImmunity()) {
				case NO_IMMUNITY:
					nVacc++; 
					break;
				case ONE_SHOT:
					one++; 
					break;
				case TWO_SHOTS:
					two++; 
					break;
				case THREE_SHOTS:
					three++; 
					break;
				}
				
				break;
			case INFECTED_RECOVERD:
				recov++;
				break;
			case DEAD:
				dead++;
				break;
			}
		}
		
		infectedLbl.setText(String.valueOf("Number of Infected People Total: " +infected));
		nonVaccinatedLbl.setText(String.valueOf("Number of Infected People (Not Vaccinated): " +nVacc));
		oneShotLbl.setText(String.valueOf("Number of Infected People (1 Shot): " +one));
		twoShotsLbl.setText(String.valueOf("Number of Infected People (2 Shots): " +two));
		threeShotsLbl.setText(String.valueOf("Number of Infected People (3 Shots): " +three));
		naturalReinfectedLbl.setText("Number of Re-Infected People: NOT IMPLEMENTED");
		recoveredLbl.setText(String.valueOf("Number of People that have Recovered: " +recov));
		diedLbl.setText(String.valueOf("Number of Dead People: " + dead));
	}

	public void calcPosition(Person person) {
		//checks if dead
		if (person.getHealthStatus() == Person.Health.DEAD) {
			person.setxIncrement(0);
			person.setyIncrement(0);
			return;
		}
		else person.cycle();
		
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
	}
//=======
	
//	public void calcPosition(Ball ball)
////>>>>>>> 55c38c35a5b7fa2bb2d1d4296b79f3762710e8a2
//	{
//
//		//check if near boundary. If so, then apply negative operator to the relevant increment
//		//Changed the operators to >= and <= from == to fix the "disappearing ball" problem
//		if(person.getxCoord() >= WIDTH - 10 )
//		{
//			//we are at right side, so change xIncrement to a negative
//			person.setxIncrement(person.getxIncrement() * -1);
//		}
//		if(person.getxCoord() <= 0)//changed operator to <=
//		{
//			//if true, we're at left edge, flip the flag
//			person.setxIncrement(person.getxIncrement() * -1);;
//		}
//		if(person.getyCoord() >= HEIGHT - 10 )
//		{
//			person.setyIncrement(person.getyIncrement() * -1);
//		}
//		if(person.getyCoord() <= 0)
//		{
//			//if true, we're at left edge, flip the flag
//			person.setyIncrement(person.getyIncrement() * -1);;
//		}
//		//adjust the person positions using the getters and setters
//		person.setxCoord(person.getxCoord() + person.getxIncrement());
//		person.setyCoord(person.getyCoord() + person.getyIncrement());
//
//
//	}//end calcPosition

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
		
		//frame.add(buildOptionsPane());
		frame.pack();//shrinks the JFrame to the smallest size possible to conserve
		             //screen real estate. Comment it out to see its effect
		frame.setVisible(true);

	}//end main
}//end class}