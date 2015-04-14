package com.example.appmarket.configs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyAppMarket extends Application {
	private static Context mcontext;
	public static SharedPreferences mysp = null;
	private static Editor myeditor;
	
	private static int myregister = 0;

	@Override
	public void onCreate(){
		super.onCreate();
		mcontext = getApplicationContext();
		mysp = mcontext.getSharedPreferences("SP", mcontext.MODE_PRIVATE);
		myeditor = mysp.edit();
	}
	
	 public static Context getCurrentContext(){
	        return mcontext;
	}
	
	 public static void setRegister(){
		 myregister = 1;
		 myeditor.putInt("register", 1);
		 myeditor.commit();
	 }
	 
	 public static int getRegister(){
		 return mysp.getInt("register", 0);
	 }
	 
	 public static String getUsername(){
		 return mysp.getString("username", "");
	 }
	 
	 public static String getpapapa(){
		 return mysp.getString("papapa", "");
	 }
}
