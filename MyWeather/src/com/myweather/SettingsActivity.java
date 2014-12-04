package com.myweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity  {

	TextView mCurrentTemp;
    private TextView mStatus;
    public boolean first;
	private TextView textView;
	public static String result;
	public double latitude,oldLatitude;
	public double longitude,oldLongitude;
    public int testByte;
    String language,unit;
    String PreviousResult,temp;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		/// Recuperation de la session precedente
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	PreviousResult = sharedpreferences.getString("PreviousResult", "");
    	language = sharedpreferences.getString("Language", "En");
    	unit = sharedpreferences.getString("Unit", "F");
    	temp = sharedpreferences.getString("latitude", "0");
    	oldLatitude = Double.valueOf(temp);
    	temp = sharedpreferences.getString("longitude", "0");
    	oldLongitude = Double.valueOf(temp);
    	System.out.println("oldvalues:"+oldLatitude+" / "+oldLongitude+" / "+language+" / "+unit);
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Keydown: ("+keyCode+")");
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_DPAD_UP :
	        { 
	    		System.out.println("Keydown: settings ("+keyCode+")");	    		
	    		System.out.println("je ressorts");
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	finish();
	        	break;
	        }
	        case KeyEvent.KEYCODE_DPAD_CENTER :
	        {
	        	break;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
   
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("je passe par la");
		overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	}
	
    @Override
	protected void onResume() {
		super.onResume();
	    LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.scroll1);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.RIGHT, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}
