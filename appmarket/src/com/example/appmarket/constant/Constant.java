package com.example.appmarket.constant;

import android.os.Environment;

public class Constant {
	//app包名
	public static final String PACKAGE_NAME="com.example.appmarket";

	// 程序存放在sd卡上地址
	public static final String APP_DATA_PATH = Environment
			.getExternalStorageDirectory() + "/appmarket/";
	// assets目录下图片存放文件夹
	public static final String ASSETS_IMAGE_PATH = "images";
	// images转存到sd卡上文件夹
	public static final String SDCARD_IMAGE_PATH = APP_DATA_PATH+"image/";
}
