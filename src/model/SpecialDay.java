package model;

/**
 * A day when there are more people parking at a different time because of an event.
 * @author Erik Storm
 * @version 5-2-2018
 */
public class SpecialDay {
	
	private int day;
	private int hour;
	private int intensity;
	private String eventName;
	
	/**
	 * Creates a specialDay with a given day, hour, intensity and name.
	 * @param day The day on which the event will take place.
	 * @param hour The hour on which the event will take place.
	 * @param intensity The amount of cars that arrive extra at peak hour.
	 * @param eventName The name of the event.
	 */
	public SpecialDay(int day, int hour, int intensity, String eventName) {
		this.day = day;
		this.hour = hour;
		this.intensity = intensity;
		this.eventName = eventName;
	}
	
	/**
	 * Gets the amount of extra cars that come per hour at a certain time.
	 * @param x The hour.
	 * @return The extra amount of cars.
	 */
	public double getTraffic(int x) {
		return (-1.0 * Math.pow((x - hour), 2) + intensity) > 0 ? (-1.0 * Math.pow((x - hour), 2) + intensity) : 0;
	}
	
	/**
	 * Changes the hour when the event starts.
	 * @param hour The new hour.
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * Changes the intensity of the event.
	 * @param intensity The new intensity.
	 */
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}
	
	/**
	 * Changes the name of the event.
	 * @param eventName The new name.
	 */
	public void setName(String eventName) {
		this.eventName = eventName;
	}
	
	/**
	 * Changes the day of the event.
	 * @param day The new day.
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Changes all the settings of the event.
	 * @param day
	 * @param hour
	 * @param intensity
	 * @param eventName
	 */
	public void changeSettings(int day, int hour, int intensity, String eventName) {
		setDay(day);
		setHour(hour);
		setIntensity(intensity);
		setName(eventName);
	}
	
	/**
	 * @return The day of the event.
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * @return The hour when the event starts.
	 */
	public int getHour() {
		return hour;
	}
	
	/**
	 * @return The intensity of the event.
	 */
	public int getIntensity() {
		return intensity;
	}
	
	/**
	 * @return The name of the event.
	 */
	public String getName() {
		return eventName;
	}
}
