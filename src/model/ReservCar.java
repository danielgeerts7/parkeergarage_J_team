package model;

import java.util.Random;
import java.awt.*;


public class ReservCar extends Car {

	private Color COLOR = Color.lightGray;
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
		else return Color.orange;
	}

}