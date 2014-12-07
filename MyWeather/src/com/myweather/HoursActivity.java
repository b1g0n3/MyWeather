package com.myweather;

import java.util.ArrayList;

import org.lucasr.twowayview.TwoWayView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class HoursActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hours);

		ArrayList<String> items = new ArrayList<String>();
		items.add("Item 1");
		items.add("Item 2");
		items.add("Item 3");
		items.add("Item 4");
		
		ArrayAdapter<String> aItems = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, items);
		TwoWayView lvTest = (TwoWayView) findViewById(R.id.lvItems);
		lvTest.setAdapter(aItems);
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
