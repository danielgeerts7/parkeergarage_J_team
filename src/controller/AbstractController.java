package controller;

import java.awt.event.ActionListener;

import model.*;

/**   
 * explain here what this class does
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public abstract class AbstractController implements ActionListener{
	protected Model model;
	
	/**
	 * Register the model to the controller.
	 * 
	 * @param model the model
	 */
	AbstractController(Model model) {
		this.model = model;
	}
}
