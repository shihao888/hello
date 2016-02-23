package com.example.hello;

import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class MyTimerTask extends TimerTask {
	private Context context;
	
	public MyTimerTask(Context applicationContext) {
		this.context = applicationContext;
		
	}

	@Override
	public void run() {
		boolean isContected;
		if (isNetworkConnected(context) || isWifiConnected(context))  
        {  
            isContected = true;  
        }  
        else  
        {  
            isContected = false;  
        }  
         
        {  
            
            AlertDialog.Builder builder = new Builder(context);
            builder.setMessage("通知网络状态改变:" + isContected).show();
        }  

	}

	private boolean isNetworkConnected(Context context2) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isWifiConnected(Context context2) {
		// TODO Auto-generated method stub
		return false;
	}

}
