package com.myweather;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.util.Log;
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
		System.out.println("Fetching data...");
		try {
			URL url = new URL("https://api.forecast.io/forecast/28faca837266a521f823ab10d1a45050/50.61741134643555,3.1304383277893066");
		//	URL url = new URL("http://www.reconinstruments.com");
			Map<String, List<String>> headers = new HashMap<String, List<String>>();			
			byte[] body = "".getBytes();
			sendRequest(new ReconHttpRequest("GET", url, null, null));

		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException...");
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
   }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		client.close();
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
			System.out.println("Request Sent");
		}
	}

	private IReconHttpCallback clientCallback = new IReconHttpCallback() {
		@Override
		public void onReceive(int requestId, ReconHttpResponse response) {
			System.out.println("Response ready...");
			Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
			
			System.out.println(new String(response.getBody()));
			System.out.println(response.getUrl());
	//		textView.setText(new String(response.getBody()));
		}

		@Override
		public void onError(int requestId, ERROR_TYPE type, String message) {
			Toast.makeText(MainActivity.this, "Error: " + type.toString() + "(" + message + ")", Toast.LENGTH_SHORT).show();
			textView.setText("");
		}

	};
}
