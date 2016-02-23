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
	MyService receiveMsgService;  
     
    // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等，所以需要一个标志记录当前状态 
	private boolean conncetState = true;
	
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
		Intent intent = new Intent(MainActivity.this, MyService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);  

		
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
		if (receiveMsgService != null) {
			unbindService(serviceConnection);
			Log.i("mylog", "执行unbind()");
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
