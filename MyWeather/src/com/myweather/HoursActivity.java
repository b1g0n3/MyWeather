package com.myweather;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;








// import org.lucasr.twowayview.TwoWayView;
import org.lucasr.twowayview.TwoWayView;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.myweather.WeatherAdapter;
import com.myweather.Weather;

public class HoursActivity extends Activity {

	String data,language,unit;
	static String key = "28faca837266a521f823ab10d1a45050";
    private ListView listView1;
    String icon,time,temperature,precipitation,wind,vitesse;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hours);
		/// Recuperation de la session precedente
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	data = sharedpreferences.getString("PreviousResult", "");
    	language = sharedpreferences.getString("Language", "en");
    	unit = sharedpreferences.getString("Unit", "F"); 
    	System.out.println("get preferences..");
		ForecastIO fio = new ForecastIO(key);
		fio.getForecast(data);
		FIOHourly hourly = new FIOHourly(fio);
		new FIOHourly(fio);
		if (language=="en") vitesse = "mph"; else vitesse = "kmh";
		Weather weather_data[] = new Weather[11] ;
		for(int i = 0; i<11; i++){
			String [] h = hourly.getHour(i).getFieldsArray();
    		time = hourly.getHour(i).getByKey("time");
			String substr=time.substring(time.indexOf(" "));
    		time=substr.substring(1, 6);
    		//System.out.println("time: >"+substr+"<");
			icon =  hourly.getHour(i).icon().replace("\"", "");
    		String icon1 = "@drawable/"+icon.replace("-", "_");
			Resources res = getResources();
    		int icon = res.getIdentifier(icon1, "drawable", getPackageName() );
			temperature = DoubleToI(hourly.getHour(i).getByKey("temperature"))+"°";
			precipitation = (hourly.getHour(i).getByKey("precipProbability"));
			String dir=headingToString2(Integer.valueOf(hourly.getHour(i).getByKey("windBearing")));
			wind = DoubleToI(hourly.getHour(i).getByKey("windSpeed"))+" "+vitesse+" ("+dir+")";
			System.out.println("i="+i);
			weather_data[i] = new Weather(time,icon,temperature,precipitation,wind);
		}
    	System.out.println("get weather content..");
        WeatherAdapter adapter = new WeatherAdapter(this, R.layout.listview_item_row, weather_data);
        TwoWayView listView1 = (TwoWayView) findViewById(R.id.lvItems);
        listView1.setAdapter(adapter);
	}		

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		System.out.println("Keydown: ("+keyCode+")");
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_DPAD_DOWN :
	        { 
	        	finish();
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	break;
	        }
	        case KeyEvent.KEYCODE_DPAD_UP :
	        {
	        	startActivity(new Intent(this, DaysActivity.class));
	        	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	        	break;
	        }
	        case KeyEvent.KEYCODE_DPAD_CENTER :
	        {
	        	break;
	        }
	        case KeyEvent.KEYCODE_BACK :
	        {
	        	finish();
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	break;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
	
    @Override
	protected void onResume() {
		super.onResume();
	    LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.scroll3);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.RIGHT, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);

	}
	
	public String DoubleToI(String sourceDouble) {
		DecimalFormat df = new DecimalFormat("#");    		
		double db=Double.valueOf(sourceDouble);
		return df.format(db);
	}

	public static String headingToString2(double x)
    {
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        return directions[ (int)Math.round((  ((double)x % 360) / 45)) ];
    }


}
