package com.myweather;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIOHourly;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IReconDataReceiver { 
	
	TextView mCurrentTemp;
    public boolean first;
	private TextView textView, textcondition,temperature,textressentie,textwind,textpress,texthumid,textozone,texttendance;
	private ImageView iconimage;
	public static String result;
	private static ReconOSHttpClient client;
	public double latitude,oldLatitude;
	public double longitude,oldLongitude;
	static String key = "28faca837266a521f823ab10d1a45050";
    public int testByte,pass;
    String language,unit,vitesse;
    String PreviousResult,temp,statusline;
	private String feel,press,wind,humid,un,tend;
    
    
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
		textwind = (TextView) findViewById(R.id.textwind);
		textpress = (TextView) findViewById(R.id.textpress);
		texthumid = (TextView) findViewById(R.id.texthumid);
		textozone = (TextView) findViewById(R.id.textozone);
		texttendance = (TextView) findViewById(R.id.texttendance);
    	iconimage = (ImageView) findViewById(R.id.icon);
	    findViewById(R.id.button_refresh);
	    statusline="(last source : ";
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
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("latitude", String.valueOf(oldLatitude) );
		editor.putString("longitude", String.valueOf(oldLongitude));
		editor.putString("Language", String.valueOf(language) );
		editor.putString("Unit", String.valueOf(unit));
		editor.apply();
		super.onPause();
	}

	
    @Override
	protected void onDestroy() {
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("latitude", String.valueOf(latitude) );
		editor.putString("longitude", String.valueOf(longitude));
		editor.putString("Language", String.valueOf(language) );
		editor.putString("Unit", String.valueOf(unit));
		editor.apply();
    	super.onDestroy();
	}
    
    @Override
	protected void onResume() {
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	PreviousResult = sharedpreferences.getString("PreviousResult", "");
    	language = sharedpreferences.getString("Language", "en");
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
    		new FIOHourly(fio);
    		String icon =  currently.get().getByKey("icon").replace("\"", "");
    		String icon1 = "@drawable/"+icon.replace("-", "_");
    		Resources res = getResources();
    		int resourceId = res.getIdentifier(
    		   icon1, "drawable", getPackageName() );
    		iconimage.setImageResource( resourceId );
    		setTitle("MyWeather : currently");
    		if (language=="en") {
    			feel = getString(R.string.feellike_en);
    			press = getString(R.string.pressure_en);
    			wind = getString(R.string.wind_en);
    			humid = getString(R.string.humid_en);
    			vitesse = "mph";
    			tend=getString(R.string.tend_en);
    		} else {
    			feel = getString(R.string.feellike_fr);
    			press = getString(R.string.pressure_fr);
    			wind = getString(R.string.wind_fr);
    			humid = getString(R.string.humid_fr);
    			vitesse = "kmh";
    			tend=getString(R.string.tend_fr);
    		}
    		
    	    System.out.println("\nCurrently\n");
    	    String [] f  = currently.get().getFieldsArray();
    	    for(int i = 0; i<f.length;i++)
    	        System.out.println(f[i]+": "+currently.get().getByKey(f[i]));
    	    
			String dir=headingToString2(Integer.valueOf(currently.get().getByKey("windBearing")));
    		temperature.setText(DoubleToI(currently.get().getByKey("temperature"))+"�");
    		textressentie.setText(feel+" "+DoubleToI(currently.get().getByKey("apparentTemperature"))+"�");
    		textcondition.setText(currently.get().getByKey("summary").replace("\"", ""));
    		textpress.setText(press+" "+DoubleToI(currently.get().getByKey("pressure"))+" mb");    		
    		textwind.setText(wind+" "+DoubleToI(currently.get().getByKey("windSpeed"))+" "+vitesse+" ("+dir+")");
    		texthumid.setText(humid+" "+DoubleToP(currently.get().getByKey("humidity"))+"%");
    		textozone.setText("Ozone : "+DoubleToI(currently.get().getByKey("ozone"))+"");
    		textView.setText(statusline+currently.get().getByKey("time")+")");
    		String substr=data.substring(data.indexOf("hourly\":{\"")+20);
    		substr=substr.substring(0, substr.indexOf("\""));
    		texttendance.setText(tend+" "+substr);
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
		pass=1;
		ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
		mDataManager.receiveData(this, ReconEvent.TYPE_LOCATION);
		mDataManager.unregisterListener(ReconEvent.TYPE_LOCATION);
		textView.setText("Waiting for GPS fix...");
		System.out.println("Waiting for GPS fix...");
	}
		
		public void onReceiveCompleted(int status, ReconDataResult result)
		{
			if (pass==1) {
				
			    if (status != ReconSDKManager.STATUS_OK)
			    {
			        System.out.println("Communication Failure with Transcend Service");
			        return;
			    }
			    pass++;
			    ReconLocation rloc = (ReconLocation)result.arrItems.get(0);
			    Location loc = rloc.GetLocation();
			    rloc.GetPreviousLocation();
			    if (loc != ReconLocation.INVALID_LOCATION)
			    {
			        latitude=loc.getLatitude();
			        longitude=loc.getLongitude();
			        
			        System.out.println("Lat trouv�e:"+latitude+" / long:"+longitude);
					oldLatitude=latitude; oldLongitude=longitude;
					statusline="(last refresh:";
			    } else {
			    	statusline="No GPS. Previous location (last refresh:";
			    	latitude=oldLatitude; longitude=oldLongitude;
			    	//		        
			    	//	remplacement de la localisation pour test	        
			    	//		        
			    	//latitude=50.647392; longitude=3.130481; // my home
			    	//latitude=45.092624; longitude=6.068348; // alpe d'huez
			        latitude=45.125263; longitude=6.127609; // Pic Blanc
			        //latitude=41.919229; longitude=8.738635; //Ajaccio
			        //latitude=46.192683; longitude=48.205964; //Russie
			        //latitude=49.168602; longitude=25.351872; //bulgarie
			        //latitude=36.752887; longitude=3.042048; //alger

			    }	
				System.out.println("Fetching data...");
		        textView.setText("Fetching data...");
				try {
					if (unit=="F") { un = "us"; } else { un = "ca"; }
					URL url = new URL("https://api.forecast.io/forecast/"+key+"/"+latitude+","+longitude+"?lang="+language+"&units="+un);
					new HashMap<String, List<String>>();			
					sendRequest(new ReconHttpRequest("GET", url, null , null));

				} catch (MalformedURLException e) {
					System.out.println("MalformedURLException...");
				}
			}
		}

		@Override
		public void onFullUpdateCompleted(int arg0, ArrayList<ReconDataResult> arg1) {
			// TODO Auto-generated method stub
			System.out.println("boucleX");
		}
		
		public String DoubleToP(String sourceDouble) {
    		DecimalFormat df = new DecimalFormat("#");    		
    		double db=Double.valueOf(sourceDouble);
			return df.format(db*100);
		}

		public String DoubleToI(String sourceDouble) {
    		DecimalFormat df = new DecimalFormat("#");    		
    		double db=Double.valueOf(sourceDouble);
			return df.format(db);
		}

		
		public void sendRequest(ReconHttpRequest request) {
			if (-1 == client.sendRequest(request)) {
				System.out.println("HUD not connected - No Internet");
		        textView.setText("No Internet");
		        statusline="No Internet (last data:";
				onDisplay(PreviousResult);
			} else {
				System.out.println("Request Sent");
			}
		}

		private IReconHttpCallback clientCallback = new IReconHttpCallback() {
			@Override
			public void onReceive(int requestId, ReconHttpResponse response) {
				textView.setText("Data received...");
				result=new String(response.getBody());
				PreviousResult=result;
				SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("PreviousResult",PreviousResult);
				editor.apply();
				oldLatitude=latitude; oldLongitude=longitude;
				System.out.println("Displaying data...");
				onDisplay(result);
			}
			
			@Override
			public void onError(int requestId, ERROR_TYPE type, String message) {
				System.out.println("Error: " + type.toString() + "(" + message + ")");
			}
		};

	    public static String headingToString2(double x)
	    {
	        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
	        return directions[ (int)Math.round((  ((double)x % 360) / 45)) ];
	    }
}
