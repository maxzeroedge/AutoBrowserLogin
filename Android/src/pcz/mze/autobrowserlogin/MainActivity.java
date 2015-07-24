package pcz.mze.autobrowserlogin;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	NotificationCompat.Builder mNotification;
    boolean notificationOn = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {		
        	
			final TextView status = (TextView) findViewById(R.id.textView2);
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connectWifi();//new WifiState().
				//status.setText("Logged and Loaded");
			}});
        
        final Button btn2 = (Button) findViewById(R.id.button2);
        mNotification = 
        		new NotificationCompat.Builder(this)
        		.setSmallIcon(R.drawable.ic_launcher)
        		.setContentTitle("AutoBrowserLogin")
        		.setContentText("Click here to login to the network IIT")
        		.setTicker("Notification Initiated")
        		.setOngoing(true)
        		.setAutoCancel(false);
        Intent resultIntent = new Intent(this, NotificationLogin.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingResultIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setContentIntent(pendingResultIntent);
        btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent serviceIntent = new Intent(getBaseContext(), LoginHeadService.class);
				if(isMyServiceRunning(LoginHeadService.class)){
					stopService(serviceIntent);
				}
				else{
					startService(serviceIntent);
				}*/
				NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				if(!notificationOn){
					mNotifyMgr.notify(001, mNotification.build());
					notificationOn = true;
				}
				else{
					mNotifyMgr.cancelAll();
					notificationOn = false;
				}
			}
			
		});
        /*if(isMyServiceRunning(LoginHeadService.class)){
        	btn2.setText("Stop Service");
        }
        else{
        	btn2.setText("Start Service");
        }*/
    }
    
    private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
        	Toast.makeText(getBaseContext(), "Created by Palashmax (palashmax@outlook.com)", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void connectWifi(){
    	final TextView status = (TextView) findViewById(R.id.textView2);
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		if(!wm.isWifiEnabled()){
			Toast.makeText(getBaseContext(),R.string.Turning_on_wifi,Toast.LENGTH_LONG).show();
			status.setText(R.string.Turning_on_wifi);
			wm.setWifiEnabled(true);
		}
		if(!checkWifiConnected()){
			WifiConfiguration wc = new WifiConfiguration();
			wc.SSID="\"IIT\"";
			wm.addNetwork(wc);
			try{
				List<WifiConfiguration> list = wm.getConfiguredNetworks();
				if(list.isEmpty()){Log.e("Empty List","No Wifi Configured or Saved");}
				for( WifiConfiguration i : list ) {
				    if(i.SSID != null && i.SSID.equals("\"IIT\"")) {
				         wm.disconnect();
				         wm.enableNetwork(i.networkId, true);
				         wm.reconnect();          
				         break;
				    }           
				 }
			}
			catch(Exception e){
				Toast.makeText(getBaseContext(),e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
			}
		}
		Thread t = new Thread() {
	        @Override
	        public void run() {
	            try {
	            	Looper.prepare();
	                    //check if connected!
	                if(!checkWifiConnected()){
	                	//Toast.makeText(getBaseContext(),"Connected to the Wifi Network",Toast.LENGTH_LONG).show();
	                    Log.d("WIFIWorker", "Working");
	                	while (!checkWifiConnected()) {
		                    //Wait to connect
		                    Thread.sleep(100);
		                }
	                }
					try{
						new Connection().execute();
						//Toast.makeText(getBaseContext(),"Connection Established",Toast.LENGTH_LONG).show();
	                	status.setText("Logged and Loaded");
					} catch(Exception e){
						Toast.makeText(getBaseContext(),e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
						Log.e("WifiWorker", e.getStackTrace().toString());
					}
	            } catch (Exception e) {
	            	Toast.makeText(getBaseContext(),e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
	            	Log.e("WifiWorker", e.getStackTrace().toString());
	            }
	        }
	    };
	    t.start();
	}
	
	public boolean checkWifiConnected(){
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mWifi.isConnected()){
				return false;
		}
		return true;
	}
}

final class Connection extends AsyncTask<String,Long,Integer>{
	
	protected Integer doInBackground(String... urls){
		try{
			new AutoBrowserLogin().fun2();
		}
		catch(Exception e){
			Log.d("Error", e.getStackTrace().toString());
		}
		return null;
	}
	
	protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }
}
