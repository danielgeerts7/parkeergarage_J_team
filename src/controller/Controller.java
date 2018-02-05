package controller;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import model.Model;

/**
 * This is the controller of the parking garage project.
 * It controls all button click (click event)
 *
 * @author danielgeerts7
 * @version 05-02-2018
 */
public class Controller extends AbstractController {

	Writer writer = null;

	/**
	 * Assign every buttons action listener to this class
	 * 
	 * @param model
	 */
	public Controller(Model model) {
		super(model);
		model.mainView.resume.addActionListener(this);
		model.mainView.plusHundredTicks.addActionListener(this);	
		model.mainView.pause.addActionListener(this);
		model.mainView.save.addActionListener(this);
		model.mainView.exit.addActionListener(this);
	}

	/**
	 * When a button is clicked this method will handle what to do next
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Resume")) {
			model.resumeSimulator(true);
		}
		if (e.getActionCommand().equals("Pause")) {
			model.pauseSimulator();
		}
		if (e.getActionCommand().equals("+100 ticks")) {
			model.pauseSimulator();
			model.tickHundredTimes();
			model.resumeSimulator(false);
		}
		if (e.getActionCommand().equals("Save")) {
			save();
		}
		if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
	}

	/**
	 * Save some information to a external file
	 */
	public void save() {
		Random random = new Random();
		int randomNumber = random.nextInt(1000);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(

				new FileOutputStream("Java-Simulaties/StatisJavasimulator-" + randomNumber + ".html"), "utf-8"))) {
			writer.write("<h1>Statisics: </h1><br>" + "Day: "+ model.getCurrentDay() + ", Time: " + model.getTime()+ "<br>");
			writer.write("Total Money earned this week: " + Long.toString(model.getEarnedMoney())+" euro"+ "<br>");
			writer.write("Money earned this day: " + Long.toString(model.getDailyRevenue())+ " euro<br>");
			writer.write("Total cars parked today: " + Integer.toString(model.getCarsParkedToday()) + " cars<br>"+ System.lineSeparator());
			writer.write("Total regular customers today: " + Integer.toString(model.getDailyAdHocCar()) + "<br>");
			writer.write("Total passholders today: " + Integer.toString(model.getDailyParkingPassCar()) + "<br>");
			writer.write("Total reservations made today: " + Integer.toString(model.getDailyReservCar()) + "<br>");
		} catch (IOException ex) {
			// report
		} finally {
			try {writer.close();} catch (Exception ex) {/*ignore*/}
		}	
	}
}
