package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

import controller.CarQueue;
import model.Car;

/**The visualization of a single queue.
 * @author Erik Storm
 * **/
public class CarQueueView extends JPanel{
	private CarQueue q;	
	private String name;
	
	/**Creates a view for for a specific CarQueue with a name of the queue.
	 * @param q The CarQueue.
	 * @param name The name of the queue.
	 * **/
	public CarQueueView(CarQueue q, String name) {
		this.q = q;
		this.name = name;
		
		setPreferredSize(new Dimension(200, 50));
	}
	
	/**Paints the view of the queue.
	 * @param g The graphics that will be used.
	 * **/
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
				} else {
					g.setColor(Color.orange);
					g.fillRect(x, 25, 40, 20);
				}
				g.setColor(Color.black);
				g.fillRect(x+6, 23, 8, 2);
				g.fillRect(x+26, 23, 8, 2);
				g.fillRect(x+6, 45, 8, 2);
				g.fillRect(x+26, 45, 8, 2);
				g.fillRect(x+10, 27, 10, 16);
				g.fillRect(x+27, 27, 6, 16);
				
				x += 42;
			}	
		}
	}
	
	/**Updates the view.
	 * **/
	public void updateView() {
		repaint();
	}
}
