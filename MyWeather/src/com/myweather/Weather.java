package com.myweather;

public class Weather {
    public int icon;
    public String time,temperature,precipitation,wind;
    public Weather(){
        super();
    }
    
    public Weather(String time, int icon, String temperature, String precipitation, String wind) {
        super();
        this.time = time;
        this.icon = icon;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
    }
}