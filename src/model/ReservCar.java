package model;

import java.util.Random;
import java.awt.*;


public class ReservCar extends Car {

	private static Color COLOR = Color.lightGray;
	private static Color secondCOLOR = Color.orange;
	
	public ReservCar() {
		Random random = new Random();
		Random randomReserve = new Random();
		int randomReserveTime = randomReserve.nextInt(30);
		int stayMinutes = (int) (+ 15 + random.nextFloat() * 3 * 60);
		this.setReserveTime(randomReserveTime);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(true);
	}

	public Color getColor(){
		if (randomReserveTime > 0) {
			return COLOR;
		}
		else return secondCOLOR;
	}
	
	public static Color getStaticColor(){
		return secondCOLOR;
	}
}