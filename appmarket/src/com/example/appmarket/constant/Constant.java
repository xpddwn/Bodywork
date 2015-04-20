package com.example.appmarket.constant;

import android.os.Environment;

public class Constant {
	// app����
	public static final String PACKAGE_NAME = "com.example.appmarket";

	// ��������sd���ϵ�ַ
	public static final String APP_DATA_PATH = Environment
			.getExternalStorageDirectory() + "/appmarket/";
	// assetsĿ¼��ͼƬ����ļ���
	public static final String ASSETS_IMAGE_PATH = "images";
	// imagesת�浽sd�����ļ���
	public static final String SDCARD_IMAGE_PATH = APP_DATA_PATH + "image/";
}
