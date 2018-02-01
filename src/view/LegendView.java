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
			g.setColor(Color.red);
			g.fillRect(25, 105, 40, 20);
			g.setColor(Color.blue);
			g.fillRect(25, 135, 40, 20);
			g.setColor(Color.orange);
			g.fillRect(25, 165, 40, 20);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(25, 195, 40, 20);
			g.setColor(Color.white);
			g.fillRect(25, 225, 40, 20);

		}
 	
}
