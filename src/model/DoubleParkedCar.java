package model;

import java.awt.Color;

public class DoubleParkedCar extends Car {

	private static Color color = Color.BLACK;
	
	@Override
	public Color getColor() {
		return color;
	}
	
	public static Color getStaticColor() {
		return color;
	}
}
