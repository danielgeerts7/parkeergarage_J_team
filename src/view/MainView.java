package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;

/**   
 * explain here what this class does
 * 
 * @author danielgeerts7
 * @version 26-01-2018
 */
public class MainView {	
	
	// A 16:9 resolution
	private int width = 1536;
	private int height = 864;
	private boolean fullscreen = false;
	
	private JFrame frame;
	private Model model;
	
	private CarParkView carParkView;
	private CarQueueView carQueueView;
	private CarQueueView carQueueView2;
	private QueueView queueView;
	public JPanel buttonPane;
	private JPanel northView;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	private TextInformationView textInfoView;
	
	public MainView(Model model, String applicationName, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		// Create JFrame with title name
		this.frame = new JFrame(applicationName);
		
		if (fullscreen) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		}
		this.model = model;
		
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		carQueueView = new CarQueueView(carParkView.getAHCQ(), "people to buy a ticket");
		carQueueView2 = new CarQueueView(carParkView.getPPQ(), "people who have a card");
		queueView = new QueueView(carQueueView, carQueueView2);
		buttonPane = new JPanel();
		northView = new JPanel();
		
		textInfoView = new TextInformationView();
		
		northView.setLayout(new BorderLayout());
		northView.add(textInfoView, BorderLayout.CENTER);
		northView.add(new ImageComponent("media/The-J-Team_logo.png", 6), BorderLayout.WEST);
		northView.add(queueView, BorderLayout.SOUTH);
		
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		Container contentPane = frame.getContentPane();
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(carParkView, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(northView, BorderLayout.NORTH);
              
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        updateView();
    }

	/**
	 * This method updates every view (probably all JPanel)
	 */
    public void updateView() {
        carParkView.updateView();
        textInfoView.updateInfo(model);
        queueView.updateView();
    }

}
