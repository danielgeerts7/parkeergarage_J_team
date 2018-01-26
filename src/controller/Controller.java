package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;

import model.Model;
import view.PieSlice;

public class Controller extends AbstractController {

	public Controller(Model model) {
		super(model);
		model.mainView.resume.addActionListener(this);
		model.mainView.pause.addActionListener(this);
		model.mainView.plusHundredTicks.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Resume")) {
			model.resumeSimulator();
		}
		
		if (e.getActionCommand().equals("Pause")) {
			model.pauseSimulator();
		}
		
		if (e.getActionCommand().equals("+100 ticks")) {
			model.pauseSimulator();
			model.tickHundredTimes();
			model.resumeSimulator();
		}
	}
}
