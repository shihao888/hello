package com.example.hello;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{
	Button buttonOK, buttonCancel; 
	private ProfileUtil profile;
	String mobilenum,pwd1,pwd2,realname,stuid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		profile = new ProfileUtil(this);
		
		buttonOK = (Button) findViewById(R.id.registerOK);  
        buttonCancel = (Button) findViewById(R.id.registerCancel);  
  
        buttonOK.setOnClickListener(this);  
        buttonCancel.setOnClickListener(this); 
	}

	@Override
	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.registerOK:
			// 获取用户手机号 
            mobilenum = ((EditText)findViewById(R.id.reg_mobilenum)).getText().toString();  
            // 获取用户密码
            pwd1 = ((EditText)findViewById(R.id.pwd1)).getText().toString(); 
            pwd2 = ((EditText)findViewById(R.id.pwd2)).getText().toString();
            //获取其他信息
            realname = ((EditText)findViewById(R.id.realname)).getText().toString();
            stuid = ((EditText)findViewById(R.id.stuid)).getText().toString();
            
            if (TextUtils.isEmpty(mobilenum) || TextUtils.isEmpty(pwd1)|| TextUtils.isEmpty(pwd2)
            		||TextUtils.isEmpty(realname)|| TextUtils.isEmpty(stuid)) {  
                Toast.makeText(this, "所有信息都必须填写！", Toast.LENGTH_LONG).show();
                return;
            }else if(!pwd1.equals(pwd2)){
            	Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_LONG).show();
                return;
            }else{
            	if(profile.isNetworkAvailable()){
            		registerNodejsServer();
            	}else{
            		Toast.makeText(this, "没有网络连接！", Toast.LENGTH_LONG).show();
                    return;
            	}
            }
			break;
		case R.id.registerCancel:
			finish();
			break;
		}
			
	}

	private void registerNodejsServer() {
			String[] s = new String[4];		
			try {
				s[0] = URLEncoder.encode(mobilenum, "UTF-8");
				s[1] = URLEncoder.encode(profile.getMD5(pwd1), "UTF-8");
				s[2] = URLEncoder.encode(realname, "UTF-8");
				s[3] = URLEncoder.encode(stuid, "UTF-8");
				
					
				String site = ProfileUtil.mywebsite+"/init";
				String url = site + "?mobilenum=" + s[0] + "&pwd=" + s[1] + "&realname=" + s[2] + "&stuid="
						+ s[3];

				// 启动线程更新网站端数据库
				Handler h = new MyHandler(this);
				HttpGetThread httpThread = new HttpGetThread(url, h);
				new Thread(httpThread).start();
				
				

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end of registerNodejsServer()
	//http://blog.csdn.net/wuleihenbang/article/details/17126371
	//http://blog.csdn.net/aigochina/article/details/17841999
	private static class MyHandler extends Handler {  
        private final RegisterActivity mActivity;  
  
        public MyHandler(RegisterActivity activity) {  
            mActivity = new WeakReference<RegisterActivity>(activity).get();  
        }  
        
        @Override  
        public void handleMessage(Message msg) {  
        	super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("MyValue");//请求结果
			Toast.makeText(mActivity.getApplicationContext(), val, Toast.LENGTH_LONG).show();
			mActivity.finish();
        }  
    }  

	
	
}
