package run;

import controller.Simulator;
import model.Model;

public  class main {
    public static void main(String[] args) {
    	Model m = new Model(3, 6, 30);
    	Simulator s = new Simulator(m);
    	s.run();
    }
}
