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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	Button buttonLogin, buttonRegister; 	 
	EditText et_mobilenum,et_pwd;
	String userid;
	
	private ProfileUtil profile;
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
		
		//得到唯一et_mobilenum
		profile = new ProfileUtil(this);
		userid=profile.readParam("userid");
		if(null==userid||"N/A".equals(userid)){
			userid = ProfileUtil.getUUID();
			profile.writeParam("userid", userid);
        }		
		
		// 通过 findViewById(id)方法获取用户名和密码控件对象  
		et_mobilenum = (EditText) findViewById(R.id.et_mobilenum);  
        et_pwd = (EditText) findViewById(R.id.et_pwd); 
				
		//开启
		buttonLogin = (Button) findViewById(R.id.buttonlogin);  
        buttonRegister = (Button) findViewById(R.id.buttonregister);  
  
        buttonLogin.setOnClickListener(this);  
        buttonRegister.setOnClickListener(this); 

	}
	
	boolean isServiceStarted = false;
	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
				
		switch (src.getId()) {
		case R.id.buttonlogin:
			// 获取用户手机号  
            final String mobilenum = et_mobilenum.getText().toString();  
            // 获取用户密码
            final String pwd = et_pwd.getText().toString(); 
            
            if (TextUtils.isEmpty(mobilenum) || TextUtils.isEmpty(pwd)) {  
                Toast.makeText(this, "手机号和密码都不能为空,调查服务没有启动！", Toast.LENGTH_LONG).show();
                return;
            }else{
            	profile.writeParam("username",mobilenum);
            	profile.writeParam("stuid",pwd);
            }
            
			if (!isServiceStarted) {			
				Intent intent = new Intent(this, MyService.class);		     
			    if (startService(intent) == null) {
			    	Toast.makeText(getApplicationContext(), "无法启动！" ,Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    isServiceStarted = true;
			    //
			}else{
				Toast.makeText(getApplicationContext(), "服务已启动！" ,Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		case R.id.buttonregister:
			Intent intent = new Intent(); 
			intent.setClass(this,RegisterActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//http://blog.csdn.net/sxsj333/article/details/6639812
			startActivity(intent);
			setTitle("用户注册");
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
			String s="版本version 6 \n"+"用户id:"+profile.readParam("userid");
			AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器 
			builder.setMessage(s);
			builder.create().show();			
			return true;
		}
		if (id == R.id.chkmyservice) {
			String s = "";
			if(isServiceRunning("com.example.hello.MyService"))s="后台服务正在运行......";
			else s="后台服务已经停止!!!";
			Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
			Long total = profile.readTime("totaltime");
			Long start = profile.readTime("starttime");
			Long stop = profile.readTime("stoptime");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			Date date1 = new Date(start);Date date2 = new Date(stop);
			String sTotal = formatDuring(total);
			String sStart = formatter.format(date1);
			String sStop= formatter.format(date2);
			s="total="+sTotal+" starttime="+sStart+" stoptime="+sStop;
			AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器 
			builder.setMessage(s);
			builder.create().show(); 
			
			return true;
		}
		if (id == R.id.cleartotal) {
			profile.writeTime(0,"totaltime");
		}
		if (id == R.id.stopservice) {
			if (isServiceStarted) {		
				stopService(new Intent(this, MyService.class));
				isServiceStarted = false;
			}else{
				Toast.makeText(getApplicationContext(), "服务已停止！" ,Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	public static String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);  
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
	    long seconds = (mss % (1000 * 60)) / 1000;  
	    return days + " days " + hours + " hours " + minutes + " minutes "  
	            + seconds + " seconds ";  
	} 
	
	
}
