package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import controller.CarQueue;
import model.Car;
import model.Location;
import model.Model;

public class CarParkView extends JPanel {

	private Dimension size;
	private Image carParkImage;

	private Model model;

	private int numberOfFloors;
	private int numberOfRows;
	private int numberOfPlaces;

	/**
	 * Constructor for objects of class CarPark
	 */
	 public CarParkView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		 this.model = model;
		 size = new Dimension(0, 0);

		 this.numberOfFloors = numberOfFloors;
		 this.numberOfRows = numberOfRows;
		 this.numberOfPlaces = numberOfPlaces;

	 }

	 /**
	  * Overridden. Tell the GUI manager how big we would like to be.
	  */
	 public Dimension getPreferredSize() {
		 return new Dimension(800, 500);
	 }

	 /**
	  * Overriden. The car park view component needs to be redisplayed. Copy the
	  * internal image to screen.
	  */
	 public void paintComponent(Graphics g) {
		 if (carParkImage == null) {
			 return;
		 }

		 Dimension currentSize = getSize();
		 if (size.equals(currentSize)) {
			 g.drawImage(carParkImage, 0, 0, null);
		 }
		 else {
			 // Rescale the previous image.
			 g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
		 }
	 }

	 public void updateView() {
		 // Create a new car park image if the size has changed.
		 Dimension d = getSize();
		 if (!size.equals(getSize())) {
			 size = getSize();
			 carParkImage = createImage(size.width, size.height);
		 }
		 if (carParkImage != null) {
			 Graphics graphics = carParkImage.getGraphics();
			 for(int floor = 0; floor < numberOfFloors; floor++) {
				 for(int row = 0; row < numberOfRows; row++) {
					 for(int place = 0; place < numberOfPlaces; place++) {
						 Location location = new Location(floor, row, place);
						 Car car = model.getCarAt(location);
						 Color color = car == null ? Color.white : car.getColor();
						 drawPlace(graphics, location, color);
					 }
				 }
			 }
		 }
		 repaint();
	 }

	 /**
	  * Paint a place on this car park view in a given color.
	  */
	 private void drawPlace(Graphics graphics, Location location, Color color) {
		 graphics.setColor(color);
		 graphics.fillRect(
				 location.getFloor() * 250 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 60 + (location.getRow() % 2) * 20,
				 50 + location.getPlace() * 10,
				 20 - 1,
				 10 - 1); // TODO use dynamic size or constants
	 }
	 
	 public CarQueue getAHCQ() {
		 return model.getEntranceCarQueue();
	 }

	 public CarQueue getPPQ() {
		 return model.getEntrancePassQueue();
	 }
}