package com.myweather;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myweather.weatherlib;
import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.reconinstruments.ReconSDK.*;
import com.reconinstruments.webapi.IReconHttpCallback;
import com.reconinstruments.webapi.ReconHttpRequest;
import com.reconinstruments.webapi.ReconHttpResponse;
import com.reconinstruments.webapi.ReconOSHttpClient;
import com.reconinstruments.webapi.IReconHttpCallback.ERROR_TYPE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver {
	
	TextView mCurrentTemp;
    private TextView mStatus;
    public boolean first;
	private TextView textView;
	public static String result;
	private static ReconOSHttpClient client;
	public double latitude;
	public double longitude;
	static String key = "28faca837266a521f823ab10d1a45050";


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		textView = (TextView) findViewById(R.id.text_view);
		textView.setMovementMethod(new ScrollingMovementMethod());
		
	/// Recuperation de la session precedente
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	String PreviousResult = sharedpreferences.getString("PreviousResult", "");
    	System.out.println("Previous="+PreviousResult);
    	doRefresh();
    	
    	
		//ForecastIO fio = new ForecastIO(key);
		//fio.getForecast(answer);
		//FIOCurrently currently = new FIOCurrently(fio);
		//textView.setText("temperature="+currently.get().temperature());
	}
    
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("PreviousResult","blabla");
		editor.apply();
		client.close();
	}

////////////////////////////////////////////////////
	private void doRefresh() {
		result="";
		client = new ReconOSHttpClient(this, clientCallback);
		Toast.makeText(this, "Reading GPS", Toast.LENGTH_SHORT).show();
		ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		textView.setText("Waiting for GPS fix...");
		System.out.println("Waiting for GPS fix...");
	}
		
		public void onReceiveCompleted(int status, ReconDataResult result)
		{
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
			        textView.setText("Position set...");
					System.out.println("Lat:"+latitude+" / long:"+longitude);
					System.out.println("Fetching data...");
			        textView.setText("Fetching data...");
					try {
						URL url = new URL("https://api.forecast.io/forecast/28faca837266a521f823ab10d1a45050/"+latitude+","+longitude);
						Map<String, List<String>> headers = new HashMap<String, List<String>>();			
						byte[] body = "".getBytes();
						sendRequest(new ReconHttpRequest("GET", url, null, null));

					} catch (MalformedURLException e) {
						System.out.println("MalformedURLException...");
						Toast.makeText(this, "erreur http:"+e.getMessage(), Toast.LENGTH_LONG).show();
					}
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
			System.out.println("boucleX");
		}
		
		public void sendRequest(ReconHttpRequest request) {
			if (-1 == client.sendRequest(request)) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				System.out.println("HUD not connected - No Internet");
				result="NoInternet";
			} else {
				System.out.println("Request Sent");
			}
		}

		private IReconHttpCallback clientCallback = new IReconHttpCallback() {
			@Override
			public void onReceive(int requestId, ReconHttpResponse response) {
				System.out.println("Response ready...");
				//result=String(response.getBody());
				textView.setText("Data received...");
				System.out.println("return to main...");
				result=new String(response.getBody());
				textView.setText("result="+result);
				System.out.println("result="+result);	
			}
			
			@Override
			public void onError(int requestId, ERROR_TYPE type, String message) {
				System.out.println("Error: " + type.toString() + "(" + message + ")");
			}
		};
    
}
