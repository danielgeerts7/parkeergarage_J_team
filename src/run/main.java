package run;

import controller.Controller;
import model.Model;

public  class main {
	public static void main(String[] args) {
		Model m = new Model(3, 6, 30);
		Controller controller = new Controller(m);
		m.start();
	}
}