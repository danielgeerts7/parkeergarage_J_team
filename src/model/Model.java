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

	private int dailyParkingPassCar = 0;
	private int dailyReservCar = 0;
	private int dailyAdHocCar = 0;

	private PlaySongController playSongController;
	private String audioFilePath = "media/The-A-Team-theme-song.wav";
	private String ApplicationTitle;

	// All views that are used in this application
	public MainView mainView;

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
					Car car = getCarAt(location, false);
					if (car != null && (car instanceof AdHocCar || car instanceof ReservCar)) {
						expectedMoneyToBeMade += car.getMinutesParked() * priceToPayPerMinuteWhenParked;
					}
				}
			}
		}
	}

	private void carsArriving(){		

		int numberOfCars = getNumberOfCars(getWeekDayArrivals(), getWeekendArrivals()) * (enactModifier(entranceCarQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, AD_HOC);

		numberOfCars = getNumberOfCars(getWeekDayPassArrivals(), getWeekendPassArrivals()) * (enactModifier(entrancePassQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, PASS);

		numberOfCars = getNumberOfCars(getWeekDayReservArrivals(), getWeekendReservArrivals()) * (enactModifier(entrancePassQueue.carsInQueue())/100);
		addArrivingCars(numberOfCars, RESERV);


	}

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
		case RESERV:
			for (int i = 0; i < numberOfCars; i++) {
				getEntrancePassQueue().addCar(new ReservCar());
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
		return null;
	}
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
		return null;
	}

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

	public int getCarsParkedToday() {
		return carsParkedToday;
	}

	public long getDailyRevenue() {
		return dailyRevenue;
	}

	public int getDailyParkingPassCar() {
		return dailyParkingPassCar;
	}

	public int getDailyReservCar() {
		return dailyReservCar;
	}

	public int getDailyAdHocCar() {
		return dailyAdHocCar;
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

	public int getWeekDayReservArrivals() {
		return weekDayReservArrivals;
	}

	public int getWeekendReservArrivals() {
		return weekendReservArrivals;
	}

	public Car[][][] getCars() {
		return cars;
	}

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
			carsParkedToday = 0;
			dailyRevenue = 0;
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
		case 5:  currentDay = "Saterday";
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

	public void updatePieChart() {
		pauseSimulator();
		mainView.pieChartView.dataset.setValue("Paying cars", getCurrentCarsParkedOfClass(AdHocCar.class));
		mainView.pieChartView.dataset.setValue("Parking pass holders", getCurrentCarsParkedOfClass(ParkingPassCar.class));
		mainView.pieChartView.dataset.setValue("Free spots", numberOfOpenSpots);
		mainView.pieChartView.dataset.setValue("Cars with reserved parking spots", getCurrentCarsParkedOfClass(ReservCar.class));
		resumeSimulator(false);
	}
}

