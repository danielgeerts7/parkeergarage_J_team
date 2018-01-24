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
public class MainView {	
	
	private JFrame frame;
	private Model model;
	
	private CarParkView carParkView;
	public JPanel buttonPane;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	private TextInformationView textInfoView;
	
	public MainView(Model model, JFrame frame, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		this.frame = frame;
		this.model = model;
		
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		buttonPane = new JPanel();
		
		textInfoView = new TextInformationView();
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		Container contentPane = frame.getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        contentPane.add(textInfoView, BorderLayout.NORTH);
        
        frame.pack();
        frame.setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
        textInfoView.updateInfo(model);
    }

}
