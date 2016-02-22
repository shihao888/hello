package com.example.hello;

import java.util.Date;
import java.util.Timer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

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
                Timer timer = new Timer();  
                timer.schedule(new MyTimerTask(getApplicationContext(),connectState), new Date());  
            }  
		}  
    }; 
    
    public interface ConnectState  
    {          
        // 网络状态改变之后，通过此接口的实例通知当前网络的状态，此接口在Activity中注入实例对象  
    	public void GetState(boolean isConnected); 
    }  
  
    private ConnectState connectState;  
  
    public void setConnectState(ConnectState connectState)  
    {  
        this.connectState = connectState;  
    }  
  
    private Binder binder = new MyBinder();  
    private boolean isContected = true; 
	@Override
	public void onCreate() {
		// 注册广播  
        IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action  
        registerReceiver(mReceiver, mFilter); 
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver); // 删除广播
	}
	public class MyBinder extends Binder  
    {  
        public MyService getService()  
        {  
            return MyService.this;  
        }  
    } 
}
