package model;

import java.awt.Color;

/**
 * This is a subclass of Car
 * The cyan cars that you see on screen when the application is running is this class
 * It represents double parked customers
 *
 * @author danielgeerts7
 * @version 05-02-2018
 */
public class DoubleParkedCar extends Car {

	private static Color color = Color.CYAN;

	/**
	 * @return current color
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * @return current color
	 */
	public static Color getStaticColor() {
		return color;
	}
}
