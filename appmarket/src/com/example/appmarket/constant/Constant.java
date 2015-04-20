package com.example.appmarket.constant;

import android.os.Environment;

public class Constant {
	public static final String PACKAGE_NAME = "com.example.appmarket";
	public static final String APP_DATA_PATH = Environment
			.getExternalStorageDirectory() + "/appmarket/";
	public static final String ASSETS_IMAGE_PATH = "images";
	public static final String SDCARD_IMAGE_PATH = APP_DATA_PATH + "image/";
	public static final String BASE_URL = "http://123.57.133.126/app/v1/";
}
