package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PieChart extends JPanel {
	private static final long serialVersionUID = -5712412361082680298L;

	private Rectangle bounds;
	public ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
	
	PieChart() {
		bounds = new Rectangle(300, 300);
	}
	
	public void paint(Graphics g) {
		drawPie((Graphics2D) g, bounds, slices);
	}
	
	void drawPie(Graphics2D g, Rectangle area, ArrayList<PieSlice> slices) {
		
		double total = 0.0D;
		
		for (int i = 0; i < slices.size(); i++) {
			total += slices.get(i).value;
		}
		
		double curValue = 0.0D;
		int startAngle = 0;
		for (int i = 0; i < slices.size(); i++) {
			startAngle = (int) (curValue * 360 / total);
			int arcAngle = (int) (slices.get(i).value * 360 / total);
			g.setColor(slices.get(i).color);
			g.fillArc(50, 50, area.width, area.height, startAngle, arcAngle);
			curValue += slices.get(i).value;
		}
	}
	
	public void addPieSlice(double value, Color color) {
		PieSlice slice = new PieSlice(value, color);
		slices.add(slice);
	}
}