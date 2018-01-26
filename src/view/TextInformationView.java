package view;

import java.awt.*;
import javax.swing.*;

import model.Model;

/**
 * This class shows some information on the top side of the application
 * There are labels for the titles of the information
 * and unique labels for the real information, that is being updated by the method UpdateInfo() in this class
 * 
 * @author danielgeerts7
 * @version 26-01-2018
 */
public class TextInformationView extends JPanel {
    
	private static JLabel timeTitle, tickTitle, timeLabel, tickLabel;
	
	// Title of current parked cars
	private static JLabel currentParkedCarsTitle, currentParkedPayingCarsTitle, currentParkedPassholderCarsTitle;
	// Info label of current parked cars
	private static JLabel currentParkedCarsLabel, currentParkedPayingCarsLabel, currentParkedPassholderCarsLabel;
	
	private static JLabel carQueueTitle, carPassholderQueueTitle, carQueueLabel, carPassholderQueueLabel;
	private static JLabel earnedMoneyTitle, expectedToBeEarnedMoneyTitle, earnedMoney, expectedToBeEarnedMoney;
	
    /**
     * Constructor for objects of class TextInformationView
     */
    public TextInformationView() {
        super(new GridBagLayout());
        setBackground(Color.WHITE);
        
        // Title of the time and tick
        timeTitle = new JLabel("Time: ");
        tickTitle = new JLabel("Tick: ");
        // Real information of the time and tick
        timeLabel = new JLabel("");
        tickLabel = new JLabel("");
        
        // Titles of the current parked cars
        currentParkedCarsTitle = new JLabel("Total parked cars: ");
    	currentParkedPayingCarsTitle = new JLabel("Total parked paying cars: ");
        currentParkedPayingCarsTitle.setForeground(Color.red);
    	currentParkedPassholderCarsTitle = new JLabel("Total parked pass holders: ");
    	currentParkedPassholderCarsTitle.setForeground(Color.blue);
    	// Information of the current parked cars
    	currentParkedCarsLabel = new JLabel("");
    	currentParkedPayingCarsLabel = new JLabel("");
    	currentParkedPassholderCarsLabel = new JLabel("");
    	
    	// Car queue title
    	carQueueTitle = new JLabel("Current paying cars waiting: ");
    	carQueueTitle.setForeground(Color.red);
        carPassholderQueueTitle = new JLabel("Current pass holders waiting: ");
        carPassholderQueueTitle.setForeground(Color.blue);
        // Car queue information
        carQueueLabel = new JLabel("");
        carPassholderQueueLabel = new JLabel("");
        
        // Money title
        earnedMoneyTitle = new JLabel("Earned money by paying customers: ");
        earnedMoneyTitle.setForeground(Color.red);
        expectedToBeEarnedMoneyTitle = new JLabel("Expected money to be earned by the current parking customers: ");
        expectedToBeEarnedMoneyTitle.setForeground(Color.red);
        // Money real information
        earnedMoney = new JLabel("");
        expectedToBeEarnedMoney = new JLabel("");
        
        GridBagConstraints c = new GridBagConstraints();
        // Padding between labels
        c.ipadx = 25;
        c.ipady = 0;
        
        // add car time and tick information
        c.gridx = 0;
        c.gridy = 0;
        this.add(timeTitle, c);
        c.gridy = 1;
        this.add(tickTitle, c);
        c.gridx = 1;
        c.gridy = 0;
        this.add(timeLabel, c);
        c.gridy = 1;
        this.add(tickLabel, c);
        
        // add current car amount information
        c.gridx = 2;
        c.gridy = 0;
        this.add(currentParkedPayingCarsTitle, c);
        c.gridy = 1;
        this.add(currentParkedPassholderCarsTitle, c);
        c.gridy = 2;
        this.add(currentParkedCarsTitle, c);
        c.gridx = 3;
        c.gridy = 0;
        this.add(currentParkedPayingCarsLabel, c);
        c.gridy = 1;
        this.add(currentParkedPassholderCarsLabel, c);
        c.gridy = 2;
        this.add(currentParkedCarsLabel, c);

        
        // add car Queue information
        c.gridx = 4;
        c.gridy = 0;
        this.add(carQueueTitle, c);
        c.gridy = 1;
        this.add(carPassholderQueueTitle, c);
        c.gridx = 5;
        c.gridy = 0;
        this.add(carQueueLabel, c);
        c.gridy = 1;
        this.add(carPassholderQueueLabel, c);
        
        // add car Queue information
        c.gridx = 6;
        c.gridy = 0;
        this.add(earnedMoneyTitle, c);
        c.gridy = 1;
        this.add(expectedToBeEarnedMoneyTitle, c);
        c.gridx = 7;
        c.gridy = 0;
        this.add(earnedMoney, c);
        c.gridy = 1;
        this.add(expectedToBeEarnedMoney, c);
    }
    
    /**
     * Updates the texts on screen
     * 
     * @param model
     */
    public void updateInfo(Model model) {
    	// Update tick and time
    	timeLabel.setText(model.getTime());
    	tickLabel.setText(model.getCurrentTick() + "/10.000");
    	
    	// Update current parked cars information
    	int parkedAdHocCars = model.getCurrentAdHocCarsParked();
    	int parkedPassholderCars = model.getCurrentParkingPassCarsParked();
    	currentParkedCarsLabel.setText(Integer.toString(parkedAdHocCars + parkedPassholderCars));
    	currentParkedPayingCarsLabel.setText(Integer.toString(parkedAdHocCars));
    	currentParkedPassholderCarsLabel.setText(Integer.toString(parkedPassholderCars));
    	
    	// Update cars in queue information
    	carQueueLabel.setText(Integer.toString(model.getEntranceCarQueue().carsInQueue()));
    	carPassholderQueueLabel.setText(Integer.toString(model.getEntrancePassQueue().carsInQueue()));
    	
    	String euroSign = "\u20ac";
    	
    	// Information about the paying customers
    	// Something with price and the expected price
    	String resultEarned = String.format("%.2f",  model.getEarnedMoney());
    	earnedMoney.setText(euroSign + resultEarned + ",-");
    	String resultExpected = String.format("%.2f",  model.getExpectedMoneyToBeEarned());
    	expectedToBeEarnedMoney.setText(euroSign + resultExpected + ",-");
    }
}