// define package name
package model;

//import java classes
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

// import own classes
import controller.CarQueue;
import view.CarParkView;
import view.MainView;

/**   
 * explain here what this class does
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public class Model {
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute
    
    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour
    
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Car[][][] cars;
    
    // Create the JFrame
    private JFrame frame;
    private String ApplicationTitle = "The J-Team";
    
    // All views that are used in this application
    private ArrayList<MainView> allViews;
    private CarParkView carParkView;
    
    public Model(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
    	entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        
        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        
        // Create JFrame with title name
        frame = new JFrame(ApplicationTitle);
        
        allViews = new ArrayList<MainView>();
        carParkView = new CarParkView(this, numberOfFloors, numberOfRows, numberOfPlaces);
        
        allViews.add(carParkView);
        
        for (MainView view : allViews) {
        	frame.add(view);
	    }
    }
    
    /**
     *	Update every view that this application has
     */
    public void updateViews() {
	    for (MainView view : allViews) {
	    	view.updateView();
	    }
    }
	
	public int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }
	

	public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getNumberOfOpenSpots(){
    	return numberOfOpenSpots;
    }
    
    public CarQueue getEntranceCarQueue() {
    	return entranceCarQueue;
    }
    
    public CarQueue getEntrancePassQueue() {
    	return entrancePassQueue;
    }
    
    public CarQueue getPaymentCarQueue() {
    	return paymentCarQueue;
    }
    
    public CarQueue getExitCarQueue() {
    	return exitCarQueue;
    }
    
    public int getEnterSpeed() {
    	return enterSpeed;
    }
    
    public int getPaymentSpeed() {
    	return paymentSpeed;
    }
    
    public int getExitSpeed() {
    	return exitSpeed;
    }
    
    public int getWeekDayArrivals() {
    	return weekDayArrivals;
    }
    
    public int getWeekendArrivals() {
    	return weekendArrivals;
    }
    
    public int getWeekDayPassArrivals() {
    	return weekDayPassArrivals;
    }
    
    public int getWeekendPassArrivals() {
    	return weekendPassArrivals;
    }
    
    public Car[][][] getCars() {
    	return cars;
    }
    
    public Car getCarAt(Location loc) {
    	return cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
    }
    
    public void setCarAt(int floor, int row, int place, Car car) {
    	cars[floor][row][place] = car;
    }
    
    public void numberOfOpenSpotsMinusOne() {
    	numberOfOpenSpots--;    	
    }
    
    public void numberOfOpenSpotsPlusOne() {
    	numberOfOpenSpots++;    	
    }
    
    public void advanceTime() {
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
    }
}
