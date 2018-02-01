package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LegendView extends JPanel{

	public LegendView() {
		setPreferredSize(new Dimension(230, 50));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font("default", Font.BOLD, 12));
		g.drawString("Regular customers", 70, 120);
		g.drawString("Passholders", 70, 150);
		g.drawString("Customers with reservation", 70, 180);
		g.drawString("Reserved parkingspot", 70, 210);
		g.drawString("Open parkingspot", 70, 240);
		
		//draw red car
		g.setColor(Color.red);
		g.fillRect(25, 105, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 103, 8, 2);
		g.fillRect(51, 103, 8, 2);
		g.fillRect(31, 125, 8, 2);
		g.fillRect(51, 125, 8, 2);
		g.fillRect(35, 107, 10, 16);
		g.fillRect(52, 107, 6, 16);
		
		//draw blue car
		g.setColor(Color.blue);
		g.fillRect(25, 135, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 133, 8, 2);
		g.fillRect(51, 133, 8, 2);
		g.fillRect(31, 155, 8, 2);
		g.fillRect(51, 155, 8, 2);		
		g.fillRect(35, 137, 10, 16);
		g.fillRect(52, 137, 6, 16);
		
		//draw orange car
		g.setColor(Color.orange);
		g.fillRect(25, 165, 40, 20);
		g.setColor(Color.black);
		g.fillRect(31, 162, 8, 2);
		g.fillRect(51, 162, 8, 2);
		g.fillRect(31, 185, 8, 2);
		g.fillRect(51, 185, 8, 2);		
		g.fillRect(35, 167, 10, 16);
		g.fillRect(52, 167, 6, 16);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(25, 195, 40, 20);
		
		g.setColor(Color.white);
		g.fillRect(25, 225, 40, 20);

	}

}
