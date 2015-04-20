package com.example.appmarket.sqlite.model;

public class ApplicationInfo {
	public int app_id;
	public int category_id;
	public String app_name;
	public String icon_url;
	public String description;// 描述信息
	public float size;
	public String version;// 
	public int install_state;
	public String apk_path;
	

	public static final String FIELD_TABLE_NAME = "app";
	public static final String FIELD_APP_ID = "app_id";
	public static final String FIELD_CATRGORY_ID = "category_id";
	public static final String FIELD_APP_NAME = "app_name";
	public static final String FIELD_ICON_URL = "icon_url";
	public static final String FIELD_DESCRIPPTION = "description";
	public static final String FIELD_SIZE = "size";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_INSTALL_STATE = "install_state";
	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getInstall_state() {
		return install_state;
	}
	public void setInstall_state(int install_state) {
		this.install_state = install_state;
	}
	public String getApk_path() {
		return apk_path;
	}
	public void setApk_path(String apk_path) {
		this.apk_path = apk_path;
	}
	
	
}
