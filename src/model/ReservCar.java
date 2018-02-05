package model;

import java.util.Random;
import java.awt.*;

/**
 * This is a subclass of Car
 * The orange cars that you see on screen when the application is running is this class
 * Also the lightgray parking spots come from this class 
 * It represents customers that have reserved
 *
 * @author Florian Molenaars
 * @version 05-02-2018
 */
public class ReservCar extends Car {

	private static Color COLOR = Color.lightGray;
	private static Color secondCOLOR = Color.orange;

	/**
	 * Set the amount of minutes that this customer is going to be staying in the parking garage
	 * Also set if this customer has to pay or not
	 */
	public ReservCar() {
		Random random = new Random();
		Random randomReserve = new Random();
		int randomReserveTime = randomReserve.nextInt(30);
		int stayMinutes = (int) (+ 15 + random.nextFloat() * 3 * 60);
		this.setReserveTime(randomReserveTime);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(true);
	}

	/**
	 * @return current color
	 */
	public Color getColor(){
		if (randomReserveTime > 0) {
			return COLOR;
		}
		else return secondCOLOR;
	}

	/**
	 * @return orange color
	 */
	public static Color getStaticColor(){
		return secondCOLOR;
	}
}