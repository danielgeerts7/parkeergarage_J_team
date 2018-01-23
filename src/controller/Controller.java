package controller;

import java.awt.event.ActionEvent;

import model.AdHocCar;
import model.Car;
import model.Location;
import model.Model;
import model.ParkingPassCar;

public class Controller extends AbstractController {

	public Controller(Model model) {
		super(model);
		model.mainView.start.addActionListener(this);
		model.mainView.stop.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start")) {
			
		}
		
		if (e.getActionCommand().equals("Stop")) {
		
		}
	}
	
}
