package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;

/**   
 * explain here what this class does
 * 
 * @author danielgeerts7
 * @version 22-01-2018
 */
public abstract class MainView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Model model;
	
	public MainView(Model model) {
		this.model = model;
	}
	
	public abstract void updateView();

}
