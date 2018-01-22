package run;

import controller.newSimulator;
import model.Model;

public  class main {
    public static void main(String[] args) {
    	Model m = new Model(3, 6, 30);
    	newSimulator s = new newSimulator(m);
    	s.run();
    }
}
