package com.myweather;


import java.io.IOException;
import java.util.ArrayList;

import com.bryanpgardner.forecastiowrapper.*;
import com.reconinstruments.ReconSDK.*;
import com.reconinstruments.webapi.IReconHttpCallback;
import com.reconinstruments.webapi.ReconHttpRequest;
import com.reconinstruments.webapi.ReconHttpResponse;
import com.reconinstruments.webapi.ReconOSHttpClient;
import com.reconinstruments.webapi.IReconHttpCallback.ERROR_TYPE;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver {
	
	public double latitude;
	public double longitude;
	TextView mCurrentTemp;
    private TextView mStatus;
    public boolean first;
	private TextView textView;
	private ReconOSHttpClient client;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		textView = (TextView) findViewById(R.id.text_view);
		textView.setMovementMethod(new ScrollingMovementMethod());
		client = new ReconOSHttpClient(this, clientCallback);

	}
    
    protected void onStart() {
    	super.onStart();
    	first=true;
	    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		latitude=50.61741134643555;
		longitude=3.1304383277893066;
		textView.setText("Lat:"+latitude+" / long:"+longitude);
		String key = "28faca837266a521f823ab10d1a45050";
		System.out.println("Lat:"+latitude+" / long:"+longitude);
		
		ForecastIO test = new ForecastIO(latitude,longitude,key);
		try {
			test.getForecast();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataBlockDaily daily = new DataBlockDaily(test.getDaily());
		DataBlockHourly hourly = new DataBlockHourly(test.getHourly());
		DataPointCurrently current = new DataPointCurrently(test.getCurrently());
  //              AlertsArray alerts = new AlertsArray(test.getAlerts());
		
		System.out.println("ForecastIO test cases:");
		System.out.println("Latitude: " + test.getLat() + " Longitude: " + test.getLon());
		System.out.println("API key: " + test.getApi());
		System.out.println("Timezone: " + test.getTimezone());
		System.out.println("Offset: " + test.getOffset());
		System.out.println("Url: " + test.getApiUrl());
		
		
		System.out.println("\nDataPointCurrently test cases: ");
		System.out.println("Time: " + current.getTime());
		System.out.println("Summary: " + current.getSummary());
		System.out.println("Icon: " + current.getIcon());
		System.out.println("Precip Intensity: " + current.getPrecipIntensity());
		System.out.println("Precip Probability: " + current.getPrecipProbability());
		System.out.println("Precip Type: " + current.getPrecipType());
		System.out.println("Dew Point: " + current.getDewPoint());
		System.out.println("Wind Speed: " + current.getWindSpeed());
		System.out.println("Wind bearing: " + current.getWindBearing());
		System.out.println("Cloud cover: " + current.getCloudCover());
		System.out.println("Humidity: " + current.getHumidity());
		System.out.println("Pressure: " + current.getPressure());
		System.out.println("Visibility: " + current.getVisibility());
		System.out.println("Ozone: " + current.getOzone());
		System.out.println("Temperature: " + current.getTemperature());
		System.out.println("Apparent Temperature: " + current.getApparentTemperature());
		System.out.println("Nearest Storm Distance: " + current.getNearestStormDistance());
		System.out.println("Nearest Storm Bearing: " + current.getNearestStormBearing());
		
		System.out.println("\nDataBlockDaily test cases:");
		System.out.println("Daily summary: " + daily.getSummary());
		System.out.println("Daily icon: " + daily.getIcon());
		System.out.println("Data point objects: " + daily.numDailyDataPoint());
		for (int i = 0; i < daily.numDailyDataPoint(); i++){
			System.out.println("\n");
			System.out.println("Time: " + daily.getDailyDataPoint(i).getTime());
			System.out.println("Summary: " + daily.getDailyDataPoint(i).getSummary());
			System.out.println("Icon: " + daily.getDailyDataPoint(i).getIcon());
			System.out.println("Precip Intensity: " + daily.getDailyDataPoint(i).getPrecipIntensity());
			System.out.println("Precip Probability: " + daily.getDailyDataPoint(i).getPrecipProbability());
			System.out.println("Precip Type: " + daily.getDailyDataPoint(i).getPrecipType());
			System.out.println("Dew Point: " + daily.getDailyDataPoint(i).getDewPoint());
			System.out.println("Wind Speed: " + daily.getDailyDataPoint(i).getWindSpeed());
			System.out.println("Wind bearing: " + daily.getDailyDataPoint(i).getWindBearing());
			System.out.println("Cloud cover: " + daily.getDailyDataPoint(i).getCloudCover());
			System.out.println("Humidity: " + daily.getDailyDataPoint(i).getHumidity());
			System.out.println("Pressure: " + daily.getDailyDataPoint(i).getPressure());
			System.out.println("Visibility: " + daily.getDailyDataPoint(i).getVisibility());
			System.out.println("Ozone: " + daily.getDailyDataPoint(i).getOzone());
			System.out.println("Sunrise: " + daily.getDailyDataPoint(i).getSunriseTime());
			System.out.println("Sunset: " + daily.getDailyDataPoint(i).getSunsetTime());
			System.out.println("Moon Phase: " + daily.getDailyDataPoint(i).getMoonPhase());
			System.out.println("Precip Intensity Max: " + daily.getDailyDataPoint(i).getPrecipIntensityMax());
			System.out.println("Precip Intensity Max Time: " + daily.getDailyDataPoint(i).getPrecipIntensityMaxTime());
			System.out.println("Precip Accumulation: " + daily.getDailyDataPoint(i).getPrecipAccumulation());
			System.out.println("Temperature Min: " + daily.getDailyDataPoint(i).getTemperatureMin());
			System.out.println("Temperature Min Time: " + daily.getDailyDataPoint(i).getTemperatureMinTime());
			System.out.println("Temperature Max: " + daily.getDailyDataPoint(i).getTemperatureMax());
			System.out.println("Temperature Max Time: " + daily.getDailyDataPoint(i).getTemperatureMaxTime());
			System.out.println("Apparent Temperature Min: " + daily.getDailyDataPoint(i).getApparentTemperatureMin());
			System.out.println("Apparent Temperature Min Time: " + daily.getDailyDataPoint(i).getApparentTemperatureMinTime());
			System.out.println("Apparent Temperature Max: " + daily.getDailyDataPoint(i).getApparentTemperatureMax());
			System.out.println("Apparent Temperature Max Time: " + daily.getDailyDataPoint(i).getApparentTemperatureMaxTime());
		}
		
		System.out.println("\nDataBlockHourly test cases: ");
		System.out.println("Hourly summary: " + hourly.getSummary());
		System.out.println("Hourly icon: " + hourly.getIcon());
		System.out.println("Data point objects: " + hourly.numHourlyDataPoint());
		for (int i = 0; i < hourly.numHourlyDataPoint(); i++){
			System.out.println("\n");
			System.out.println("Time: " + hourly.getHourlyDataPoint(i).getTime());
			System.out.println("Summary: " + hourly.getHourlyDataPoint(i).getSummary());
			System.out.println("Icon: " + hourly.getHourlyDataPoint(i).getIcon());
			System.out.println("Precip Intensity: " + hourly.getHourlyDataPoint(i).getPrecipIntensity());
			System.out.println("Precip Probability: " + hourly.getHourlyDataPoint(i).getPrecipProbability());
			System.out.println("Precip Type: " + hourly.getHourlyDataPoint(i).getPrecipType());
			System.out.println("Dew Point: " + hourly.getHourlyDataPoint(i).getDewPoint());
			System.out.println("Wind Speed: " + hourly.getHourlyDataPoint(i).getWindSpeed());
			System.out.println("Wind bearing: " + hourly.getHourlyDataPoint(i).getWindBearing());
			System.out.println("Cloud cover: " + hourly.getHourlyDataPoint(i).getCloudCover());
			System.out.println("Humidity: " + hourly.getHourlyDataPoint(i).getHumidity());
			System.out.println("Pressure: " + hourly.getHourlyDataPoint(i).getPressure());
			System.out.println("Visibility: " + hourly.getHourlyDataPoint(i).getVisibility());
			System.out.println("Ozone: " + hourly.getHourlyDataPoint(i).getOzone());
			System.out.println("Temperature: " + hourly.getHourlyDataPoint(i).getTemperature());
			System.out.println("Apparent Temperature: " + hourly.getHourlyDataPoint(i).getApparentTemperature());
			
		}
                
    //            if (test.getAlerts() != null) {
    //                System.out.println("\nAlertsArray test cases: ");
    //                System.out.println("Number of alerts: " + alerts.getNumAlertObjects());
    //                for (int i = 0; i < alerts.getNumAlertObjects(); i++) {
    //                    System.out.println("\n");
    //                    System.out.println("Title: " + alerts.getAlertObject(i).getTitle());
    //                    System.out.println("Description: " + alerts.getAlertObject(i).getDescription());
    //                    System.out.println("Expires: " + alerts.getAlertObject(i).getExpires());
    //                    System.out.println("URI: " + alerts.getAlertObject(i).getUri());
    //                }
    //            }
		
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
	
	public void sendRequest(ReconHttpRequest request) {
		if (-1 == client.sendRequest(request)) {
			Toast.makeText(MainActivity.this, "HUD not connected", Toast.LENGTH_SHORT).show();
			System.out.println("HUD not connected");
		} else {
			Toast.makeText(MainActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
		}
	}

	private IReconHttpCallback clientCallback = new IReconHttpCallback() {
		@Override
		public void onReceive(int requestId, ReconHttpResponse response) {
			Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
			System.out.println("HUD not connected");
			textView.setText(new String(response.getBody()));
		}

		@Override
		public void onError(int requestId, ERROR_TYPE type, String message) {
			Toast.makeText(MainActivity.this, "Error: " + type.toString() + "(" + message + ")", Toast.LENGTH_SHORT).show();
			textView.setText("");
		}

	};
}
