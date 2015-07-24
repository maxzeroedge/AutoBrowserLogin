package pcz.mze.autobrowserlogin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("RtlHardcoded") public class LoginHeadService extends Service {

	  private WindowManager windowManager;
	  private ImageView chatHead;
	  long lastPressTime;
	  PopupWindow popup;

	  @Override 
	  public IBinder onBind(Intent intent) {
	    // Not used
	    return null;
	  }

	  @Override 
	  public void onCreate() {
	    super.onCreate();

	    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

	    chatHead = new ImageView(this);
	    chatHead.setImageResource(R.drawable.ic_launcher);

	    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.TYPE_PHONE,
	        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	        PixelFormat.TRANSLUCENT);

	    params.gravity = Gravity.TOP | Gravity.LEFT;
	    params.x = 0;
	    params.y = 100;

	    windowManager.addView(chatHead, params);
	    
	    chatHead.setOnTouchListener(new View.OnTouchListener() {
	    	  private int initialX;
	    	  private int initialY;
	    	  private float initialTouchX;
	    	  private float initialTouchY;

	    	  @SuppressLint("ClickableViewAccessibility") @Override 
	    	  public boolean onTouch(View v, MotionEvent event) {
		    	  Log.d("TouchDetector", "Touch Detected");
	    	    switch (event.getAction()) {
	    	      case MotionEvent.ACTION_DOWN:
	    	        initialX = params.x;
	    	        initialY = params.y;
	    	        initialTouchX = event.getRawX();
	    	        initialTouchY = event.getRawY();
	    	        return true;
	    	      case MotionEvent.ACTION_UP:
	    	        return true;
	    	      case MotionEvent.ACTION_MOVE:
	    	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
	    	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
	    	        windowManager.updateViewLayout(chatHead, params);
	    	        return true;
	    	      default:
	    	    	/*if(!popup.isShowing()){
	  					popup=dispPopup(R.layout.activity_main);
	  				}
	  				else{
	  					popup.dismiss();
	  				}*/
	  				final Button btnc = (Button) new MainActivity().findViewById(R.id.button1);
    	    		btnc.performClick();
	      	    	Toast.makeText(getBaseContext(),"Connecting", Toast.LENGTH_LONG).show();
	      	    	Log.d("WifiWorker", "Connecting");
	    	    }
	    	    return false;
	    	  }
	    });
	    
	    chatHead.setOnClickListener(new View.OnClickListener() {
			long pressTime = System.currentTimeMillis();
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
  	    		//getBaseContext().startActivity(myIntent);
    	    	//final Button btnc = (Button) new MainActivity().findViewById(R.id.button1);
    	    	//btnc.performClick();
				if(!popup.isShowing()){
					popup=dispPopup(R.layout.activity_main);
				}
				else{
					popup.dismiss();
				}
    	    	Toast.makeText(getBaseContext(),"Connecting", Toast.LENGTH_LONG).show();
    	    	Log.d("WifiWorker", "Connecting");
			}
		});

	  }

	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	    if (chatHead != null) windowManager.removeView(chatHead);
	  }
	  
	  private PopupWindow dispPopup(int contentView){
		  Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		  PopupWindow popupWindow = new PopupWindow(this);
		  LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		  popupWindow.setContentView(inflater.inflate(contentView, null));
		  Point size = new Point();
		  display.getSize(size);
		  popupWindow.setWidth(size.x);
		  popupWindow.setHeight((int)((size.y)*0.8));
		  popupWindow.showAsDropDown(chatHead);
		  return popupWindow;
	  }
	  
}