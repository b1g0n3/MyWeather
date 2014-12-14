package com.myweather;

public class NewsItem {

	private String temperature;
	private String icon;
	private String date;

	public String gettemperature() {
		return temperature;
	}

	public void setdate(String date) {
		this.date = date;
	}

	public String geticon() {
		return icon;
	}

	public void seticon(String icon) {
		this.icon = icon;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "[ temperature=" + temperature + ", icon=" + 
				icon + " , date=" + date + "]";
	}
}
