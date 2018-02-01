package view;
// Import java
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JPanel;
// import own classes
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

	private int floorWidth = 250;
	private int rowWidth = 60;
	private int paddingTop = 100;

	private boolean doOnce = true;


	/**
	 * Constructor for objects of class CarPark
	 */
	public CarParkView(Model model, int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		setLayout(null);
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
		for(int floor = 0; floor < numberOfFloors; floor++) {
			g.setColor(Color.black);
			drawLinesAroundFloors(g, floor);
		}

		if (doOnce) {
			for(int floor = 0; floor < numberOfFloors; floor++) {
				addLabelsToTheFloors(floor);
			}
		}
		doOnce = false;

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
		//draw the car rectangle
		graphics.fillRect(
				location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20,
				paddingTop + location.getPlace() * 15,
				20 - 1,
				10 - 1);
		if (color != Color.white && color != Color.lightGray) {
			graphics.setColor(Color.black);
			//draw the car windows
			graphics.fillRect(
					location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 5,
					paddingTop + location.getPlace() * 15 + 1,
					4,
					7);
			
			graphics.fillRect(
					location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 12,
					paddingTop + location.getPlace() * 15 + 1,
					3,
					7);
		} else {
			graphics.setColor(new Color(238,238,238));
		}
		//draw the car wheels
		graphics.fillRect(location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 2,
				paddingTop + location.getPlace() * 15 - 1,
				4,
				1);

		graphics.fillRect(location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 12,
				paddingTop + location.getPlace() * 15 - 1,
				4,
				1);

		graphics.fillRect(location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 2,
				paddingTop + location.getPlace() * 15 + 9,
				4,
				1);

		graphics.fillRect(location.getFloor() * floorWidth + (1 + (int)Math.floor(location.getRow() * 0.5)) * rowWidth + (location.getRow() % 2) * 20 + 12,
				paddingTop + location.getPlace() * 15 + 9,
				4,
				1);
		
		
		
	}

	public CarQueue getAHCQ() {
		return model.getEntranceCarQueue();
	}

	public CarQueue getPPQ() {
		return model.getEntrancePassQueue();
	}

	private void drawLinesAroundFloors(Graphics g, int floor) {
		int startX = 30 + (floor * floorWidth);
		int startY = paddingTop - 40;

		int rowSize = (numberOfRows * 32) + 30;
		int placeSize = (numberOfPlaces * 10) + 50;

		g.drawRect(startX, startY, rowSize, placeSize);//posX, posY, width, height
	}


	private void addLabelsToTheFloors(int floor) {
		JLabel floorLabel = new JLabel("Floor " + floor);
		switch (floor) {
		case 0:  floorLabel.setText("Ground floor");
		break;
		case 1:  floorLabel.setText("First floor");
		break;
		case 2:  floorLabel.setText("Second floor");
		break;
		}
		floorLabel.setFont(new Font("", Font.BOLD, 24));

		this.add(floorLabel);
		int fontsize = floorLabel.getFont().getSize();
		floorLabel.setBounds((floorWidth * (floor+1)) - (fontsize + 175), paddingTop - 30, 200, fontsize);
	}
}