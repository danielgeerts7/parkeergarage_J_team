package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.AdHocCar;
import model.DoubleParkedCar;
import model.ParkingPassCar;
import model.ReservCar;

/**
 *  A view that displays all the car types in a legend form.
 * @author Florian Molenaars
 * @version 5-2-2018
 */
public class LegendView extends JPanel{

	/**
	 * Creates a new legend with a set size.
	 */
	public LegendView() {
		setPreferredSize(new Dimension(230, 50));
	}
	
	/**
	 * Makes the legend display the types of cars.
	 * @param g Makes the JPanel available for drawing.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font("default", Font.BOLD, 12));
		g.drawString("Regular customers", 70, 100);
		g.drawString("Passholders", 70, 130);
		g.drawString("Customers with reservation", 70, 160);
		g.drawString("Double parked cars", 70, 190);
		g.drawString("Reserved parkingspot", 70, 220);
		g.drawString("Open parkingspot", 70, 250);
		
		//draw red car
		g.setColor(AdHocCar.getStaticColor());
		g.fillRect(25, 85, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 83, 8, 2);
		g.fillRect(51, 83, 8, 2);
		g.fillRect(31, 105, 8, 2);
		g.fillRect(51, 105, 8, 2);
		g.fillRect(35, 87, 10, 16);
		g.fillRect(52, 87, 6, 16);
		
		//draw blue car
		g.setColor(ParkingPassCar.getStaticColor());
		g.fillRect(25, 115, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 113, 8, 2);
		g.fillRect(51, 113, 8, 2);
		g.fillRect(31, 135, 8, 2);
		g.fillRect(51, 135, 8, 2);		
		g.fillRect(35, 117, 10, 16);
		g.fillRect(52, 117, 6, 16);
		
		//draw orange car
		g.setColor(ReservCar.getStaticColor());
		g.fillRect(25, 145, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 143, 8, 2);
		g.fillRect(51, 143, 8, 2);
		g.fillRect(31, 165, 8, 2);
		g.fillRect(51, 165, 8, 2);		
		g.fillRect(35, 147, 10, 16);
		g.fillRect(52, 147, 6, 16);
		
		//draw cyan car
		g.setColor(DoubleParkedCar.getStaticColor());
		g.fillRect(25, 175, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 173, 8, 2);
		g.fillRect(51, 173, 8, 2);
		g.fillRect(31, 195, 8, 2);
		g.fillRect(51, 195, 8, 2);
		g.fillRect(35, 177, 10, 16);
		g.fillRect(52, 177, 6, 16);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(25, 205, 40, 20);
		
		g.setColor(Color.white);
		g.fillRect(25, 235, 40, 20);

	}

}
