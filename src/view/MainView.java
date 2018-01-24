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
	public JButton resume;
	public JButton pause;
	
	public MainView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		buttonPane = new JPanel();
		
		Container contentPane = getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        
        resume = new JButton("Resume");
		pause = new JButton("Pause");
		buttonPane.add(resume);
		buttonPane.add(pause);
        
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }

}
