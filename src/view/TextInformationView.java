package view;

//Import java libraries
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

//Import own made classes
import model.AdHocCar;
import model.Model;
import model.ParkingPassCar;
import model.ReservCar;

/**
 * This class shows some information on the top side of the application
 * There are labels for the titles of the information
 * and unique labels for the real information, that is being updated by the method UpdateInfo() in this class
 * 
 * @author danielgeerts7
 * @version 26-01-2018
 */
public class TextInformationView extends JPanel {

	private static JLabel currentDay, timeTitle, tickTitle, timeLabel, tickLabel;

	// Title of current parked cars
	private static JLabel currentParkedTotalCarsTitle, currentParkedPayingCarsTitle, currentParkedPassholderCarsTitle, currentParkedReservCarsTitle;
	// Info label of current parked cars
	private static JLabel currentParkedTotalCarsLabel, currentParkedPayingCarsLabel, currentParkedPassholderCarsLabel,currentParkedReservCarsLabel ;

	private static JLabel missedCarsTitle, missedCarsLabel;

	
	private static JLabel dailyAdHocCarTitle, dailyParkingPassCarTitle, dailyReservCarTitle, carsParkedTodayTitle, dailyAdHocCarLabel, dailyParkingPassCarLabel, dailyReservCarLabel, carsParkedTodayLabel;
	private static JLabel earnedMoneyTitle, expectedToBeEarnedMoneyTitle, dailyRevenueTitle, earnedMoney, expectedToBeEarnedMoney, dailyRevenueLabel;

	/**
	 * Constructor for objects of class TextInformationView
	 */
	public TextInformationView() {
		super(new GridBagLayout());
		setBackground(Color.WHITE);

		// Shows the current day of the week
		currentDay = new JLabel();
		currentDay.setFont(new Font("", Font.BOLD, 36));
		currentDay.setBorder(new EmptyBorder(0, 0, 10, 0));//top,left,bottom,right

		// Title of the time and tick
		timeTitle = new JLabel("Time: ");
		tickTitle = new JLabel("Tick: ");
		// Real information of the time and tick
		timeLabel = new JLabel("");
		tickLabel = new JLabel("");

		// Titles of the current parked cars
		currentParkedTotalCarsTitle = new JLabel("Total parked cars: ");
		currentParkedPayingCarsTitle = new JLabel("Total parked paying cars: ");
		currentParkedPayingCarsTitle.setForeground(Color.red);
		currentParkedReservCarsTitle = new JLabel("Total reserved parkingspots: ");
		currentParkedReservCarsTitle.setForeground(Color.orange);
		currentParkedPassholderCarsTitle = new JLabel("Total parked pass holders: ");
		currentParkedPassholderCarsTitle.setForeground(Color.blue);
		// Information of the current parked cars
		currentParkedTotalCarsLabel = new JLabel("");
		currentParkedPayingCarsLabel = new JLabel("");
		currentParkedPassholderCarsLabel = new JLabel("");
		currentParkedReservCarsLabel = new JLabel("");
		
		missedCarsTitle = new JLabel("Total missed customers: ");
		missedCarsLabel = new JLabel("");

		// Daily type title
		dailyAdHocCarTitle = new JLabel("Daily regular customers: ");
		dailyAdHocCarTitle.setForeground(Color.red);
		dailyParkingPassCarTitle = new JLabel("Daily parkingpassholders: ");
		dailyParkingPassCarTitle.setForeground(Color.blue);
		dailyReservCarTitle = new JLabel("Daily Reservations: ");
		dailyReservCarTitle.setForeground(Color.ORANGE);
		carsParkedTodayTitle = new JLabel("Total cars parked today: ");
		// Car queue information
		dailyAdHocCarLabel = new JLabel("");
		dailyParkingPassCarLabel = new JLabel("");
		dailyReservCarLabel = new JLabel("");
		carsParkedTodayLabel = new JLabel(""); 

		// Money title
		earnedMoneyTitle = new JLabel("Earned money by paying customers: ");
		earnedMoneyTitle.setForeground(Color.red);
		expectedToBeEarnedMoneyTitle = new JLabel("Expected money to be earned by the current parking customers: ");
		expectedToBeEarnedMoneyTitle.setForeground(Color.red);
		dailyRevenueTitle = new JLabel("Daily revenue: ");
		// Money real information
		dailyRevenueLabel = new JLabel("");
		earnedMoney = new JLabel("");
		expectedToBeEarnedMoney = new JLabel("");

		GridBagConstraints c = new GridBagConstraints();
		// Padding between labels
		c.ipadx = 25;
		c.ipady = 0;

		// add car time and tick information
		c.gridx = 5;
		c.gridy = 0;
		this.add(currentDay, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(timeTitle, c);
		c.gridy = 2;
		this.add(tickTitle, c);
		c.gridx = 2;
		c.gridy = 1;
		this.add(timeLabel, c);
		c.gridy = 2;
		this.add(tickLabel, c);

		// add current car amount information
		c.gridx = 3;
		c.gridy = 1;
		this.add(currentParkedPayingCarsTitle, c);
		c.gridy = 2;
		this.add(currentParkedPassholderCarsTitle, c);
		c.gridy = 3;
		this.add(currentParkedReservCarsTitle, c);
		c.gridy = 4;
		this.add(currentParkedTotalCarsTitle, c);
		c.gridx = 4;
		c.gridy = 1;
		this.add(currentParkedPayingCarsLabel, c);
		c.gridy = 2;
		this.add(currentParkedPassholderCarsLabel, c);
		c.gridy = 3;
		this.add(currentParkedReservCarsLabel, c);
		c.gridy = 4;
		this.add(currentParkedTotalCarsLabel, c);


		// add average cars information
		c.gridx = 5;
		c.gridy = 1;
		this.add(dailyAdHocCarTitle, c);
		c.gridy = 2;
		this.add(dailyParkingPassCarTitle, c);
		c.gridy = 3;
		this.add(dailyReservCarTitle, c);
		c.gridy = 4;
		this.add(carsParkedTodayTitle, c);
		c.gridx = 6;
		c.gridy = 1;
		this.add(dailyAdHocCarLabel, c);
		c.gridy = 2;
		this.add(dailyParkingPassCarLabel, c);
		c.gridy = 3;
		this.add(dailyReservCarLabel, c);
		c.gridy = 4;
		this.add(carsParkedTodayLabel, c);

		// add earned money information
		c.gridx = 7;
		c.gridy = 1;
		this.add(missedCarsTitle, c);
		c.gridy = 2;
		this.add(earnedMoneyTitle, c);
		c.gridy = 3;
		this.add(expectedToBeEarnedMoneyTitle, c);
		c.gridy = 4;
		this.add(dailyRevenueTitle, c);
		c.gridx = 8;
		c.gridy = 1;
		this.add(missedCarsLabel, c);
		c.gridy = 2;
		this.add(earnedMoney, c);
		c.gridy = 3;
		this.add(expectedToBeEarnedMoney, c);
		c.gridy = 4;
		this.add(dailyRevenueLabel, c);
	}

	/**
	 * Updates the texts on screen
	 * 
	 * @param model
	 */
	public void updateInfo(Model model) {
		// Update the current day of the week
		currentDay.setText(model.getCurrentDay());
		// Update tick and time
		timeLabel.setText(model.getTime());
		tickLabel.setText(Integer.toString(model.getCurrentTick()));

		// Update current parked cars information
		int parkedAdHocCars = model.getCurrentCarsParkedOfClass(AdHocCar.class);
		int parkedPassholderCars = model.getCurrentCarsParkedOfClass(ParkingPassCar.class);
		int currentParkedReservCars = model.getCurrentCarsParkedOfClass(ReservCar.class);
		currentParkedTotalCarsLabel.setText(Integer.toString(parkedAdHocCars + parkedPassholderCars + currentParkedReservCars));
		currentParkedPayingCarsLabel.setText(Integer.toString(parkedAdHocCars));
		currentParkedPassholderCarsLabel.setText(Integer.toString(parkedPassholderCars));
		currentParkedReservCarsLabel.setText(Integer.toString(currentParkedReservCars));
		
		missedCarsLabel.setText(Integer.toString(model.getTotalMissedCars()));

		// Update cars in queue information
		dailyAdHocCarLabel.setText(Integer.toString(model.getDailyAdHocCar()));
		dailyParkingPassCarLabel.setText(Integer.toString(model.getDailyParkingPassCar()));
		dailyReservCarLabel.setText(Integer.toString(model.getDailyReservCar()));
		carsParkedTodayLabel.setText(Integer.toString(model.getCarsParkedToday()));

		String euroSign = "\u20ac";

		// Information about the paying customers
		// Something with price and the expected price
		earnedMoney.setText(euroSign + " " + model.getEarnedMoney() + ".00 ,-");
		expectedToBeEarnedMoney.setText(euroSign + " " + model.getExpectedMoneyToBeEarned() + ".00 ,-");
		dailyRevenueLabel.setText(euroSign + " " + model.getDailyRevenue() + ".00 ,-");
	}
}