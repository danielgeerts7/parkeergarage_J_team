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
	private JPanel buttonPane;
	public JButton start;
	public JButton stop;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		buttonPane = new JPanel();
		
		
		Container contentPane = getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        
        start = new JButton("Start");
		stop = new JButton("Stop");
		buttonPane.add(start);
		buttonPane.add(stop);
        
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }

}
