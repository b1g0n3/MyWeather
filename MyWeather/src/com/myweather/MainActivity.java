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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

	    CustomAsyncTask myCustomTask = new CustomAsyncTask(this);
		// Execution de la tâche, on lui passe le paramètre à traiter
		// Execute the task, we give it the parameter to work with
		myCustomTask.execute("Params to execute in background");

		
   }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		client.close();
	}

    public class CustomAsyncTask extends AsyncTask<String, Integer, Integer>{
    	private Activity  mContext;
    	private ProgressBar pbAsyncBackgroundTraitement;
     
    	public CustomAsyncTask(Activity  mContext) {
    		super();
    		this.mContext = mContext;
    	}
     
    	/* fonction qui est appelé en première */
    	/* function which is called first */
    	@Override
    	protected void onPreExecute() {
    		this.pbAsyncBackgroundTraitement = (ProgressBar) mContext.findViewById(R.id.pbAsyncBackgroundTraitement);
    		this.pbAsyncBackgroundTraitement.setVisibility(View.VISIBLE);
    		super.onPreExecute();
    	}
     
    	/* fonction qui fait le traitement de fond */
    	/* function which do work in background */
    	@Override
    	protected Integer doInBackground(String... params) {
    		// Ici on fait un traitement inutile, à vous de placer ce que vous avez à faire 
    		// Here we do useless work, you have to put here what you have to do

    		while (true) {
//    		for (int i = 0; i< params[0].length(); i++){
//    			try {
	    		final ReconSDKManager mDataManager   = ReconSDKManager.Initialize(MainActivity.this);
	    		mDataManager.receiveData(MainActivity.this, ReconEvent.TYPE_LOCATION);
	    		textView.setText("Lat:"+latitude+" / long:"+longitude);
	    		if (latitude!=0) { return 0; }
	    		
//  				Thread.sleep(100);
//    			} catch (InterruptedException e) {
//  				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//    			int progress = (i * 100 / params[0].length());
//    			Log.i("test", " progress : " + progress);
    			// Cette fonction appelle la fonction onProgressUpdate()
    			// This function call onProgressUpdate()
//    			publishProgress(progress);
    		}
    	}
     
    	/* fonction qui met à jour la progress bar */
    	/* function which set progress update */
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// On met à jour la progress bar
    		// We update the progress bar
    		this.pbAsyncBackgroundTraitement.setProgress(values[0]);
    		super.onProgressUpdate(values);
    	}
    	/* fonction qui est appelé après que le traitement effectué en tache de fond est terminé */
    	/* function which is called after work on background function is over */
    	@Override
    	protected void onPostExecute(Integer result) {
    		// Le traitement est terminé, vous pouvez faire une action comme ouvrir une nouvelle activity ou cacher des composant de l'UI
    		// The work is done, you can do an action like create an activity or set invisible some UI components
    		this.pbAsyncBackgroundTraitement.setVisibility(View.GONE);
    		Toast.makeText(this.mContext, "Work is over... Do what you want now...", Toast.LENGTH_LONG).show();    		
    		client = new ReconOSHttpClient(MainActivity.this, clientCallback);
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
    			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    		}

    		
    		super.onPostExecute(result);
    	}
     
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
