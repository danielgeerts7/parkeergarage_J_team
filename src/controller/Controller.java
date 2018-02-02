package controller;

import java.awt.event.ActionEvent;

import model.Model;

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
	}
}
