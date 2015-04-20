package com.example.appmarket.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient();
	private static String baseurl = "http://123.57.133.126/app/v1/";

	static {
		client.setTimeout(10000);
	}

	public static void get(RequestParams params, AsyncHttpResponseHandler res) {
		client.get(baseurl, params, res);
	}

	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void post(String tag, RequestParams params,
			JsonHttpResponseHandler res) {
		String httpurl = null;
		if (tag.equalsIgnoreCase("register")) {
			httpurl = baseurl + "register";
		} else if (tag.equalsIgnoreCase("login")) {
			httpurl = baseurl + "login";
		} else if (tag.equalsIgnoreCase("app")) {
			httpurl = baseurl + "get_apps";
		}else if(tag.equalsIgnoreCase("download_app")){
			httpurl = baseurl + "download_app";
		}
		client.post(httpurl, params, res);
	}

	public static void post(RequestParams params, AsyncHttpResponseHandler res) {
		// TODO Auto-generated method stub
		client.post(baseurl, params, res);
	}
}
