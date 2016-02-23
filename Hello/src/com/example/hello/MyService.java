package com.example.hello;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class MyService extends Service {
	//http://www.codeproject.com/Articles/822717/Beginner-s-Guide-to-Android-Services
	// Message types to the service to display a message
	static final int MSG_STOP_SERVICE = 0;
	static final int MSG_HELLO = 1;
	static final int MSG_HAPPY_BIRTHDAY = 2;
	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
	    
	    public ServiceHandler(Looper looper) {
	        super(looper);
	    }
	    
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            case MSG_STOP_SERVICE:
	                Toast.makeText(getApplicationContext(), "Service is shutting down...", Toast.LENGTH_SHORT).show();
	                stopSelf(msg.arg1);
	                break;
	            case MSG_HELLO:
	                Toast.makeText(getApplicationContext(), "Hello, Code Project! Greeting from Android Service.", Toast.LENGTH_SHORT).show();
	                break;
	            case MSG_HAPPY_BIRTHDAY:
	                Toast.makeText(getApplicationContext(), "Happy Birthday to you!", Toast.LENGTH_SHORT).show();
	                break;
	            default:
	                super.handleMessage(msg);
	        }
	    }
	}
	private ServiceHandler handler;
	
	
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private BroadcastReceiver mReceiver = new BroadcastReceiver()  
    {  
       
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();  
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))  
            {  
                //Timer timer = new Timer();  
                //timer.schedule(new MyTimerTask(getApplicationContext(),connectState), new Date());
            	Toast.makeText(getApplicationContext(), "Hello, ConnectivityManager.CONNECTIVITY_ACTION! Greeting from Android Service.", Toast.LENGTH_SHORT).show();
           
            }  
		}  
    }; 
    
   
	@Override
	public void onCreate() {
		// 注册广播  
        IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action  
        registerReceiver(mReceiver, mFilter); 
        //
        Toast.makeText(getApplicationContext(), "Service is starting...", Toast.LENGTH_SHORT).show();

        HandlerThread thread = new HandlerThread("StartedService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        
        // Get the HandlerThread's Looper and use it for our Handler
        Looper looper = thread.getLooper();
        handler = new ServiceHandler(looper);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver); // 删除广播
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = handler.obtainMessage();
	    msg.arg1 = startId;
	    msg.what = intent.getExtras().getInt("MESSAGE_TYPE");

	    handler.sendMessage(msg);

	    // Restart the service if it got killed
	    return START_REDELIVER_INTENT;
	}
	
}
