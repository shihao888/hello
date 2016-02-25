package com.example.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

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
	
	boolean isServiceStarted = false;
	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
				
		switch (src.getId()) {
		case R.id.buttonstart:
			if (!isServiceStarted) {			
				Intent intent = new Intent(this, MyService.class);		     
			    if (startService(intent) == null) {
			    	Toast.makeText(getApplicationContext(), "无法启动！" ,Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    isServiceStarted = true;
			}else{
				Toast.makeText(getApplicationContext(), "服务已启动！" ,Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		case R.id.buttonstop:
			
			if (isServiceStarted) {		
				stopService(new Intent(this, MyService.class));
				isServiceStarted = false;
			}else{
				Toast.makeText(getApplicationContext(), "服务已停止！" ,Toast.LENGTH_SHORT).show();
				return;
			}
			break;
			
			
		}
	}
	private boolean isServiceRunning(String serviceName) {//"com.example.MyService"
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceName.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
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
			Toast.makeText(getApplicationContext(), "版本version 5", Toast.LENGTH_SHORT).show();
			return true;
		}
		if (id == R.id.chkmyservice) {
			String s = "";
			if(isServiceRunning("com.example.hello.MyService"))s="后台服务正在运行......";
			else s="后台服务已经停止!!!";
			Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
			Long total = readTime("totaltime");
			Long start = readTime("starttime");
			Long stop = readTime("stoptime");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			Date date1 = new Date(total);Date date2 = new Date(start);Date date3 = new Date(stop);
			String sTotal = formatter.format(date1);
			String sStart = formatter.format(date2);
			String sStop= formatter.format(date3);
			s="total="+sTotal+" starttime="+sStart+" stoptime="+sStop;
			AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器 
			builder.setMessage(s);
			builder.create().show(); 
			
			return true;
		}
		if (id == R.id.cleartotal) {
			writeTime(0,"totaltime");
		}
		return super.onOptionsItemSelected(item);
	}
	public long readTime(String sName){
		
		SharedPreferences item = getSharedPreferences(sName,0);
		String tmp = item.getString(sName,"0");
		if(tmp==null)return 0l;
		else return Long.parseLong(tmp);
	}
	public void writeTime(long time,String sName){
		
		SharedPreferences item = getSharedPreferences(sName,0);
		SharedPreferences.Editor editor = item.edit();
		editor.putString(sName,Long.toString(time));
		editor.commit();
		
	}
}
