package com.myweather;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myweather.MyListener;
import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.reconinstruments.ReconSDK.*;
import com.reconinstruments.webapi.IReconHttpCallback;
import com.reconinstruments.webapi.ReconHttpRequest;
import com.reconinstruments.webapi.ReconHttpResponse;
import com.reconinstruments.webapi.ReconOSHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver { 
	
	TextView mCurrentTemp;
    private TextView mStatus;
    public boolean first;
	private TextView textView, textcondition,temperature,textressentie;
	private ImageView iconimage;
	public static String result;
	private static ReconOSHttpClient client;
	public double latitude,oldLatitude;
	public double longitude,oldLongitude;
	static String key = "28faca837266a521f823ab10d1a45050";
    private MyListener mListener;
    public int testByte;
    String language,unit;
    String PreviousResult,temp;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		textView = (TextView) findViewById(R.id.status);
		textcondition = (TextView) findViewById(R.id.condition);
		temperature = (TextView) findViewById(R.id.Temperature);
		textressentie = (TextView) findViewById(R.id.textressentie);
    	iconimage = (ImageView) findViewById(R.id.icon);
		
	    final Button button_refresh = (Button) findViewById(R.id.button_refresh);
//    	doRefresh();
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Keydown: ("+keyCode+")");
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_DPAD_DOWN :
	        {
	        	startActivity(new Intent(MainActivity.this, SettingsActivity.class));
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	break;
	        }

	        case KeyEvent.KEYCODE_DPAD_UP :
	        {
	        	startActivity(new Intent(MainActivity.this, HoursActivity.class));
	        	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	        	break;
	        }

	        case KeyEvent.KEYCODE_DPAD_CENTER :
	        {
	        	doRefresh();
	        	break;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("latitude", String.valueOf(oldLatitude) );
		editor.putString("longitude", String.valueOf(oldLongitude));
		editor.putString("Language", String.valueOf(language) );
		editor.putString("Unit", String.valueOf(unit));
		editor.apply();
//    	System.out.println("(Main onPause) Jecris values:"+oldLatitude+" / "+oldLongitude+" / "+language+" / "+unit);
		super.onPause();
	}

	
    @Override
	protected void onDestroy() {
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("latitude", String.valueOf(latitude) );
		editor.putString("longitude", String.valueOf(longitude));
		editor.putString("Language", String.valueOf(language) );
		editor.putString("Unit", String.valueOf(unit));
		editor.apply();
    	System.out.println("(Main onDestroy) Jecris values:"+oldLatitude+" / "+oldLongitude+" / "+language+" / "+unit);
    	super.onDestroy();
	}
    
    @Override
	protected void onResume() {
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	PreviousResult = sharedpreferences.getString("PreviousResult", "");
    	PreviousResult = "";
    	language = sharedpreferences.getString("Language", "Eng");
    	unit = sharedpreferences.getString("Unit", "F");
    	temp = sharedpreferences.getString("latitude", "0");
    	oldLatitude = Double.valueOf(temp);
    	temp = sharedpreferences.getString("longitude", "0");
    	oldLongitude = Double.valueOf(temp);
    	System.out.println("(Main onResume) Je lis values:"+oldLatitude+" / "+oldLongitude+" / "+language+" / "+unit+" / >"+PreviousResult+"<");
	    LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.scroll2);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.RIGHT, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
    	onDisplay(PreviousResult);
		super.onResume();
    }
    
    private void onDisplay(String data) {
    	if (data !=null & data!="") {
    		ForecastIO fio = new ForecastIO(key);
    		fio.getForecast(data);
    		FIOCurrently currently = new FIOCurrently(fio);
    	    //Print currently data
    		System.out.println("\nCurrently\n");
    		String [] f  = currently.get().getFieldsArray();
    		for(int i = 0; i<f.length;i++)
    			System.out.println(f[i]+": "+currently.get().getByKey(f[i]));
    		String icon =  currently.get().getByKey("icon").replace("\"", "");
    		String icon1 = "@drawable/"+icon.replace("-", "_");
//    		System.out.println("icon1="+icon1);
    		Resources res = getResources();
    		int resourceId = res.getIdentifier(
    		   icon1, "drawable", getPackageName() );
    		iconimage.setImageResource( resourceId );
    		setTitle("MyWeather : currently");

//			textView.setText("lastCheck "+currently.get().getByKey("time"));
    		
    		if (unit=="F") {
    			temperature.setText(DoubleToF(currently.get().getByKey("temperature"))+"°");
    		} else { 
    			temperature.setText(DoubleToC(currently.get().getByKey("temperature"))+"°");
    		}
    		if (unit=="F") {
    			textressentie.setText("Feels like "+DoubleToF(currently.get().getByKey("apparentTemperature"))+"°"); 
    		} else { 
    			textressentie.setText("Feels like "+DoubleToC(currently.get().getByKey("apparentTemperature"))+"°");
    		}
    		textcondition.setText(currently.get().getByKey("summary").replace("\"", ""));
    		
    	} else {
    		String icon1 = "@drawable/unknown";
    		Resources res = getResources();
    		int resourceId = res.getIdentifier(
    		   icon1, "drawable", getPackageName() );
    		iconimage.setImageResource( resourceId );
    		System.out.println("nothing to display or bad json...");
    		System.out.println("data="+data);    		
	        textView.setText("nothing to display...");
    	}
    }
    
////////////////////////////////////////////////////
    
	private void doRefresh() {
		result="";
		client = new ReconOSHttpClient(this, clientCallback);
		Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
		ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		textView.setText("Waiting for GPS fix...");
		System.out.println("Waiting for GPS fix...");
	}
		
		public void onReceiveCompleted(int status, ReconDataResult result)
		{
			    if (status != ReconSDKManager.STATUS_OK)
			    {
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
			    	//		        
			    	//	remplacement de la localisation pour test	        
			    	//		        
			    	// alpe d'huez
			    	//latitude=45.092624;
			    	//longitude=6.068348;
			        
			        // Pic Blanc
			        // latitude=45.125263;
			        // longitude=6.127609;
			    	
			        //Ajaccio
			        //latitude=41.919229;
			        //longitude=8.738635;
			        
			        //Russie
			        //latitude=46.192683;
			        //longitude=48.205964;

			        //bulgarie
			        //latitude=49.168602;
			        //longitude=25.351872;
			        
			        //alger
			        //latitude=36.752887;
			        //longitude=3.042048;
			        
			        System.out.println("Lat:"+latitude+" / long:"+longitude);
					oldLatitude=latitude; oldLongitude=longitude;
					System.out.println("Fetching data...");
			        textView.setText("Fetching data...");
			        
					try {
						URL url = new URL("https://api.forecast.io/forecast/28faca837266a521f823ab10d1a45050/"+latitude+","+longitude);
						Map<String, List<String>> headers = new HashMap<String, List<String>>();			
						try {
							byte[] body = "".getBytes("lang=fr");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendRequest(new ReconHttpRequest("GET", url, null , null));

					} catch (MalformedURLException e) {
						System.out.println("MalformedURLException...");
					}
			    }
			    else
			    {
			        System.out.println("No GPS Fix");
//					System.out.println("Displaying old data...");
			        textView.setText("No GPS Fix");
					onDisplay(PreviousResult);
			    }
		}

		@Override
		public void onFullUpdateCompleted(int arg0, ArrayList<ReconDataResult> arg1) {
			// TODO Auto-generated method stub
			System.out.println("boucleX");
		}
		
		public String DoubleToC(String sourceDouble) {
    		DecimalFormat df = new DecimalFormat("#");    		
    		double db=Double.valueOf(sourceDouble);
 //   		double i = (db-32)/1.8;
			return df.format((db-32)/1.8);
		}

		public String DoubleToF(String sourceDouble) {
    		DecimalFormat df = new DecimalFormat("#");    		
    		double db=Double.valueOf(sourceDouble);
			return df.format(db);
		}

		
		public void sendRequest(ReconHttpRequest request) {
			if (-1 == client.sendRequest(request)) {
				System.out.println("HUD not connected - No Internet");
				System.out.println("Displaying old data...");
		        textView.setText("No Internet");
				onDisplay(PreviousResult);
			} else {
				System.out.println("Request Sent");
			}
		}

		private IReconHttpCallback clientCallback = new IReconHttpCallback() {
			@Override
			public void onReceive(int requestId, ReconHttpResponse response) {
				System.out.println("Response ready...");
				textView.setText("Data received...");
				System.out.println("return to main...");
				result=new String(response.getBody());
				PreviousResult=result;
				SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("PreviousResult",PreviousResult);
				editor.apply();
				oldLatitude=latitude; oldLongitude=longitude;
				System.out.println("Displaying data...");
//		        textView.setText("Displaying data...");
				onDisplay(result);
			}
			
			@Override
			public void onError(int requestId, ERROR_TYPE type, String message) {
				System.out.println("Error: " + type.toString() + "(" + message + ")");
			}
		};

		
}
