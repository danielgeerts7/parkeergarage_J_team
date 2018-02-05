package controller;

import java.util.LinkedList;
import java.util.Queue;
import model.Car;

/**
 * This class controller every queue that is used in Model.java
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public class CarQueue {
	private Queue<Car> queue = new LinkedList<>();

	/**
	 * Adds a car to the queue and returning a boolean if it was added succesfully.
	 * 
	 * @param car
	 * @return boolean upon success and throwing an IllegalStateException if no space is currently available.
	 */
	public boolean addCar(Car car) {
		return queue.add(car);
	}

	/**
	 * Remove a car from the queue and give a result back
	 * 
	 * @return the first car of the queue, if queue is empty it returns null
	 */
	public Car removeCar() {
		return queue.poll();
	}

	/**
	 * @return the size of this queue
	 */
	public int carsInQueue(){
		return queue.size();
	}

	/**
	 * @return the queue
	 */
	public Queue<Car> getQueue() {
		return queue;
	}
}
