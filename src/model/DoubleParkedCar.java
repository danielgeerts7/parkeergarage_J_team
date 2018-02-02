package model;

import java.awt.Color;

public class DoubleParkedCar extends Car {

	private static Color color = Color.CYAN;
	
	@Override
	public Color getColor() {
		return color;
	}
	
	public static Color getStaticColor() {
		return color;
	}
}
