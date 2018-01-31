package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

import controller.CarQueue;
import model.Car;

public class CarQueueView extends JPanel{
	private CarQueue q;	
	private String name;
	
	public CarQueueView(CarQueue q, String name) {
		this.q = q;
		this.name = name;
		
		setPreferredSize(new Dimension(200, 50));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Number of cars waiting in the queue for "+ name + " : " + q.carsInQueue(), 20, 20);
		int x = 20;
		if(q.carsInQueue() == 0) {
			g.drawString("This queue is empty", 25, 40);
			g.drawRect(x, 25, 125, 20);
		}else {
			for(Car car : q.getQueue()) {
				Color carColor = car.getColor();
				if (carColor != Color.LIGHT_GRAY) {
					g.setColor(carColor);
					g.fillRect(x, 25, 40, 20);
					x += 42;
				} else {
					g.setColor(Color.orange);
					g.fillRect(x, 25, 40, 20);
					x += 42;
				}
			}	
		}
	}
	
	public void updateView() {
		repaint();
	}
}
