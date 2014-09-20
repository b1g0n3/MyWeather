package com.myweather;


import java.util.ArrayList;
import java.util.Date;

import com.reconinstruments.ReconSDK.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver {
	
	public double latitude;
	public double longitude;
	TextView mCurrentTemp;
    private TextView mLocationDate;
    private TextView mLocationLong;
    private TextView mLocationLat;

    private TextView mPreviousLocationDate;
    private TextView mPreviousLocationLong;
    private TextView mPreviousLocationLat;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mCurrentTemp = (TextView) findViewById(R.id.current_temp);
		mLocationDate = (TextView) findViewById(R.id.id_loctime);
		mLocationLong = (TextView) findViewById(R.id.id_loclong);
		mLocationLat  = (TextView) findViewById(R.id.id_loclat);
		mPreviousLocationDate = (TextView) findViewById(R.id.id_locprevtime);
		mPreviousLocationLong = (TextView) findViewById(R.id.id_locprevlong);
		mPreviousLocationLat  = (TextView) findViewById(R.id.id_locprevlat);

		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager)getSystemService(context);
		 
		Criteria mycriteria = new Criteria();
		mycriteria.setAccuracy(Criteria.ACCURACY_FINE);
		mycriteria.setAltitudeRequired(false);
		mycriteria.setBearingRequired(false);
		mycriteria.setCostAllowed(true);
		mycriteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(mycriteria, true);
		 
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
//		locationManager.requestLocationUpdates(provider, 1000, 0, locationListener); 
		
//		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		
	}

	public void onReceiveCompleted(int status, ReconDataResult result)
	{
		
	    // check status first
	    if (status != ReconSDKManager.STATUS_OK)
	    {
	Toast toast = Toast.makeText(this,"Communication Failure with Transcend Service",Toast.LENGTH_LONG);
	        toast.show();
	        System.out.println("Communication Failure with Transcend Service");
	        return;
	    }
	    
	 // Grab on-board temperature sensor reading and convert to Fahrenheit
//	    ReconTemperature temp = (ReconTemperature)result.arrItems.get(0);
//	    System.out.println("temperature="+temp.GetTemperature());
	         
	    // now simply dump data into text fields
	    ReconLocation rloc = (ReconLocation)result.arrItems.get(0);

	    Location loc = rloc.GetLocation();
	    Location prevloc = rloc.GetPreviousLocation();

	    // SDK returns INVALID_LOCATION if no GPS fix
	    if (loc != ReconLocation.INVALID_LOCATION)
	    {
	        Date locdate = new Date (loc.getTime() );
	        mLocationDate.setText(locdate.toString() );

	        String strText = String.format(" %.2f", loc.getLatitude() );
	        mLocationLat.setText(strText);
	        System.out.println("latitude="+strText);

	        strText = String.format(" %.2f", loc.getLongitude() );
	        mLocationLong.setText(strText);
	        System.out.println("longitude="+strText);

	    }
	    else
	    {
	        mLocationDate.setText("No GPS Fix");
	        System.out.println("No GPS Fix");

	    }


	}

	private void updateWithNewLocation(Location location) {
		System.out.println("On receive3");
	    String latLong;
//	    TextView myLocation;
//	    myLocation = (TextView) findViewById(R.id.myLocation);
	 
	    if(location!=null) {
	        latitude = location.getLatitude();
	        longitude = location.getLongitude();
	        latLong = "Latitude:      " + latitude + "\nLongitude:  " + longitude;     
	    } else {
	        latLong = "Unable to obtain a GPS Location Fix";
	    }
	    System.out.println("result="+latLong);
	    }
	
	@Override
	public void onFullUpdateCompleted(int arg0, ArrayList<ReconDataResult> arg1) {
		// TODO Auto-generated method stub
		System.out.println("On receive2");
		
	}
}
