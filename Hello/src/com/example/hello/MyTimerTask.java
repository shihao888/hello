package com.example.hello;

import java.util.TimerTask;

import com.example.hello.MyService.ConnectState;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class MyTimerTask extends TimerTask {
	private Context context;
	ConnectState connectState;
	public MyTimerTask(Context applicationContext,ConnectState connectState) {
		this.context = applicationContext;
		this.connectState = connectState;
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
        if (connectState != null)  
        {  
            connectState.GetState(isContected); // 通知网络状态改变  
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
