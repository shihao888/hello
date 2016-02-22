package com.example.hello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	Button buttonStart, buttonStop; 
	BroadcastReceiver myConnectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				// network unconnected 
			} else {
				// network connected 
			}
		}
	};

	/** 
	 * 对网络连接状态进行判断 
	 * @return  true, 可用； false， 不可用 
	 */  
	private boolean isOpenNetwork() {  
	    ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    }  
	  
	    return false;  
	}  
	/** 
	 * 对网络连接类型进行判断 
	 * @return  true, 可用； false， 不可用 
	 */  
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//设置监听
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(myConnectionReceiver, intentFilter);

		
		//开启
		buttonStart = (Button) findViewById(R.id.buttonstart);  
        buttonStop = (Button) findViewById(R.id.buttonstop);  
  
        buttonStart.setOnClickListener(this);  
        buttonStop.setOnClickListener(this); 

	}
	//取消注册Receiver
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myConnectionReceiver != null) {
			unregisterReceiver(myConnectionReceiver);
		}
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		
		switch (src.getId()) {
		case R.id.buttonstart:
			builder.setMessage("onClick: starting service").show();
			startService(new Intent(this, MyService.class));
			break;
		case R.id.buttonstop:
			builder.setMessage("onClick: stop service").show();
			stopService(new Intent(this, MyService.class));
			break;
		}
	}
	

}
