package com.example.hello;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.UUID;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.SharedPreferences;

public class ProfileUtil {
	private SharedPreferences sp;
	public ProfileUtil(SharedPreferences sharedPreference) {
		sp = sharedPreference;
	}
	//自己编写的方法
	/*
	 * 用来生成唯一的id
	 * 类似这样的格式：145835041706101
	 * 如果将系统部署到集群上面，情况有会有不同了，
	 * 不同的服务器集群生成的这个数字，是有重合的概率的，因此，一般情况是，
	 * 将集群中的每个机器进行编码，然后将机器编码放在这个标识的前面以示区分。
	 */
	private static AtomicInteger counter = new AtomicInteger(0);
	
	 public static void main(String[] args) {
         
	        System.out.println(ProfileUtil.getAtomicCounter());
	 }

	public static long getAtomicCounter() {
		if (counter.get() > 999999) {
			counter.set(1);
		}
		long time = System.currentTimeMillis();
		long returnValue = time * 100 + counter.incrementAndGet();
		return returnValue;
	}
	
	//uuid，返回去掉了-的uuid
	public static String getUUID() {

		String uuidStr = UUID.randomUUID().toString();

		uuidStr = uuidStr.substring(0, 8) + uuidStr.substring(9, 13)

				+ uuidStr.substring(14, 18) + uuidStr.substring(19, 23)

				+ uuidStr.substring(24);

		return uuidStr;

	}
	//读参数：读取姓名、学号等信息
	public String readParam(String sName) {

		return sp.getString(sName, "N/A");

	}
	//写参数：写入姓名、学号等信息
	public void writeParam(String key, String value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();

	}
	//读写配置文件
	public void writeTime(long time, String sName) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putString(sName, Long.toString(time));
		editor.commit();

	}

	public long readTime(String sName) {

		String tmp = sp.getString(sName, "0");
		if (tmp == null)
			return 0l;
		else
			return Long.parseLong(tmp);
	}
}