package com.example.appmarket.sqlite.model;

public class ApplicationInfo {
	public int app_id;
	public int category_id;
	public String app_name;
	public String icon_url;
	public String description;//√Ë ˆ
	public float size;
	public String version;//∞Ê±æ
	public int install_state;
	public String apk_path;
	

	public static final String FIELD_TABLE_NAME="app";
	public static final String FIELD_APP_ID="app_id";
	public static final String FIELD_CATRGORY_ID="category_id";
	public static final String FIELD_APP_NAME="app_name";
	public static final String FIELD_ICON_URL="icon_url";
	public static final String FIELD_DESCRIPPTION="description";
	public static final String FIELD_SIZE="size";
	public static final String FIELD_VERSION="version";
	public static final String FIELD_INSTALL_STATE="install_state";
}
