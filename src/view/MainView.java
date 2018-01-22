package view;

import java.awt.BorderLayout;
import java.awt.Container;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CarParkView carParkView;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		
		Container contentPane = getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }

}
