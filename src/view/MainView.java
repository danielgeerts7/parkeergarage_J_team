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
	private JPanel buttonPane;
	public JButton start;
	public JButton stop;
	
	private TextInformationView textInfoView;
	
	public MainView(Model model, JFrame frame, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		this.frame = frame;
		this.model = model;
		
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		buttonPane = new JPanel();
		
		textInfoView = new TextInformationView();
		
		Container contentPane = frame.getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        contentPane.add(textInfoView, BorderLayout.NORTH);
        
        start = new JButton("Start");
		stop = new JButton("Stop");
		buttonPane.add(start);
		buttonPane.add(stop);
        
        frame.pack();
        frame.setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
        textInfoView.updateInfo(model);
    }

}
