package view;

// Import java libraries
import java.awt.BorderLayout;
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
	private JPanel centerView;
	private JPanel southView;
	public LineChartView lineChartView;
	public PieChart pieChartView;
	public JButton resume;
	public JButton pause;
	public JButton plusHundredTicks;
	
	private TextInformationView textInfoView;
	private LegendView legendaView;
	
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
		centerView = new JPanel();
		southView = new JPanel();
		lineChartView = new LineChartView();
		pieChartView = new PieChart();	
		textInfoView = new TextInformationView();
		legendaView = new LegendView();
		resume = new JButton("Resume");
		pause = new JButton("Pause");
		plusHundredTicks = new JButton("+100 ticks");
		
		northView.setLayout(new BorderLayout());
		northView.add(new ImageComponent("media/The-J-Team_logo.png", 6), BorderLayout.WEST);
		northView.add(textInfoView, BorderLayout.CENTER);
		northView.add(queueView, BorderLayout.SOUTH);
		
		centerView.setLayout(new BorderLayout());
		centerView.add(buttonPane, BorderLayout.NORTH);
		centerView.add(carParkView, BorderLayout.CENTER);
		centerView.add(legendaView, BorderLayout.WEST);
		
		southView.setLayout(new BorderLayout());
		southView.add(lineChartView.panel, BorderLayout.CENTER);
		southView.add(pieChartView, BorderLayout.EAST);
		
		buttonPane.add(resume);
		buttonPane.add(pause);
		buttonPane.add(plusHundredTicks);
		
		pieChartView.plot.setBackgroundPaint(frame.getBackground());
		pieChartView.chart.setBackgroundPaint(frame.getBackground());
		
		Container contentPane = frame.getContentPane();
		contentPane.setPreferredSize(new Dimension(width, height));
		contentPane.add(northView, BorderLayout.NORTH);
		contentPane.add(centerView, BorderLayout.CENTER);
		contentPane.add(southView, BorderLayout.SOUTH);
              
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        System.out.println(frame.getBackground());

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
