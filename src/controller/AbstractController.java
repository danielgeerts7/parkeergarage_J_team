package controller;

import java.awt.event.ActionListener;
import model.*;

/**   
 * This is the superclass for some controller classes
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public abstract class AbstractController implements ActionListener{
	protected Model model;
	
	/**
	 * Register the model to the controller.
	 * 
	 * @param Model the model
	 */
	AbstractController(Model model) {
		this.model = model;
	}
}
