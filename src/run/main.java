package run;

import javax.swing.ImageIcon;

import view.StartWindow;

/**The main class that holds the method that will be called when you start the program.
 * @author Daniël Geerts
 * @author Stijn Westerhof
 * @author Florian Molenaars
 * @author Erik Storm
 * @version 1.0
 * 
 * **/

public  class main {
	/**Method that gets called when the program gets started and
	 * creates the startwindow to set certain values.
	 * @param args Array with strings.
	 * **/
	public static void main(String[] args) {
		ImageIcon img = new ImageIcon("media/The-J-Team_icon.png");
		StartWindow startWindow = new StartWindow("The J-Team™ Parking Simulator");
		startWindow.setIconImage(img.getImage());

	}
}
