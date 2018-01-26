package view;

import java.awt.BorderLayout;
import java.awt.Container;

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
	private CarQueueView carQueueView;
	private CarQueueView carQueueView2;
	private QueueView queueView;
	public JPanel buttonPane;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		carQueueView = new CarQueueView(carParkView.getAHCQ(), "people to buy a ticket");
		carQueueView2 = new CarQueueView(carParkView.getPPQ(), "people who have a card");
		queueView = new QueueView(carQueueView, carQueueView2);
		buttonPane = new JPanel();
		
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		Container contentPane = getContentPane();
		contentPane.add(queueView, BorderLayout.NORTH);
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
        queueView.updateView();
    }

}
