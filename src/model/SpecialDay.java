package model;

public class SpecialDay {
	
	private int day;
	private int hour;
	private int intensity;
	private String eventName;
	
	public SpecialDay(int day, int hour, int intensity, String eventName) {
		this.day = day;
		this.hour = hour;
		this.intensity = intensity;
		this.eventName = eventName;
	}
	
	public double getTraffic(int x) {
		return (-1.0 * Math.pow((x - hour), 2) + intensity) > 0 ? (-1.0 * Math.pow((x - hour), 2) + intensity) : 0;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}
	
	public void setName(String eventName) {
		this.eventName = eventName;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void changeSettings(int day, int hour, int intensity, String eventName) {
		setDay(day);
		setHour(hour);
		setIntensity(intensity);
		setName(eventName);
	}
	
	
	public int getDay() {
		return day;
	}
	public int getHour() {
		return hour;
	}
	public int getIntensity() {
		return intensity;
	}
	public String getName() {
		return eventName;
	}
}
