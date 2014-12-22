package com.myweather;

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
	ArrayList<String> icons = new ArrayList<String>();
	ArrayList<String> temperatures = new ArrayList<String>();
    private ListView listView1;

	
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
    	Weather weather_data[] = new Weather[]
    		{
//	    		for(int i = 0; i<11; i++){
//    			String [] h = hourly.getHour(i).getFieldsArray();
//    			System.out.println("Hour #"+(i+1));
//    			icons.add(hourly.getHour(i).icon());
//    			temperatures.add(hourly.getHour(i).getByKey("temperature"));
        		
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
                new Weather("",R.drawable.partly_cloudy_day, "","",""),
//        		}

    		};

//    		ForecastIO fio = new ForecastIO(key);
//    		fio.getForecast(data);
//    		FIOHourly hourly = new FIOHourly(fio);
//    		new FIOHourly(fio);
//    		if(hourly.hours()<0)
//    	        System.out.println("No hourly data.");
//    	    else
//    	        System.out.println("\nHourly:\n");
    	    //Print hourly data
//	    		for(int i = 0; i<11; i++){
//	    			String [] h = hourly.getHour(i).getFieldsArray();
//	    			System.out.println("Hour #"+(i+1));
//	    			icons.add(hourly.getHour(i).icon());
//	    			temperatures.add(hourly.getHour(i).getByKey("temperature"));
//	    		}
    		
    	System.out.println("get weather content..");
        WeatherAdapter adapter = new WeatherAdapter(this, R.layout.listview_item_row, weather_data);
        TwoWayView listView1 = (TwoWayView) findViewById(R.id.lvItems);
        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
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

}
