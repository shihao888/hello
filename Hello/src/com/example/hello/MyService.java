package com.example.hello;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyService extends Service {
	
	private ServiceHandler handler;
	private ConnectivityManager connectivityManager;  
    private NetworkInfo info; 
    
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
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//读写配置文件
	public void writeTime(long time,String sName){
		
		SharedPreferences item = getSharedPreferences(sName,0);
		SharedPreferences.Editor editor = item.edit();
		editor.putString(sName,Long.toString(time));
		editor.commit();
		
	}
	public long readTime(String sName){
		
		SharedPreferences item = getSharedPreferences(sName,0);
		String tmp = item.getString(sName,"0");
		if(tmp==null)return 0l;
		else return Long.parseLong(tmp);
	}
	//得到手机唯一标识
	public String getDevId() {
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		return tm.getDeviceId();		
	}
	public static String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);  
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
	    long seconds = (mss % (1000 * 60)) / 1000;  
	    return days + " days " + hours + " hours " + minutes + " minutes "  
	            + seconds + " seconds ";  
	}  
	//
	private BroadcastReceiver mReceiver = new BroadcastReceiver()  
    {  
       
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();  
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))  
            {  
				connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo(); 
				
					if(info != null && info.isAvailable() && (info.getState() == NetworkInfo.State.CONNECTED)){						
						//开始计时
						Long l = System.currentTimeMillis();
						writeTime(l,"starttime");
						Toast.makeText(getApplicationContext(), info.getTypeName(),Toast.LENGTH_SHORT).show(); 
					}
					else{
						Toast.makeText(getApplicationContext(), "没有可用网络",Toast.LENGTH_SHORT).show();
						//停止计时
						long stoptime = System.currentTimeMillis();						
						long totaltime = readTime("totaltime")+stoptime-readTime("starttime"); 
						writeTime(totaltime,"totaltime"); //记录总时间
						writeTime(stoptime,"starttime");//记录结束时间到开始位置						
					}
				
            }  
		}  
    }; 
    
    private void improvePriority() {  
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,  
	            new Intent(this, MyService.class), 0);  
	    Notification notification = new Notification.Builder(this)  
	            .setContentTitle("Foreground Service")  
	            .setContentText("Foreground Service Started.")  
	            .setSmallIcon(R.drawable.ic_launcher).build();  
	    notification.contentIntent = contentIntent;  
	    startForeground(1, notification);  //0 将不会显示 notification
	} 
	@Override
	public void onCreate() {
		improvePriority();
		
		//注册广播  
        IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action  
        registerReceiver(mReceiver, mFilter); 
        
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
		Toast.makeText(getApplicationContext(), "Service is shutting down...", Toast.LENGTH_SHORT).show();
		super.onDestroy();
		unregisterReceiver(mReceiver); // 删除广播
		
		stopForeground(true); 
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
