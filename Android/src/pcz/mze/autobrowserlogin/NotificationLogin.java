package pcz.mze.autobrowserlogin;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class NotificationLogin extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d("Logging Connection","Notification for Login");
		connectWifiFromOutside();
		finish();
	}
	public void connectWifiFromOutside(){
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		if(!wm.isWifiEnabled()){
			Toast.makeText(getBaseContext(),R.string.Turning_on_wifi,Toast.LENGTH_LONG).show();
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
						Toast.makeText(getBaseContext(),"Connection Established",Toast.LENGTH_LONG).show();
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
