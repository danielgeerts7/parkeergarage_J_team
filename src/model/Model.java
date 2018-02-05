// define package name
package model;

import java.util.HashMap;
//import java classes
import java.util.Random;

// import own classes
import controller.CarQueue;
import controller.PlaySongController;
import view.MainView;

/**   
 * This class updates all values every tick and makes the program run.
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public class Model extends Thread{

	public volatile boolean running = true;
	public volatile boolean paused = false;
	private final Object pauseLock = new Object();
	public Thread thread;

	private CarQueue entranceCarQueue;
	private CarQueue entrancePassQueue;
	private CarQueue paymentCarQueue;
	private CarQueue exitCarQueue;

	private int day;
	private int hour;
	private int minute;

	int enterSpeed; // number of cars that can enter per minute
	int paymentSpeed; // number of cars that can pay per minute
	int exitSpeed; // number of cars that can leave per minute

	int weekDayArrivals; // average number of arriving cars per hour
	int weekendArrivals; // average number of arriving cars per hour
	int weekDayPassArrivals; // average number of arriving cars per hour
	int weekendPassArrivals; // average number of arriving cars per hour
	int weekDayReservArrivals;
	int weekendReservArrivals;

	int numberOfDubbleParkedCustomers = 5; // average number of double parked customers

	private int numberOfFloors;
	private int numberOfRows;
	private int numberOfPlaces;
	private int numberOfOpenSpots;
	private int totalOpenParkingSpots;
	private int carsParkedToday;
	private Car[][][] cars;

	private int tickPause = 100;
	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	private static final String RESERV = "3";

	private int currentTick = 0;
	private double priceToPayPerMinuteWhenParked;

	private long dailyRevenue = 0;
	private long moneyMade = 0;
	private long expectedMoneyToBeMade = 0;
	private HashMap<Integer, SpecialDay> specialDays;
	
	private int totalMissedCars = 0;

	private int dailyParkingPassCar = 0;
	private int dailyReservCar = 0;
	private int dailyAdHocCar = 0;

	private PlaySongController playSongController;
	private String audioFilePath = "media/The-A-Team-theme-song.wav";
	private String ApplicationTitle;

	// All views that are used in this application
	public MainView mainView;

	/**
	 * Creates a Model for updating all the values.
	 * @param specialDays A HashMap which contains the special days.
	 * @param values The HashMap which contains all values for running the simulator.
	 * @param ApplicationTitle The title of the window.
	 * @param pricetoPayperMinuteWhenParked The price that is payed per minute for ticket holders.
	 * @param fullscreen Boolean whether or not the screen should be full screen.
	 * @param playSound If the sound track gets played.
	 */
	public Model(HashMap<Integer, SpecialDay> specialDays, HashMap<String, Integer> values, String ApplicationTitle, double pricetoPayperMinuteWhenParked,boolean fullscreen, boolean playSound) {
		entranceCarQueue = new CarQueue();
		entrancePassQueue = new CarQueue();
		paymentCarQueue = new CarQueue();
		exitCarQueue = new CarQueue();
		
		numberOfFloors = values.get("floors");
		numberOfRows = values.get("rows");
		numberOfPlaces = values.get("places");
		
		day = values.get("days");
		hour = values.get("hours");
		minute = values.get("minutes");
		
		enterSpeed = values.get("enterSpeed");
		paymentSpeed = values.get("paymentSpeed");
		exitSpeed = values.get("exitSpeed");
		
		weekDayArrivals = values.get("weekDayArrivals");
		weekendArrivals = values.get("weekendArrivals");
		weekDayPassArrivals = values.get("weekDayPassArrivals");
		weekendPassArrivals = values.get("weekendPassArrivals");
		weekDayReservArrivals = values.get("weekDayReservArrivals");
		weekendReservArrivals = values.get("weekendReservArrivals");
		
		this.priceToPayPerMinuteWhenParked = pricetoPayperMinuteWhenParked;
		this.ApplicationTitle = ApplicationTitle;

		this.specialDays = specialDays;

		this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
		cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];

		playSongController = new PlaySongController(audioFilePath, playSound);
		playSongController.start();

		mainView = new MainView(this, ApplicationTitle, fullscreen, numberOfFloors, numberOfRows, numberOfPlaces);
	}

	/**
	 * Makes the simulator run.
	 */
	public void run(){
		while (running) {
			synchronized (pauseLock) {
				if (!running) { // may have changed while waiting to synchronize on pauseLock
					break;
				}
				if (paused) {
					try {
						pauseLock.wait(); // will cause this Thread to block until 
						// another thread calls pauseLock.notifyAll()
						// Note that calling wait() will 
						// relinquish the synchronized lock that this 
						// thread holds on pauseLock so another thread
						// can acquire the lock to call notifyAll()
					} catch (InterruptedException ex) {
						break;
					}
					if (!running) { // running might have changed since we paused
						break;
					}

				}
				// Tick time
				tick(true);
			}
		}

	}
	/**Pauses the simulator.
	 * **/
	public void pauseSimulator() {
		paused = true;
	}
	
	/**Resumes the simulator.
	 * @param playSound Boolean to play the music or not.
	 * **/
	public void resumeSimulator(boolean playSound) {
		if (paused) {
			synchronized (pauseLock) {
				paused = false;
				pauseLock.notifyAll(); // Unblocks thread
			}
		}
    	if (playSound && !playSongController.isPlaying() && playSongController.canPlay()) {
			playSongController = new PlaySongController(audioFilePath);
			playSongController.start();
		}
	}
	
	private int counter = 0;

	/**Every time this method is called all parts of the simulator will be updated.
	 * @param withSleep Boolean to let the simulator sleep for a while.
	 * **/
	private void tick(boolean withSleep) {
		advanceTime();
		updateCarTime();
		handleExit();
		calculateMoney();
		if (withSleep) {
			counter++;
			updatePieChart();
			if (counter > 25) {
				updateLineChart(false);
				counter = 0;
			}
			mainView.updateView();
		}
		currentTick++;
		// Pause.
		// the + 100 ticks function doesn't need Thread.sleep
		if (withSleep) {
			try {
				Thread.sleep(tickPause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		handleEntrance();
	}
	
	private int hundredCounter = 0;
	
	/**Performs the method tick a hundred times.
	 * **/
	public void tickHundredTimes() {
		for (int i = 1; i <= 100; i++) {
			tick(false);
			hundredCounter++;
			if (hundredCounter > 25) {
				updateLineChart(true);
				hundredCounter = 0;
			}
		}
	}

	/**Updates the places of the cars.
	 * **/
	private void updateCarTime() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for(int row= 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location, false);
					if (car != null) {
						car.tick();
					}
				}
			}
		}
	}
	/** Handles all the cars that are entering the parking garage.
	 * **/
	private void handleEntrance(){
		carsArriving();
		carsEntering(getEntrancePassQueue());
		carsEntering(getEntranceCarQueue());
	}
	
	/** Handles all the cars leaving the parking garage.
	 * **/
	private void handleExit(){
		carsReadyToLeave();
		carsPaying();
		carsLeaving();
	}

	/** Calculates the amount of money that gets made per tick and calculates the expected amount of money to be made.
	 * **/
	private void calculateMoney() {
		expectedMoneyToBeMade = 0;
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location, false);
					if (car != null && (car instanceof AdHocCar || car instanceof ReservCar)) {
						expectedMoneyToBeMade += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
					}
				}
			}
		}
	}

	/**
	 * Add cars to every queue.
	 */
	private void carsArriving(){		

		int numberOfCars = getNumberOfCars(getWeekDayArrivals(), getWeekendArrivals()) * (enactModifier(entranceCarQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, AD_HOC);

		numberOfCars = getNumberOfCars(getWeekDayPassArrivals(), getWeekendPassArrivals()) * (enactModifier(entrancePassQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, PASS);

		numberOfCars = getNumberOfCars(getWeekDayReservArrivals(), getWeekendReservArrivals()) * (enactModifier(entrancePassQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, RESERV);


	}
	
	/** If there are too many cars waiting in the queue then this modifier will be enacted 
	 * to prevent more cars from getting in the queue.
	 * 
	 * @param q The amount of cars sitting in the queue.
	 * @return A value on a scale of 0 to 100 which will be divided by 100 to get a percentage 
	 * of the normal amount of cars that would've entered.
	 * **/
	private int enactModifier(int q) {
		if (q > 12) {
			return 0;
		}
		if(q > 10) {
			return 25;
		}
		if (q > 6)
			return 50;

		else{
			return 100;
		}
	}
	
	/** Gets the cars in the queue and assigns them to a parking spot.
	 * @param queue The queue from which the cars will be pulled out of.
	 * **/
	private void carsEntering(CarQueue queue){
		int i=0;
		// Remove car from the front of the queue and assign to a parking space.
		while (queue.carsInQueue() > 0 && getNumberOfOpenSpots() > 0 && i < getEnterSpeed()) {
			Car car = queue.removeCar();
			if (car instanceof ParkingPassCar) {
				Location freeLocation = getFirstFreeLocationPass();
				setCarAt(freeLocation, car);
				dailyParkingPassCar += 1;
				i++;
			}
			else if (car instanceof ReservCar) {
				Location freeLocation = getFirstFreeLocation();
				setCarAt(freeLocation, car);
				dailyReservCar += 1;
				i++;
				
			}
			else {
				Location freeLocation = getFirstFreeLocation();
				setCarAt(freeLocation, car);
				dailyAdHocCar += 1;
				i++;
			}
		}
	}

	/** Adds cars to a queue that will leave the parking garage.
	 * **/
	private void carsReadyToLeave(){
		// Add leaving cars to the payment queue.
		Car car = getFirstLeavingCar();
		while (car!=null) {
			if (car.getHasToPay()){
				car.setIsPaying(true);
				getPaymentCarQueue().addCar(car);
			}
			else {
				carLeavesSpot(car);
			}
			car = getFirstLeavingCar();
		}
	}
	
	/** Removes cars from the queue to pay and adds the money to the revenue.
	 * **/
	private void carsPaying(){
		// Let cars pay.
		int i = 0;
		while (getPaymentCarQueue().carsInQueue()>0 && i < getPaymentSpeed()){
			Car car = getPaymentCarQueue().removeCar();
			if (car instanceof AdHocCar || car instanceof ReservCar) {
	 			if (car.isDoubleParked()) {
					moneyMade += 2*(car.getMinutesParked() * priceToPayPerMinuteWhenParked);
				} else {
					moneyMade += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
				}
			}
			carLeavesSpot(car);
			dailyRevenue += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
			carLeavesSpot(car);

			if(car instanceof ReservCar) {
				moneyMade += 2;
				dailyRevenue += 2;
			}
			i++;
		}
	}
	
	/** Removes cars from the ExitCarQueue.
	 * */
	private void carsLeaving(){
		// Let cars leave.
		int i=0;
		while (getExitCarQueue().carsInQueue()>0 && i < getExitSpeed()){
			getExitCarQueue().removeCar();
			i++;
		}	
	}
	
	/** Adds new cars to the car queue for entering the parking garage.
	 * @param numberOfCars The amount of cars of a certain type that enter the garage.
	 * @param type The type of car.
	 * **/
	private void addArrivingCars(int numberOfCars, String type){
		// Add the cars to the back of the queue.
		switch(type) {
		case AD_HOC: 
			for (int i = 0; i < numberOfCars; i++) {
				getEntranceCarQueue().addCar(new AdHocCar());
			}
			break;
		case PASS:
			for (int i = 0; i < numberOfCars; i++) {
				getEntrancePassQueue().addCar(new ParkingPassCar());
			}
			break;	  
		case RESERV:
			for (int i = 0; i < numberOfCars; i++) {
				getEntrancePassQueue().addCar(new ReservCar());
			}
			break;
		}
	}
	
	/** Removes the car from a spot and adds it to the exit queue.
	 * @param car The car that leaves.
	 * **/
	private void carLeavesSpot(Car car){
		removeCarAt(car.getLocation());
		getExitCarQueue().addCar(car);
	}

	/** Sets a car at a spot and return a boolean based on the success of the operation.
	 * @param location The location that is assigned to a spot.
	 * @param car The car that gets assigned to the location.
	 * @return A boolean based on if the car has been set to the location.
	 * **/
	public boolean setCarAt(Location location, Car car) {
		if (!locationIsValid(location)) {
			return false;
		}
		if (location == null || car == null) {
			return false;
		}
		Car oldCar = getCarAt(location, false);
		if (oldCar == null && oldCar instanceof DoubleParkedCar == false) {
			setCarAt(location.getFloor(), location.getRow(), location.getPlace(), car);
			car.setLocation(location);
			numberOfOpenSpotsMinusOne();
			carsParkedToday++;
			if (!car.isDoubleParked()) {
				checkIfCarIsDubbleParked(location, car);
			}
			return true;
		}
		return false;
	}
	
	/** Checks if a car is able to double park and then based on a random value park it double.
	 * @param loc The location on where the car will be double parked.
	 * @param car The car that may be parked double.
	 * **/
	private void checkIfCarIsDubbleParked(Location loc, Car car) {
		Random r = new Random();
		// Random int between zero and 60 minutes
		int Low = 0;
		int High = 60;
		int result = r.nextInt(High-Low) + Low;
		
		if (loc.getPlace() < numberOfPlaces-1) {		
			Location nextLocation = new Location(loc.getFloor(), loc.getRow(), loc.getPlace() + 1);
			Car checkCar = cars[nextLocation.getFloor()][nextLocation.getRow()][nextLocation.getPlace()];
			if (checkCar == null) {
				if (numberOfDubbleParkedCustomers > result && locationIsValid(nextLocation)) {
					car.setDoubleParked(true);
					numberOfOpenSpotsMinusOne();
				}
			}
		}
	}
	
	/** Removes a car at a location.
	 * @param location The location that will be set to null.
	 * **/
	public void removeCarAt(Location location) {
		if (!locationIsValid(location)) {
			return;
		}
		Car car = getCarAt(location, false);
		if (car == null) {
			return;
		}
		setCarAt(location.getFloor(), location.getRow(), location.getPlace(), null);
		if (car.isDoubleParked()) {
			Location nextParkingSpot = new Location(location.getFloor(), location.getRow(), location.getPlace()+1);
			if (!locationIsValid(nextParkingSpot)) {
				return;
			}
			setCarAt(nextParkingSpot.getFloor(), nextParkingSpot.getRow(), nextParkingSpot.getPlace(), null);
			numberOfOpenSpotsPlusOne();
		}
		car.setLocation(null);
		numberOfOpenSpotsPlusOne();
		car = null;
	}
	
	/** Determines the next free location where a car can park.
	 * @return The first free location.
	 * **/
	public Location getFirstFreeLocation() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows();row++){
				if (floor == 0 && row < 4) {
					row+=4;
				}
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					if (getCarAt(location, false) == null) {
						return location;
					}
				}
			}
		}
		totalMissedCars++;
		return null;
	}
	
	/** Determines the first free location for pass holders.
	 * @return The location where the first free location is.
	 * **/
	public Location getFirstFreeLocationPass() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					if (getCarAt(location, false) == null) {
						return location;
					}
				}
			}
		}
		totalMissedCars++;
		return null;
	}

	/** Searches for cars to leave.
	 * @return The car that will leave.
	 * **/
	public Car getFirstLeavingCar() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location, false);
					if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
						return car;
					}
				}
			}
		}
		return null;
	}

	/** Determines whether or not a location is taken or not.
	 * @param location The location that gets checked if it is a good parking spot.
	 * @return A boolean that is based on if this location is available for parking.
	 * **/
	private boolean locationIsValid(Location location) {
		if (location == null) {
			return false;
		}
		int floor = location.getFloor();
		int row = location.getRow();
		int place = location.getPlace();
		if (floor < 0 || floor >= getNumberOfFloors() || row < 0 || row > getNumberOfRows() || place < 0 || place > getNumberOfPlaces()) {
			return false;
		}
		return true;
	}

	/** Gets the number of cars that enter per minute.
	 * @param weekDay The peak amount of cars that enter per hour on workdays.
	 * @param weekend The peak amount of cars that enter per hour in the weekend.
	 * @param The number of cars that enter per minute.
	 * **/
	public int getNumberOfCars(int weekDay, int weekend){
		Random random = new Random();
		
		// Get the average number of cars that arrive per hour.
		int highestPoint = day < 5 ? weekDay : weekend;
		int averageNumberOfCarsPerHour;
		
		double lowestPoint = 15;
		double amp = (highestPoint - lowestPoint) / 2.0;
		double evenPoint = amp + lowestPoint;
		averageNumberOfCarsPerHour = (int)(evenPoint + amp * Math.sin(6.28/24 * (hour-6)));
		averageNumberOfCarsPerHour = averageNumberOfCarsPerHour;

		if(specialDays.containsKey(day)) {
			averageNumberOfCarsPerHour += specialDays.get(day).getTraffic(hour);
		}

		
		// Calculate the number of cars that arrive this minute.
		double standardDeviation = averageNumberOfCarsPerHour * 0.3;
		double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
		return (int)Math.round(numberOfCarsPerHour / 60);	
	}

	/** Gets the amount of floors.
	 * @return The amount of floors.
	 * **/
	public int getNumberOfFloors() {
		return numberOfFloors;
	}


	/** Gets the amount of rows.
	 * @return The amount of rows.
	 * **/
	public int getNumberOfRows() {
		return numberOfRows;
	}


	/** Gets the amount of places.
	 * @return The amount of places.
	 * **/
	public int getNumberOfPlaces() {
		return numberOfPlaces;
	}


	/** Gets the amount of open spots.
	 * @return The amount of open spots.
	 * **/
	public int getNumberOfOpenSpots(){
		return numberOfOpenSpots;
	}


	/** Gets the amount of total cars parked today.
	 * @return The total amount of cars that parked today.
	 * **/
	public int getCarsParkedToday() {
		return carsParkedToday;
	}


	/** The money made on a single day.
	 * @return The amount of money that is made on a single day.
	 * **/
	public long getDailyRevenue() {
		return dailyRevenue;
	}


	/** Gets the amount of cars that have been parked with a parking pass on a single day.
	 * @return How many cars that have been parked with a parking pass on a single day.
	 * **/
	public int getDailyParkingPassCar() {
		return dailyParkingPassCar;
	}



	/** Gets the amount of cars that have been parked on a reserved parking spot on a single day.
	 * @return How many cars that have been parked on a reserved parking spot on a single day.
	 * **/
	public int getDailyReservCar() {
		return dailyReservCar;
	}


	/** Gets the amount of cars that have been parked with a ticket on a single day.
	 * @return How many cars that have been parked with a ticket on a single day.
	 * **/
	public int getDailyAdHocCar() {
		return dailyAdHocCar;
	}

	/** Gets the queue for the entrance for people with tickets.
	 * @return returns the CarQueue for people entering the garage with a ticket.
	 * **/
	public CarQueue getEntranceCarQueue() {
		return entranceCarQueue;
	}

	/** Gets the queue for the entrance for people with parking pass.
	 * @return returns the CarQueue for people entering the garage with a parking pass.
	 * **/
	public CarQueue getEntrancePassQueue() {
		return entrancePassQueue;
	}

	/** Gets the queue for people leaving that have to pay at the exit.
	 * @return returns the CarQueue for people leaving that have to pay at the exit.
	 * **/
	public CarQueue getPaymentCarQueue() {
		return paymentCarQueue;
	}

	/** Gets the queue for people leaving that don't have to pay at the exit.
	 * @return returns the CarQueue for people leaving that don't have to pay at the exit.
	 * **/	
	public CarQueue getExitCarQueue() {
		return exitCarQueue;
	}

	/** Gets the amount of cars that can enter per minute.
	 * @return returns the amount of cars that can enter per minute.
	 * **/
	public int getEnterSpeed() {
		return enterSpeed;
	}

	/** Gets the amount of cars that can pay per minute.
	 * @return returns the amount of cars that can pay per minute.
	 * **/
	public int getPaymentSpeed() {
		return paymentSpeed;
	}

	/** Gets the amount of cars that can leave per minute.
	 * @return returns the amount of cars that can leave per minute.
	 * **/
	public int getExitSpeed() {
		return exitSpeed;
	}

	/** Gets the amount of cars with tickets that enter at peak on week days.
	 * @return returns the amount of cars with tickets that enter on week days.
	 * **/
	public int getWeekDayArrivals() {
		return weekDayArrivals;
	}

	/** Gets the amount of cars that enter at peak hour with tickets in the weekend.
	 * @return returns the amount of cars that enter with tickets in the weekend.
	 * **/
	public int getWeekendArrivals() {
		return weekendArrivals;
	}

	/** Gets the amount of pass holders that enter at peak hour on week days.
	 * @return returns the amount of pass holders at peak hour that enter on week days.
	 * **/
	public int getWeekDayPassArrivals() {
		return weekDayPassArrivals;
	}
	
	/** Gets the amount of pass holders that enter at peak hour in the weekend.
	 * @return returns the amount of pass holders at peak hourthat enter in the weekend.
	 * **/
	public int getWeekendPassArrivals() {
		return weekendPassArrivals;
	}
	
	/** Gets the amount of cars with a reservation that enter at peak hour on weekdays.
	 * @return returns the amount of cars with a reservation at peak hour that enter on weekdays.
	 * **/
	public int getWeekDayReservArrivals() {
		return weekDayReservArrivals;
	}

	/** Gets the amount of cars with a reservation that enter at peak hour in the weekend.
	 * @return returns the amount of cars with a reservation at peak hour that enter in the weekend.
	 * **/
	public int getWeekendReservArrivals() {
		return weekendReservArrivals;
	}

	/** Gets an array with cars with a location.
	 * @return returns an array with cars.
	 * **/
	public Car[][][] getCars() {
		return cars;
	}

	/** Gets a car from an location.
	 * @param loc The location where the car will be pulled from.
	 * @param drawing A boolean that determines if a car is double parked.
	 * @return The car that is on the given location.
	 * **/
	public Car getCarAt(Location loc, boolean drawing) {
		Location oldLoc = new Location(loc.getFloor(), loc.getRow(), loc.getPlace()-1);
		
		if (locationIsValid(oldLoc)) {
			Car car = cars[oldLoc.getFloor()][oldLoc.getRow()][oldLoc.getPlace()];
			Car doublecar = cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
			if (car != null && car.isDoubleParked() && doublecar == null) {
				if (drawing) {
					return new DoubleParkedCar();
				} else {
					return car;
				}
			}
		}
		return cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
	}

	/** Puts a car into an array with the given properties.
	 * @param floor The floor on which the car will be parked.
	 * @param row The row on which the car will be parked.
	 * @param place The place where the car will be parked.
	 * **/
	public void setCarAt(int floor, int row, int place, Car car) {
		cars[floor][row][place] = car;
	}

	/**
	 * Downs the number of open spots by 1.
	 */
	public void numberOfOpenSpotsMinusOne() {
		numberOfOpenSpots--;    	
	}
	
	/**
	 * Ups the number of open spots by 1.
	 */
	public void numberOfOpenSpotsPlusOne() {
		numberOfOpenSpots++;    	
	}

	/**
	 * Lets one minute pass by and changes the hours and days accordingly.
	 */
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
			carsParkedToday = 0;
			dailyRevenue = 0;
		}
		while (day > 6) {
			day -= 7;
		}
	}

	/**
	 * Pauses the thread for 1 second.
	 */
	public void pauseThread() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return current Tick
	 */
	public int getCurrentTick() {
		return currentTick;
	}

	/**
	 * @return all the money that the paying customers have payed
	 */
	public long getEarnedMoney() {
		return moneyMade;
	}

	/**
	 * @return all the money that the paying customers yet have to pay before leaving the parking garage
	 */
	public long getExpectedMoneyToBeEarned() {
		return expectedMoneyToBeMade;
	}

	/**
	 * This function makes the string format of the time look like we are used to know
	 * @return complete time string
	 */
	public String getTime() {		
		String completeStr = "";

		if (day < 10) {
			completeStr += "0";
		}
		completeStr += day;
		completeStr += ":";
		if (hour < 10) {
			completeStr += "0";
		}
		completeStr += hour;
		completeStr += ":";
		if (minute < 10) {
			completeStr += "0";
		}
		completeStr += minute;

		return completeStr;
	}

	/**
	 * Turns the number of a day into a string.
	 * @return The day in the form of a string.
	 */
	public String getCurrentDay() {
		String currentDay = "";

		switch (day) {
		case 0:  currentDay = "Monday";
		break;
		case 1:  currentDay = "Tuesday";
		break;
		case 2:  currentDay = "Wednesday";
		break;
		case 3:  currentDay = "Thursday";
		break;
		case 4:  currentDay = "Friday";
		break;
		case 5:  currentDay = "Saturday";
		break;
		case 6:  currentDay = "Sunday";
		break;
		}

		return currentDay;
	}

	/**
	 * @return number of cars with a certain class that are parked in the garage
	 */
	public int getCurrentCarsParkedOfClass(Class<?> cls) {
		int amountOfCars = 0;
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location, false);
					if (car != null && cls.isInstance(car)) {
						amountOfCars++;
					}
				}
			}
		}
		return amountOfCars;
	}

	/**
	 * Updates the line chart.
	 * @param hundredTicks Boolean if it needs to update a hundred ticks.
	 */
	public void updateLineChart(boolean hundredTicks) {
		if (!hundredTicks) {
			pauseSimulator();
		}
		mainView.lineChartView.dataset.addValue(getCurrentCarsParkedOfClass(AdHocCar.class), "Paying Cars", Integer.toString(getCurrentTick()));
		mainView.lineChartView.dataset.addValue(getCurrentCarsParkedOfClass(ParkingPassCar.class), "ParkingPass Cars", Integer.toString(getCurrentTick()));
		mainView.lineChartView.dataset.addValue(getCurrentCarsParkedOfClass(ReservCar.class), "Cars with reserved parking spots", Integer.toString(getCurrentTick()));
		if (!hundredTicks) {
			resumeSimulator(false);
		}
	}

	/**
	 * Updates the pie chart.
	 */
	public void updatePieChart() {
		pauseSimulator();
		mainView.pieChartView.dataset.setValue("Paying cars", getCurrentCarsParkedOfClass(AdHocCar.class));
		mainView.pieChartView.dataset.setValue("Parking pass holders", getCurrentCarsParkedOfClass(ParkingPassCar.class));
		mainView.pieChartView.dataset.setValue("Free spots", numberOfOpenSpots);
		mainView.pieChartView.dataset.setValue("Cars with reserved parking spots", getCurrentCarsParkedOfClass(ReservCar.class));
		resumeSimulator(false);
	}
	
	/**
	 * @return total numbers of cars that we have missed
	 */
	public int getTotalMissedCars() {
		return totalMissedCars;
	}
}

