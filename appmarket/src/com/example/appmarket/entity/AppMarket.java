package com.example.appmarket.entity;

public class AppMarket {
	public String app_name;
	public String icon_url;
	public double size;
	public String version;
	public long download_count;
	public String description;
	public long app_id;
	public int install_state;
	public String apk_path;
	public int category_id;


	public static final String FIELD_TABLE_NAME = "app";
	public static final String FIELD_APP_ID = "app_id";
	public static final String FIELD_CATRGORY_ID = "category_id";
	public static final String FIELD_APP_NAME = "app_name";
	public static final String FIELD_ICON_URL = "icon_url";
	public static final String FIELD_DESCRIPPTION = "description";
	public static final String FIELD_SIZE = "size";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_INSTALL_STATE = "install_state";
	public AppMarket(){
		
	}

	public AppMarket(String app_name, String icon_url, double size,
			String version, long download_count, String description,long app_id) {
		this.app_name = app_name;
		this.icon_url = icon_url;
		this.size = size;
		this.version = version;
		this.download_count = download_count;
		this.description = description;
		this.app_id = app_id;
	}

	public void setapp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getapp_name() {
		return app_name;
	}

	public void seticon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String geticon_url() {
		return icon_url;
	}

	public void setsize(double size) {
		this.size = size;
	}

	public double getsize() {
		return size;
	}

	public void setversion(String version) {
		this.version = version;
	}

	public String getversion() {
		return version;
	}

	public void setdownload_count(long download_count) {
		this.download_count = download_count;
	}

	public long getdownload_count() {
		return download_count;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public String getdescription() {
		return description;
	}
	
	public long getapp_id() {
		return app_id;
	}
}
