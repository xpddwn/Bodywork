package com.example.appmarket.entity;

public class AppMarket {
	private String app_name;
	private String icon_url;
	private double size;
	private String version;
	private long download_count;
	private String description;
	private long app_id;

	public AppMarket() {
		// ȱʡ
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
