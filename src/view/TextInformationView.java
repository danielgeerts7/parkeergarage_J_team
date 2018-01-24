package view;

import java.awt.*;
import javax.swing.*;

import model.Model;

/**
 * This class shows some information on the top side of the application
 * 
 * @author danielgeerts7
 * @version 24-01-2018
 */
public class TextInformationView extends JPanel {
    
	private static JLabel timeLabel, tickLabel;
	private static JLabel currentParkedCarsLabel, currentParkedPayingCarsLabel, currentParkedPassholderCarsLabel;
	private static JLabel carQueueLabel, carPassholderQueueLabel;
	
    /**
     * Constructor for objects of class TextInformationView
     */
    public TextInformationView() {
        super(new GridBagLayout());
        
        timeLabel = new JLabel("");
        tickLabel = new JLabel("");
        
    	currentParkedCarsLabel = new JLabel("");
    	currentParkedPayingCarsLabel = new JLabel("");
    	currentParkedPassholderCarsLabel = new JLabel("");
    	
        carQueueLabel = new JLabel("");
        carPassholderQueueLabel = new JLabel("");
        
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 25;
        c.ipady = 0;
        
        // add car time and tick information
        c.gridx = 0;
        c.gridy = 0;
        this.add(timeLabel, c);
        c.gridy = 1;
        this.add(tickLabel, c);
        
        // add current car amount information
        c.gridx = 1;
        c.gridy = 0;
        this.add(currentParkedCarsLabel, c);
        c.gridy = 1;
        this.add(currentParkedPayingCarsLabel, c);
        c.gridy = 2;
        this.add(currentParkedPassholderCarsLabel, c);
        
        // add car Queue information
        c.gridx = 2;
        c.gridy = 0;
        this.add(carQueueLabel, c);
        c.gridy = 1;
        this.add(carPassholderQueueLabel, c);
    }
    
    /**
     * Updates the texts on screen
     * 
     * @param model
     */
    public void updateInfo(Model model) {
    	// Update tick and time
    	timeLabel.setText("Time: " + model.getTime());
    	tickLabel.setText("Tick: " + model.getCurrentTick() + "/10.000");
    	
    	// Update current parked cars information
    	int parkedAdHocCars = model.getCurrentAdHocCarsParked();
    	int parkedPassholderCars = model.getCurrentParkingPassCarsParked();
    	currentParkedCarsLabel.setText("Total cars: " + (parkedAdHocCars + parkedPassholderCars));
    	currentParkedPayingCarsLabel.setText("Total parked paying cars: " + parkedAdHocCars);
    	currentParkedPassholderCarsLabel.setText("Total parked pass holders: " + parkedPassholderCars);
    	
    	// Update cars in queue information
    	carQueueLabel.setText("Current cars waiting: " + model.getEntranceCarQueue().carsInQueue());
    	carPassholderQueueLabel.setText("Current pass holders waiting: " + model.getEntrancePassQueue().carsInQueue());
    }
}