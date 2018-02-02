package run;

import javax.swing.ImageIcon;

import view.StartWindow;

public  class main {
	public static void main(String[] args) {
		StartWindow startWindow = new StartWindow("The J-Team");
		
		ImageIcon img = new ImageIcon("media/The-J-Team_icon.png");
		startWindow.setIconImage(img.getImage());
	}
}
