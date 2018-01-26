package view;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class QueueView extends JPanel{
	private CarQueueView q1;
	private CarQueueView q2;
	
	public QueueView(CarQueueView q1, CarQueueView q2) {
		this.q1 = q1;
		this.q2 = q2;
		this.setLayout(new GridLayout(2,1));
		this.add(this.q1);
		this.add(this.q2);
	}
	public void updateView() {
		q1 .updateView();
		q2 .updateView();
	}
}
