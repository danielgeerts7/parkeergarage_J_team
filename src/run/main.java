package run;

import javax.swing.ImageIcon;

import view.StartWindow;

public  class main {
	public static void main(String[] args) {
		ImageIcon img = new ImageIcon("media/The-J-Team_icon.png");
		StartWindow startWindow = new StartWindow("The J-Team™ Parking Simulator");
		startWindow.setIconImage(img.getImage());

	}
}
