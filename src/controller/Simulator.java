package controller;

import java.awt.event.ActionEvent;

import model.AdHocCar;
import model.Car;
import model.Location;
import model.Model;
import model.ParkingPassCar;

public class Simulator extends AbstractController {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	
    private int tickPause = 100;
	
	public Simulator(Model model) {
		super(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void run() {
        for (int i = 0; i < 10000; i++) {
            tick();
        }
    }

    private void tick() {
    	model.advanceTime();
    	updateCarTime();
    	handleExit();
    	model.updateViews();
    	// Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	handleEntrance();
    }

    private void updateCarTime() {
    	for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    private void handleEntrance(){
    	carsArriving();
    	carsEntering(model.getEntrancePassQueue());
    	carsEntering(model.getEntranceCarQueue());
    }
    
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    private void carsArriving(){
    	int numberOfCars = model.getNumberOfCars(model.getWeekDayArrivals(), model.getWeekendArrivals());
        addArrivingCars(numberOfCars, AD_HOC);
        
    	numberOfCars = model.getNumberOfCars(model.getWeekDayPassArrivals(), model.getWeekendPassArrivals());
        addArrivingCars(numberOfCars, PASS);
    }

    private void carsEntering(CarQueue queue){
        int i=0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue() > 0 && 
    			model.getNumberOfOpenSpots() > 0 && 
    			i < model.getEnterSpeed()) {
            Car car = queue.removeCar();
            Location freeLocation = getFirstFreeLocation();
            setCarAt(freeLocation, car);
            i++;
        }
    }
    
    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            model.getPaymentCarQueue().addCar(car);
        	}
        	else {
        		carLeavesSpot(car);
        	}
            car = getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (model.getPaymentCarQueue().carsInQueue()>0 && i < model.getPaymentSpeed()){
            Car car = model.getPaymentCarQueue().removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
    	}
    }
    
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (model.getExitCarQueue().carsInQueue()>0 && i < model.getExitSpeed()){
    		model.getExitCarQueue().removeCar();
            i++;
    	}	
    }
    
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
    	switch(type) {
    	case AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	model.getEntranceCarQueue().addCar(new AdHocCar());
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	model.getEntranceCarQueue().addCar(new ParkingPassCar());
            }
            break;	            
    	}
    }
    
    private void carLeavesSpot(Car car){
    	removeCarAt(car.getLocation());
        model.getExitCarQueue().addCar(car);
    }
    

    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return model.getCarAt(location);
    }

    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null) {
            model.setCarAt(location.getFloor(), location.getRow(), location.getPlace(), car);
            car.setLocation(location);
            model.numberOfOpenSpotsMinusOne();
            return true;
        }
        return false;
    }

    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        model.setCarAt(location.getFloor(), location.getRow(), location.getPlace(), null);
        car.setLocation(null);
        model.numberOfOpenSpotsPlusOne();
        return car;
    }

    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= model.getNumberOfFloors() || row < 0 || row > model.getNumberOfRows() || place < 0 || place > model.getNumberOfPlaces()) {
            return false;
        }
        return true;
    }
}
