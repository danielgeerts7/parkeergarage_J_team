package controller;


import java.util.LinkedList;
import java.util.Queue;

import model.Car;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    public boolean addCar(Car car) {
        return queue.add(car);
    }

    public Car removeCar() {
        return queue.poll();
    }

    public int carsInQueue(){
    	return queue.size();
    }
    
    public Queue<Car> getQueue() {
    	return queue;
    }
}
