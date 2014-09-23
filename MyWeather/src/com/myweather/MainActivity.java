package com.myweather;


import java.util.ArrayList;

import com.reconinstruments.ReconSDK.*;

import dme.forecastiolib.FIOCurrently;
import dme.forecastiolib.ForecastIO;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver {
	
	public double latitude;
	public double longitude;
	TextView mCurrentTemp;
    private TextView mStatus;
    public boolean first;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}
    
    protected void onStart() {
    	super.onStart();
    	first=true;
	    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		latitude=50.61741134643555;
		longitude=3.1304383277893066;
		mStatus = (TextView) findViewById(R.id.status);
		mStatus.setText("Lat:"+latitude+" / long:"+longitude);
		System.out.println("Lat:"+latitude+" / long:"+longitude);
		ForecastIO fio = new ForecastIO("28faca837266a521f823ab10d1a45050"); //instantiate the class with the API key. 
		fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
		fio.setExcludeURL("hourly,minutely");             //excluded the minutely and hourly reports from the reply
	//	String response = Some_External_Http_Library.GET(fio.getUrl("38.7252993", "-9.1500364"));
		
//		fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
//		fio.setExcludeURL("hourly,minutely");             //excluded the minutely and hourly reports from the reply
//		fio.getForecast(getString(latitude), getString(longitude));   //sets the latitude and longitude - not optional
//		FIOCurrently currently = new FIOCurrently(fio);
//	    System.out.println("\nCurrently\n");
//	    String [] f  = currently.get().getFieldsArray();
//	    for(int i = 0; i<f.length;i++)
//	        System.out.println(f[i]+": "+currently.get().getByKey(f[i]));
		
    }

	public void onReceiveCompleted(int status, ReconDataResult result)
	{
		if (first) {
		    if (status != ReconSDKManager.STATUS_OK)
		    {
		    	Toast toast = Toast.makeText(this,"Communication Failure with Transcend Service",Toast.LENGTH_LONG);
		        toast.show();
		        System.out.println("Communication Failure with Transcend Service");
		        return;
		    }
		    ReconLocation rloc = (ReconLocation)result.arrItems.get(0);
		    Location loc = rloc.GetLocation();
		    Location prevloc = rloc.GetPreviousLocation();
		    if (loc != ReconLocation.INVALID_LOCATION)
		    {
		        latitude=loc.getLatitude();
		        longitude=loc.getLongitude();
		        first=false;
		    }
		    else
		    {
		        System.out.println("No GPS Fix");
		    }
		}
	}

	private String getString(double latitude2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFullUpdateCompleted(int arg0, ArrayList<ReconDataResult> arg1) {
		// TODO Auto-generated method stub
		System.out.println("boucleX");
	}
}
