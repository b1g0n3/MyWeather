package com.myweather;


import java.util.ArrayList;
import java.util.Date;

import com.reconinstruments.ReconSDK.*;

import dme.forecastiolib.ForecastIO;
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
    private TextView mStatus;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
	}
    
    protected void onStart() {
    	super.onStart();
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
	    System.out.println("ReconSDKManager.STATUS_OK");
	    ReconLocation rloc = (ReconLocation)result.arrItems.get(0);
	    Location loc = rloc.GetLocation();
	    Location prevloc = rloc.GetPreviousLocation();

	    // SDK returns INVALID_LOCATION if no GPS fix
	    if (loc != ReconLocation.INVALID_LOCATION)
	    {
	        Date locdate = new Date (loc.getTime() );
	        System.out.println("locationdate="+locdate.toString() );
	        latitude=loc.getLatitude();
	        longitude=loc.getLongitude();
	        System.out.println("latitude="+latitude);
	        System.out.println("longitude="+longitude);
			mStatus = (TextView) findViewById(R.id.status);
			mStatus.setText("Lat:"+latitude+" / long:"+longitude);
			ForecastIO fio = new ForecastIO("28faca837266a521f823ab10d1a45050"); //instantiate the class with the API key. 
			fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
			fio.setExcludeURL("hourly,minutely");             //excluded the minutely and hourly reports from the reply
			fio.getForecast(getString(latitude), getString(longitude));   //sets the latitude and longitude - not optional
	    }
	    else
	    {
	        System.out.println("No GPS Fix");
	    }
	}

	private String getString(double latitude2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFullUpdateCompleted(int arg0, ArrayList<ReconDataResult> arg1) {
		// TODO Auto-generated method stub
		System.out.println("On receive4");
	}
}
