// define package name
package model;

//import java classes
import java.util.Random;

// import own classes
import controller.CarQueue;
import controller.PlaySongController;
import view.MainView;

/**   
 * explain here what this class does
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

	private int tickPause = 100;
	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	
	private int currentTick = 0;
	private double priceToPayPerMinuteWhenParked = 0.1;
	
	private int moneyMade = 0;
	private int expectedMoneyToBeMade = 0;
	
	private PlaySongController playSongController;
	private String audioFilePath = "media/The-A-Team-theme-song.wav";
	
	// Create the application title for the JFrame in MainView
	private String ApplicationTitle = "The J-Team";
	public MainView mainView;

	public Model(int numberOfFloors, int numberOfRows, int numberOfPlaces, boolean playSound) {
		entranceCarQueue = new CarQueue();
		entrancePassQueue = new CarQueue();
		paymentCarQueue = new CarQueue();
		exitCarQueue = new CarQueue();

		this.numberOfFloors = numberOfFloors;
		this.numberOfRows = numberOfRows;
		this.numberOfPlaces = numberOfPlaces;

		this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
		cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];

		playSongController = new PlaySongController(audioFilePath, playSound);
		playSongController.start();

		mainView = new MainView(this, ApplicationTitle, numberOfFloors, numberOfRows, numberOfPlaces);
	}
	

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

    public void pauseSimulator() {
        paused = true;
    }

    public void resumeSimulator() {
    	if (paused) {
    		synchronized (pauseLock) {
	            paused = false;
	            pauseLock.notifyAll(); // Unblocks thread
	        }
    	}
    	if (!playSongController.isPlaying() && playSongController.canPlay()) {
			playSongController = new PlaySongController(audioFilePath);
			playSongController.start();
		}
    }
	
	private void tick(boolean withSleep) {
		advanceTime();
		updateCarTime();
		handleExit();
		calculateMoney();
		mainView.updateView();
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
	
	public void tickHundredTimes() {
		for (int i = 1; i <= 100; i++) {
			tick(false);
		}
	}

	private void updateCarTime() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for(int row= 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
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
		carsEntering(getEntrancePassQueue());
		carsEntering(getEntranceCarQueue());
	}

	private void handleExit(){
		carsReadyToLeave();
		carsPaying();
		carsLeaving();
	}
	
	private void calculateMoney() {
		expectedMoneyToBeMade = 0;
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location);
					if (car != null && car instanceof AdHocCar) {
						expectedMoneyToBeMade += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
					}
				}
			}
		}
	}

	private void carsArriving(){
		//System.out.println(qModifier + " " + entranceCarQueue.carsInQueue());
		
		int numberOfCars = getNumberOfCars(getWeekDayArrivals(), getWeekendArrivals()) * (enactModifier(entranceCarQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, AD_HOC);
		
		numberOfCars = getNumberOfCars(getWeekDayPassArrivals(), getWeekendPassArrivals()) * (enactModifier(entrancePassQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, PASS);
	}
	
	private int enactModifier(int q) {
		if (q > 15) {
			return 0;
		}
		if(q > 12) {
			return 25;
		}
		if (q > 10)
		return 50;
		
		else{
			return 100;
		}
	}

	private void carsEntering(CarQueue queue){
		int i=0;
		// Remove car from the front of the queue and assign to a parking space.
		while (queue.carsInQueue() > 0 && getNumberOfOpenSpots() > 0 && i < getEnterSpeed()) {
			Car car = queue.removeCar();
			if (car instanceof ParkingPassCar) {
			Location freeLocation = getFirstFreeLocationPass();
			setCarAt(freeLocation, car);
			i++;
			}
			else {
			Location freeLocation = getFirstFreeLocation();
			setCarAt(freeLocation, car);
			i++;
			}
		}
	}

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

	private void carsPaying(){
		// Let cars pay.
		int i = 0;
		while (getPaymentCarQueue().carsInQueue()>0 && i < getPaymentSpeed()){
			Car car = getPaymentCarQueue().removeCar();
			// TODO Handle payment.
			moneyMade += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
			carLeavesSpot(car);
			i++;
		}
	}

	private void carsLeaving(){
		// Let cars leave.
		int i=0;
		while (getExitCarQueue().carsInQueue()>0 && i < getExitSpeed()){
			getExitCarQueue().removeCar();
			i++;
		}	
	}

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
		}
	}

	private void carLeavesSpot(Car car){
		removeCarAt(car.getLocation());
		getExitCarQueue().addCar(car);
	}

	public boolean setCarAt(Location location, Car car) {
		if (!locationIsValid(location)) {
			return false;
		}
		Car oldCar = getCarAt(location);
		if (oldCar == null) {
			setCarAt(location.getFloor(), location.getRow(), location.getPlace(), car);
			car.setLocation(location);
			numberOfOpenSpotsMinusOne();
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
		setCarAt(location.getFloor(), location.getRow(), location.getPlace(), null);
		car.setLocation(null);
		numberOfOpenSpotsPlusOne();
		return car;
	}

	public Location getFirstFreeLocation() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows();row++){
				if (floor == 0 && row < 4) {
				row+=4;
				}
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					if (getCarAt(location) == null) {
						return location;
					}
				}
			}
		}
		return null;
	}
	public Location getFirstFreeLocationPass() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
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
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
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
		if (floor < 0 || floor >= getNumberOfFloors() || row < 0 || row > getNumberOfRows() || place < 0 || place > getNumberOfPlaces()) {
			return false;
		}
		return true;
	}

	public int getNumberOfCars(int weekDay, int weekend){
		Random random = new Random();

		// Get the average number of cars that arrive per hour.
		int averageNumberOfCarsPerHour = day < 5 ? weekDay : weekend;

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
	public double getEarnedMoney() {
		return moneyMade;
	}
	
	/**
	 * @return all the money that the paying customers yet have to pay before leaving the parking garage
	 */
	public double getExpectedMoneyToBeEarned() {
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
	 * @return number of cars with a certain class that are parked in the garage
	 */
	public int getCurrentCarsParkedOfClass(Class<?> cls) {
		int amountOfCars = 0;
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location);
					if (car != null && cls.isInstance(car)) {
						amountOfCars++;
					}
				}
			}
		}
		return amountOfCars;
	}
}
