package model;

import java.awt.*;

/**
 * This is the superclass of every car that you can see on screen
 * Every color in the parking garage represents a (subclass) Car
 *
 * @version 05-02-2018
 */
public abstract class Car {

    private Location location;
    private int minutesLeft;
    private int minutesParked;
    protected int randomReserveTime;
    private boolean isPaying;
    private boolean hasToPay;
    private boolean isDoubleParked;

    /**
     * Set some variables with standard values
     */
    public Car() {
    	minutesParked = 0;
    	randomReserveTime = 0;
    	isDoubleParked = false;
    }
    
    /**
     * This function is called every tick (frame per second) of the application
     */
    public void tick() {
    	if (randomReserveTime < 1) {
    		minutesParked++;
        	minutesLeft--;
    	}
        randomReserveTime--;
    }

    /**
     * @return the current location where the car is parked
     */
    public Location getLocation() {
        return location;
    }

    /**
     * set the location where the car going to park
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the current minutes the car is going to be parked of the parking spot
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * set minutes the car is going to be parked on the parking spot
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
    /**
     * @return the current minutes that the car has left before parking on the reserved parking spot
     */
    public int getReserveTime() {
    	return randomReserveTime;
    }
    
    /**
     * set the reserved time for the reserved customers 
     */
    public void setReserveTime(int randomReserveTime) {
    	this.randomReserveTime = randomReserveTime;
    }
    
    /**
     * @return if the customer is paying or not
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * Set if the customer is paying at this moment
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * @return if the customer has to pay or not
     */
    public boolean getHasToPay() {
        return hasToPay;
    }

    /**
     * Set if the customer has to pay or not
     */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }
    
    /**
     * @return current minutes this customer has parked
     */
    public int getMinutesParked() {
        return minutesParked;
    }
    
    /**
     * @return if this customer is double parked or not
     */
    public boolean isDoubleParked() {
    	return this.isDoubleParked;
    }
    
    /**
     * Set if the customer is double parked or not 
     */
    public void setDoubleParked(boolean isDoubleParked) {
    	this.isDoubleParked = isDoubleParked;
    }
    
    /**
     * Return color of the (subclass) Car
     */
    public abstract Color getColor();
}