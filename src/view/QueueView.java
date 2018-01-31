package view;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class QueueView extends JPanel{
	private CarQueueView q1;
	private CarQueueView q2;
	
	public QueueView(CarQueueView q1, CarQueueView q2) {
		this.q1 = q1;
		this.q2 = q2;
		this.setLayout(new GridLayout(1,2));
		this.add(this.q1);
		this.add(this.q2);
		this.setBackground(Color.white);
		this.q1.setBackground(Color.white);
		this.q2.setBackground(Color.white);
		this.setBorder(new EmptyBorder(0, 0, 10, 0));//top,left,bottom,right
	}
	public void updateView() {
		q1 .updateView();
		q2 .updateView();
	}
}
