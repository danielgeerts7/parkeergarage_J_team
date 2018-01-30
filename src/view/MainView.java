package view;

import java.awt.BorderLayout;
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
	private boolean fullscreen = true;
	
	private JFrame frame;
	private Model model;
	
	private CarParkView carParkView;
	private CarQueueView carQueueView;
	private CarQueueView carQueueView2;
	private QueueView queueView;
	public JPanel buttonPane;
	private JPanel northView;
	public LineChartView lineChartView;
	public PieChart pieChartView;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	private TextInformationView textInfoView;
	
	public MainView(Model model, JFrame frame, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		this.frame = frame;
		if (fullscreen) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		}
		this.model = model;
		
		carParkView = new CarParkView(model, numberOfFloors, numberOfRows, numberOfPlaces);
		carQueueView = new CarQueueView(model.getEntranceCarQueue(), "people to buy a ticket");
		carQueueView2 = new CarQueueView(model.getEntrancePassQueue(), "people who have a card");
		queueView = new QueueView(carQueueView, carQueueView2);
		buttonPane = new JPanel();
		northView = new JPanel();
		lineChartView = new LineChartView();
		pieChartView = new PieChart();	
		textInfoView = new TextInformationView();
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		
		northView.setLayout(new BorderLayout());
		northView.add(textInfoView, BorderLayout.NORTH);
		northView.add(queueView, BorderLayout.CENTER);
		northView.add(buttonPane, BorderLayout.SOUTH);
		
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		Container contentPane = frame.getContentPane();
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(carParkView, BorderLayout.WEST);
		contentPane.add(lineChartView, BorderLayout.SOUTH);
		contentPane.add(northView, BorderLayout.NORTH);
		contentPane.add(lineChartView.panel, BorderLayout.SOUTH);
		contentPane.add(pieChartView, BorderLayout.CENTER);
              
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
        lineChartView.repaint();
        pieChartView.repaint();
    }

}
