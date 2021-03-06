package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import controller.Controller;
import model.Model;
import model.SpecialDay;

/**	The window that pops up when the program starts and sets the values for the simulator.
 * 	
 *@author Erik Storm
 *@version 5-2-2018
 * **/

public class StartWindow extends JFrame {
	
	private HashMap<String, Integer> values;
	private HashMap<Integer, SpecialDay> specialDays;
	private String[] daysOfTheWeek = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private ArrayList<JFormattedTextField> numberFields;
	
	private Container contentPane;
	private JPanel inputPanel;
	private GridBagConstraints grid;
	
	private JFormattedTextField floors;
	private JFormattedTextField rows;
	private JFormattedTextField places;
	
	private JFormattedTextField days;
	private JFormattedTextField hours;
	private JFormattedTextField minutes;
	
	private JFormattedTextField enterSpeed;
	private JFormattedTextField paymentSpeed;
	private JFormattedTextField exitSpeed;
	
	private JFormattedTextField weekDayArrivals;
	private JFormattedTextField weekendArrivals;
	private JFormattedTextField weekDayPassArrivals;
	private JFormattedTextField weekendPassArrivals;
	private JFormattedTextField weekDayReservArrivals;
	private JFormattedTextField weekendReservArrivals;

	private JCheckBox fullscreen;
	private JCheckBox playMusic;
	
	private JFormattedTextField pricetoPayperMinuteWhenParked;

	private JTextField parkingGarageName;
	
	private JComboBox dayPicker;
	
	private JFormattedTextField hour;
	private JFormattedTextField intensity;
	private JTextField eventName;
	private JLabel msg;

	/** Startup screen where you can change the settings of the simulator.
	 * @param title the name of the window.
	 * **/
	public StartWindow(String title) {
		super(title);
		
		values = new HashMap<>();
		specialDays = new HashMap<>();
		
		numberFields = new ArrayList<>();
		
		setPreferredSize(new Dimension(550,850));
		setResizable(true);
		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		createInputFields();
		fillSpecialDays();
		contentPane.add(new ImageComponent("media/The-J-Team_logo.png", 4), BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	/**fills the specialDays HashMap and updates the dayPicker.
	 * **/
	public void fillSpecialDays() {
		specialDays.clear();
		specialDays.put(3, new SpecialDay(3, 20, 60, "Buying Eve"));
		specialDays.put(4, new SpecialDay(4, 20, 80, "ConcertBuilding"));
		specialDays.put(5, new SpecialDay(5, 20, 80, "ConcertBuilding"));
		specialDays.put(6, new SpecialDay(6, 14, 80, "ConcertBuilding"));
		
		for(int i = 0; i < 7; i++) {
			updateDayPicker(i);
		}
		dayPicker.setSelectedIndex(0);
	}
	
	/**This method instantiates the input fields and creates the buttons
	 * **/
	public void createInputFields() {
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		grid = new GridBagConstraints();

		grid.fill = GridBagConstraints.HORIZONTAL;
		createLabel("The amount of floors: ", 0, 0, inputPanel);
		createLabel("The amount of rows: ", 0, 1, inputPanel);
		createLabel("The amount of places: ", 0, 2, inputPanel);
		
		floors = createInput(3, 1, 0, inputPanel);
		rows = createInput(6, 1, 1, inputPanel);
		places = createInput(30, 1, 2, inputPanel);

		createLabel("Starting day: ", 2, 0, inputPanel);
		createLabel("Starting hour: ", 2, 1, inputPanel);
		createLabel("Starting minute: ", 2, 2, inputPanel);
		
		days = createInput(0, 3, 0, inputPanel);
		hours = createInput(0, 3, 1, inputPanel);
		minutes = createInput(0, 3, 2, inputPanel);
		
		createBox(0,3, inputPanel);
		
		createLabel("Enter speed: ", 0, 4, inputPanel);
		createLabel("Payment speed: ", 0, 5, inputPanel);
		createLabel("Exit speed: ", 0, 6, inputPanel);
		
		enterSpeed = createInput(2, 1, 4, inputPanel);
		paymentSpeed = createInput(7, 1, 5, inputPanel);
		exitSpeed = createInput(5, 1, 6, inputPanel);
		
		createLabel("weekDayArrivals: ", 2, 4, inputPanel);
		createLabel("weekendArrivals: ", 2, 5, inputPanel);
		createLabel("weekDayPassArrivals: ", 2, 6, inputPanel);
		createLabel("weekendPassArrivals: ", 2, 7, inputPanel);
		createLabel("weekDayReservArrivals: ", 2, 8, inputPanel);
		createLabel("weekendReservArrivals: ", 2, 9, inputPanel);
		
		weekDayArrivals = createInput(115, 3, 4, inputPanel);
		weekendArrivals = createInput(200, 3, 5, inputPanel);
		weekDayPassArrivals = createInput(60, 3, 6, inputPanel);
		weekendPassArrivals = createInput(20, 3, 7, inputPanel);
		weekDayReservArrivals = createInput(80, 3, 8, inputPanel);
		weekendReservArrivals = createInput(160, 3, 9, inputPanel);
		
		fullscreen = createCheckBox("Fullscreen", true, 0, 8, inputPanel);
		playMusic = createCheckBox("Play Music", false, 0, 9, inputPanel);

		createBox(0,10, inputPanel);
		
		createLabel("�/minute when parking: ", 0, 11, inputPanel);
		pricetoPayperMinuteWhenParked = createDoubleInput(0.015, 1, 11, inputPanel);
		
		createLabel("Parking garage name: ", 2, 11, inputPanel);
		parkingGarageName = createTextInput("Cityparking Groningen", 3, 11, inputPanel);
		
		createBox(0,12, inputPanel);
		JButton start = createButton("START", 0, 13, 2, inputPanel);
		start.addActionListener(e -> start());
		
		JButton reset = createButton("RESET", 2,13, 2, inputPanel);
		reset.addActionListener(e -> reset());
		
		// For creating the special days.
		
		dayPicker = createComboBox(specialDays, 0, 14, inputPanel);
		dayPicker.addActionListener(e -> updateSpecialDay(dayPicker.getSelectedIndex()));
				
		JButton changeSpecialDay = createButton("CHANGE", 1, 14, 1, inputPanel);
		changeSpecialDay.addActionListener(e -> changeSpecialDay(dayPicker.getSelectedIndex()));
		
		JButton addSpecialDay = createButton("ADD SPECIAL DAY", 2, 14, 1, inputPanel);
		addSpecialDay.addActionListener(e -> addSpecialDay(dayPicker.getSelectedIndex()));
		
		JButton deleteSpecialDay = createButton("DELETE", 3, 14, 1, inputPanel);
		deleteSpecialDay.addActionListener(e -> deleteSpecialDay(dayPicker.getSelectedIndex()));
		
		contentPane.add(inputPanel, BorderLayout.CENTER);
		createSpecialDayEditor();
	}	
	
	/**creates the view for making special days.
	 * **/
	public void createSpecialDayEditor() {
		//setting up the panel and adding +1 to day.
		JPanel specialPanel = new JPanel();
		specialPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		GridBagConstraints specialGrid = new GridBagConstraints();
		specialPanel.setLayout(new GridBagLayout());
		
		//creating the labels and action listeners
		createLabel("The hour at which the most people park in the parking garage: ", 0, 0, specialPanel);
		hour = createInput(0, 1,0, specialPanel);
		createLabel("The average amount of cars getting parked per hour at peak hour: ", 0, 1, specialPanel);
		intensity = createInput(0, 1,1, specialPanel);
		createLabel("The name of the event: ", 0, 2, specialPanel);
		eventName = createTextInput("", 1, 2, specialPanel);
		specialGrid.gridwidth = 2;
		specialGrid.fill = GridBagConstraints.HORIZONTAL;
		msg = createLabel("", 0, 3, specialPanel);
		msg.setForeground(Color.RED);
		contentPane.add(specialPanel, BorderLayout.SOUTH);
	}
	
	/**When this method is called all the specialdayview will be updated to display the new information.
	 * @param index The index that needs to be updated.
	 * **/
	public void updateSpecialDay(Integer index) {
		if (!specialDays.containsKey(index)) {
			hour.setValue(0);
			intensity.setValue(0);	
			eventName.setText("");
		}else {
			SpecialDay specialDay = specialDays.get(index);
			hour.setValue(specialDay.getHour());
			intensity.setValue(specialDay.getIntensity());
			eventName.setText(specialDay.getName());
		}
		msg.setText("");
	}
	
	/**Deletes the special day that is selected in the dayPicker.
	 * @param index the index of the special day that will be deleted.
	 * **/
	public void deleteSpecialDay(Integer index) {
		if(specialDays.containsKey(index)) {
			specialDays.remove(index);
			updateDayPicker(index);
			updateSpecialDay(index);
			displayPositiveMessage("Special day deleted successfully!");
		}else {
			displayNegativeMessage("Cannot remove this special day because there is no special day");
		}
	}
	
	/**Updates the daypicker to display the correct information about the special days.
	 * @param index the index that needs to be updated.
	 * 
	 * **/
	public void updateDayPicker(Integer index) {
		dayPicker.removeItemAt(index);
		if(specialDays.containsKey(index)) {
			dayPicker.insertItemAt(daysOfTheWeek[index] + " (" + specialDays.get(index).getName() + ") ", index);
		}else {
			dayPicker.insertItemAt(daysOfTheWeek[index], index);
		}
		dayPicker.setSelectedIndex(index);
	}
	
	/**Adds a special day to the HashMap specialDays and updates the view so it displays the correct information.
	 * @param index the index on which the new special day will be added to.
	 * **/
	public void addSpecialDay(Integer index) {
		int d = dayPicker.getSelectedIndex();
		int h = (int)hour.getValue();
		int i = (int)intensity.getValue();
		String n = eventName.getText();
		
		if(h != 0 && i != 0 && n != "" && !specialDays.containsKey(index)) {
			SpecialDay specialDay = new SpecialDay(d, h, i, n);
			specialDays.put(dayPicker.getSelectedIndex(), specialDay);
			updateDayPicker(index);
			updateSpecialDay(index);
			displayPositiveMessage("Special day added successfully!");
		}else {
			displayNegativeMessage("Cannot enter an empty special day");
		}
	}
	
	/**Changes information of the selected index to the new input information.
	 * @param index the index of the day that will be changed.
	 * **/
	public void changeSpecialDay(Integer index) {
		int h = (int)hour.getValue();
		int i = (int)intensity.getValue();
		String n = eventName.getText();
		
		if(h != 0 && i != 0 && n != "" && specialDays.containsKey(index)) {
			SpecialDay specialDay = new SpecialDay(dayPicker.getSelectedIndex(), h, i, n);
			specialDays.put(dayPicker.getSelectedIndex(), specialDay);
			updateDayPicker(index);
			updateSpecialDay(index);
			displayPositiveMessage("Special day updated successfully!");
		}else {
			displayNegativeMessage("Cannot change a special day when there is none");
		}
	}
	
	/**Displays a positive message in a green colour.
	 * @param text The text that gets displayed.
	 * **/
	public void displayPositiveMessage(String text) {
		msg.setForeground(new Color(0, 160, 0));
		msg.setText(text);
	}
	
	/**Displays a negative message in a red colour.
	 * @param text The text that gets displayed.
	 * **/
	public void displayNegativeMessage(String text) {
		msg.setForeground(Color.RED);
		msg.setText(text);
	}
	
	/**This method will be called when the start button has been clicked
	 * so the simulator can run with the right information.
	 * This window will then be disposed because of no further need.
	 * **/
	public void start() {
		insertIntoHashMap();
		Model m = new Model(specialDays, values, parkingGarageName.getText(), (Double)pricetoPayperMinuteWhenParked.getValue(), fullscreen.isSelected() ,playMusic.isSelected());
		Controller controller = new Controller(m);
		m.start();
		setVisible(false);
		dispose();
	}
	
	/**Puts the values of all fields into a HashMap so it can be given to
	 * the constructor of the model in a cleaner way.
	 * **/
	public void insertIntoHashMap(){

		values.put("floors", (int)floors.getValue());
		values.put("rows", (int)rows.getValue());
		values.put("places", (int)places.getValue());

		values.put("days", (int)days.getValue());
		values.put("hours", (int)hours.getValue());
		values.put("minutes", (int)minutes.getValue());

		values.put("enterSpeed", (int)enterSpeed.getValue());
		values.put("paymentSpeed", (int)paymentSpeed.getValue());
		values.put("exitSpeed", (int)exitSpeed.getValue());

		values.put("weekDayArrivals", (int)weekDayArrivals.getValue());
		values.put("weekendArrivals", (int)weekendArrivals.getValue());
		values.put("weekDayPassArrivals", (int)weekDayPassArrivals.getValue());
		values.put("weekendPassArrivals", (int)weekendPassArrivals.getValue());	
		values.put("weekDayReservArrivals", (int)weekDayReservArrivals.getValue());
		values.put("weekendReservArrivals", (int)weekendReservArrivals.getValue());	
	}
	/** Resets all fields and buttons to its original state.
	 * **/
	public void reset() {
		floors.setValue(3);
		rows.setValue(6);
		places.setValue(30);
		
		days.setValue(0);
		hours.setValue(0);
		minutes.setValue(0);
		
		enterSpeed.setValue(2);
		paymentSpeed.setValue(7);
		exitSpeed.setValue(5);
		
		weekDayArrivals.setValue(115);
		weekendArrivals.setValue(200);
		weekDayPassArrivals.setValue(60);
		weekendPassArrivals.setValue(20);
		weekDayReservArrivals.setValue(80);
		weekendReservArrivals.setValue(160);
		
		fullscreen.setSelected(true);
		playMusic.setSelected(false);
		
		pricetoPayperMinuteWhenParked.setValue(0.015);
		
		parkingGarageName.setText("Cityparking Groningen");
		
		fillSpecialDays();
	}
	/**Makes a drop down menu with the correct position in the grid and on the right panel.
	 * 
	 * @param text The text which will be displayed.
	 * @param x the x value of the grid position.
	 * @param y The y value of the grid position. 
	 * @param panel the panel on which the object will be put.
	 * @return the JCombobox.
	 * **/
	public JComboBox createComboBox(HashMap<Integer, SpecialDay> specialDays, int x, int y, JPanel panel) {
		JComboBox comboBox = new JComboBox();
		comboBox.setPreferredSize(new Dimension(135,26));
		comboBox.setMaximumSize(new Dimension(135, 26));
		grid.gridx = x;
		grid.gridy = y;
		for(Integer i = 0; i < 7; i++) {
			if(specialDays.containsKey(i)) {
				String occasion = specialDays.get(i).getName();
				comboBox.addItem(daysOfTheWeek[i] + " (" + occasion + ") ");
			}else {
				comboBox.addItem(daysOfTheWeek[i]);
			}
		} 
		panel.add(comboBox, grid);
		return comboBox;
	}
	
	/**Makes a label with the correct position in the grid.
	 * 
	 * @param text The text which will be displayed.
	 * @param x the x value of the grid position.
	 * @param y The y value of the grid position. 
	 * @param panel the panel on which the object will be put.
	 * @return the JLabel that will be made
	 * **/
	public JLabel createLabel(String text, int x, int y, JPanel panel) {
		grid.insets = new Insets(10,0,0,5);
		grid.gridx = x;
		grid.gridy = y;
		JLabel label = new JLabel(text);
		panel.add(label, grid);
		return label;
	}
	
	/**Makes an input field for integers that belong to a certain x and y value in a grid.
	 * @param prefix the value that is already in the input field.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position.
	 * @param panel the panel on which the object will be put.
	 * @return returns a formatted text field.
	 * **/
	public JFormattedTextField createInput(int prefix, int x, int y, JPanel panel) {
		grid.insets = new Insets(10,0,0,25);
		grid.gridx = x;
		grid.gridy = y;
		JFormattedTextField textField = new JFormattedTextField(); 
		textField.setValue(new Integer(prefix));
		textField.setColumns(5);
		numberFields.add(textField);
		panel.add(textField, grid);
		return textField;
	}
	
	/**Makes an input field for doubles that belong to a certain x and y value in a grid.
	 * @param prefix the value that is already in the input field.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position.
	 * @param panel the panel on which the object will be put.
	 * @return returns a formatted text field.
	 * **/
	public JFormattedTextField createDoubleInput(double prefix, int x, int y, JPanel panel) {
		grid.insets = new Insets(10,0,0,25);
		grid.gridx = x;
		grid.gridy = y;
		JFormattedTextField textField = new JFormattedTextField(); 
		textField.setValue(new Double(prefix));
		textField.setColumns(5);
		panel.add(textField, grid);
		return textField;
	}
	
	/**Creates an empty box so the layout looks better.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position. 
	 * @param panel The panel on which the object will be put.
	 * **/
	public void createBox(int x, int y, JPanel panel) {
		grid.gridx = x;
		grid.gridy = y;
		panel.add(Box.createVerticalStrut(10), grid);
	}
	
	/**Creates a button with text with an x and y value that belong to a place in the grid.
	 * @param text the text that will be displayed onto a button.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position. 
	 * @param panel the panel on which the object will be put.
	 * @return returns a JButton with an x and y position.
	 * **/
	public JButton createButton(String text, int x, int y, int gridwidth, JPanel panel) {
		grid.fill = GridBagConstraints.BOTH;
		grid.gridwidth = gridwidth;
		grid.gridx = x;
		grid.gridy = y;
		JButton button = new JButton(text);
		
		panel.add(button, grid);
		grid.gridwidth = 1;
		return button;
	}
	
	/**Creates a text input field for making the parking garage's name with an x and y value for the grid.
	 * @param text the value that is already in the input field.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position. 
	 * @param panel the panel on which the object will be put.
	 * @return returns the text field.
	 * **/
	public JTextField createTextInput(String text, int x, int y, JPanel panel) {
		grid.gridx = x;
		grid.gridy = y;
		
		JTextField textField = new JTextField(text);
		panel.add(textField, grid);
		return textField;
	}

	/**Creates a checkbox with an x and y value for the grid.
	 * @param text The text that gets put next to the checkbox.
	 * @param selected Boolean to set if the box needs to start checked or not.
	 * @param x the x value of the grid position.
	 * @param y the y value of the grid position. 
	 * @param panel the panel on which the object will be put.
	 * @return returns the checkbox.
	 * **/
	public JCheckBox createCheckBox(String text, boolean selected, int x, int y, JPanel panel) {
		grid.gridx = x;
		grid.gridy = y;
		JCheckBox checkbox = new JCheckBox(text,selected);
		panel.add(checkbox, grid);
		return checkbox;
	}
}
