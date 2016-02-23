package com.example.hello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	Button buttonStart, buttonStop; 	 
  
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
		
				
		//开启
		buttonStart = (Button) findViewById(R.id.buttonstart);  
        buttonStop = (Button) findViewById(R.id.buttonstop);  
  
        buttonStart.setOnClickListener(this);  
        buttonStop.setOnClickListener(this); 

	}
	/*
	 * 服务不能自己运行，需要通过调用Context.startService()或Context.bindService()方法启动服务。
	 * 这两个方法都可以启动Service，但是它们的使用场合有所不同。使用startService()方法启用服务，
	 * 调用者与服务之间没有关连，即使调用者退出了，服务仍然运行。
	 * 使用bindService()方法启用服务，调用者与服务绑定在了一起，调用者一旦退出，服务也就终止，
	 * 大有“不求同时生，必须同时死”的特点。
	 */
	boolean isServiceStarted = false;
	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		
		switch (src.getId()) {
		case R.id.buttonstart:
			builder.setMessage("onClick: starting service").show();			
			
			Intent intent = new Intent(this, MyService.class);
		    Bundle bundle = new Bundle();
		    bundle.putInt("MESSAGE_TYPE", MyService.MSG_HELLO);
		    intent.putExtras(bundle);
		    
		    if (startService(intent) == null) return;
		    isServiceStarted = true;
		    
			break;
		case R.id.buttonstop:
			builder.setMessage("onClick: stop service").show();
			stopService(new Intent(this, MyService.class));
			if (isServiceStarted) {
				Intent intent1 = new Intent(this, MyService.class);
				Bundle bundle1 = new Bundle();
				bundle1.putInt("MESSAGE_TYPE", MyService.MSG_STOP_SERVICE);
				intent1.putExtras(bundle1);
				startService(intent1);
				isServiceStarted = false;
			}
			break;
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
}
