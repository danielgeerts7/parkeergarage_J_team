
import java.awt.*;
import javax.swing.*;

public class TestView extends JFrame {
    
	private static JPanel panel;
	private static JLabel timeLabel;
	private static JLabel carQueueLabel;
	private static JLabel carPassQueueLabel;
	
    /**
     * Constructor for objects of class CarPark
     */
    public TestView(String nameOfView) {
    	super(nameOfView);
        super.setVisible(true);
        
        panel = new JPanel(new GridBagLayout());
        
        timeLabel = new JLabel("Time: 0/10.000");
        carQueueLabel = new JLabel("Current cars waiting: 0");
        carPassQueueLabel = new JLabel("Current pass holders waiting: 0");
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(timeLabel, c);
        c.gridy = 1;
        panel.add(carQueueLabel, c);
        c.gridy = 2;
        panel.add(carPassQueueLabel, c);
        
        this.getContentPane().add(panel);
        
        super.pack();
    }
    
    public void updateInfo(int time, Simulator sim) {
    	timeLabel.setText("Time: " + time + "/10.000");
    	carQueueLabel.setText("Current cars waiting: " + sim.getEntranceCarQueue().carsInQueue());
    	carPassQueueLabel.setText("Current pass holders waiting: " + sim.getEntrancePassholdersQueue().carsInQueue());
    }
    
    public void terminateView() {
    	super.setVisible(false);
    	super.dispose();
    }
}