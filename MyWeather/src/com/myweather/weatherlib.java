package com.myweather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reconinstruments.ReconSDK.IReconDataReceiver;
import com.reconinstruments.ReconSDK.ReconDataResult;
import com.reconinstruments.ReconSDK.ReconEvent;
import com.reconinstruments.ReconSDK.ReconLocation;
import com.reconinstruments.ReconSDK.ReconSDKManager;
import com.reconinstruments.webapi.IReconHttpCallback;
import com.reconinstruments.webapi.ReconHttpRequest;
import com.reconinstruments.webapi.ReconHttpResponse;
import com.reconinstruments.webapi.ReconOSHttpClient;

import android.app.Application;
import android.location.Location;
import android.widget.Toast;

public class weatherlib extends Application implements IReconDataReceiver { 
	private static String result;
	private static ReconOSHttpClient client;
	public double latitude;
	public double longitude;
	static String key = "28faca837266a521f823ab10d1a45050";
	
	public String RefreshWeather() {
			client = new ReconOSHttpClient(this, clientCallback);
			Toast.makeText(this, "Reading GPS", Toast.LENGTH_SHORT).show();
			ReconSDKManager mDataManager   = ReconSDKManager.Initialize(null);
			mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
			return result;
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
					System.out.println("Lat:"+latitude+" / long:"+longitude);
					System.out.println("Fetching data...");
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
			} else {
				Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
				System.out.println("Request Sent");
			}
		}

		private static IReconHttpCallback clientCallback = new IReconHttpCallback() {
			@Override
			public void onReceive(int requestId, ReconHttpResponse response) {
				System.out.println("Response ready...");
				String answer=String(response.getBody());
				System.out.println(new String(response.getBody()));
				System.out.println(response.getUrl());
//				textView.setText(new String(response.getBody()));
			}

			private String String(byte[] body) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onError(int requestId, ERROR_TYPE type, String message) {
				System.out.println("Error: " + type.toString() + "(" + message + ")");
			}

		};
	
}
