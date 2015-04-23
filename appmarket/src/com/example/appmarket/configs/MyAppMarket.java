package com.example.appmarket.configs;

import cn.trinea.android.common.service.impl.ImageCache;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyAppMarket extends Application {
	private static Context mcontext;
	public static SharedPreferences mysp = null;
	private static Editor myeditor;

	private static int myregister = 0;
	private static final ImageCache ICON_CACHE = new ImageCache();

	@Override
	public void onCreate() {
		super.onCreate();
		mcontext = getApplicationContext();
		mysp = mcontext.getSharedPreferences("SP", mcontext.MODE_PRIVATE);
		myeditor = mysp.edit();
	}

	public static Context getCurrentContext() {
		return mcontext;
	}

	public static void setRegister() {
		myregister = 1;
		myeditor.putInt("register", 1);
		myeditor.commit();
	}

	public static int getRegister() {
		return mysp.getInt("register", 0);
	}

	public static String getUsername() {
		return mysp.getString("username", "");
	}

	public static String getpapapa() {
		return mysp.getString("papapa", "");
	}
	
	public static ImageCache getImageCache(){
		if(ICON_CACHE != null)
		{
			return ICON_CACHE;
		}
		else{
			return new ImageCache();
		}
    }
    
    public static ImageCache getImageCacheavatar(){

			return new ImageCache();
		
    }
}
