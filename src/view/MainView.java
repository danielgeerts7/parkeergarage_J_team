package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;

/**   
 * explain here what this class does
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private CarParkView carParkView;
	public PieChart subscribersPieChart;
	public JPanel buttonPane;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		this.setPreferredSize(new Dimension(1500,1000));
		
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		subscribersPieChart = new PieChart(900, 100, 400, 400);
		buttonPane = new JPanel();
		
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		buttonPane.setBounds(0, 20, 1000, 100);
		carParkView.setBounds(0, 100, 800, 500);
		
		Container contentPane = getContentPane();
		contentPane.add(buttonPane);
		contentPane.add(carParkView);
		contentPane.add(subscribersPieChart);
             
		setVisible(true);
		
		pack();
    }

    public void updateView() {
        carParkView.updateView();
        subscribersPieChart.repaint();
    }

}
