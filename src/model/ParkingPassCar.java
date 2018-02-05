package model;

import java.util.Random;
import java.awt.*;

/**
 * This is a subclass of Car
 * The blue cars that you see on screen when the application is running is this class
 * It represents parking pass holders
 *
 * @version 05-02-2018
 */
public class ParkingPassCar extends Car {
	private static final Color COLOR=Color.blue;

	/**
	 * Set the amount of minutes that this customer is going to be staying in the parking garage
	 * Also set if this customer has to pay or not
	 */
	public ParkingPassCar() {
		Random random = new Random();
		int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(false);
	}

	/**
	 * @return current color
	 */
	public Color getColor(){
		return COLOR;
	}

	/**
	 * @return current color
	 */
	public static Color getStaticColor(){
		return COLOR;
	}
}
