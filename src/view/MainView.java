package view;

// Import java libraries
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Import own made classes
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
	private LegendaView legendaView;
	
	/**
	 * Set every variable that does not have a value yet
	 * Also adds every view to this.frame, then the this.frame is being shown
	 * 
	 * @param model
	 * @param applicationName, is the name of that is showed in the top of the window
	 * @param numberOfFloors
	 * @param numberOfRows
	 * @param numberOfPlaces
	 */
	public MainView(Model model, String applicationName, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		// Create JFrame with title name
		this.frame = new JFrame(applicationName);
		
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
		legendaView = new LegendaView();
		

		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		
		northView.setLayout(new BorderLayout());
		northView.add(new ImageComponent("media/The-J-Team_logo.png", 6), BorderLayout.WEST);
		northView.add(textInfoView, BorderLayout.NORTH);
		northView.add(queueView, BorderLayout.CENTER);
		northView.add(buttonPane, BorderLayout.SOUTH);
		
		
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		pieChartView.plot.setBackgroundPaint(frame.getBackground());
		pieChartView.chart.setBackgroundPaint(frame.getBackground());
		
		Container contentPane = frame.getContentPane();
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(legendaView, BorderLayout.WEST);
		contentPane.add(carParkView, BorderLayout.CENTER);		contentPane.add(lineChartView, BorderLayout.SOUTH);
		contentPane.add(northView, BorderLayout.NORTH);
		contentPane.add(lineChartView.panel, BorderLayout.SOUTH);
		contentPane.add(pieChartView, BorderLayout.EAST);
              
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
