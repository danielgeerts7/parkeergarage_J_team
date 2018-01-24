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
	public JPanel buttonPane;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		buttonPane = new JPanel();
		
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		Container contentPane = getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }

}
