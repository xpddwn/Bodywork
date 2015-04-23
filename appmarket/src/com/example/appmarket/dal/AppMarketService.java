package com.example.appmarket.dal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AppMarketService {
	public static int status = 0;

	private static final String flag = "app";

	public static int getStatus() {

		return status;
	}

	public static void setStatus() {
		status = 0;
	}

	public static List<AppMarket> getJSONlistshops(String tag, String start)
			throws Exception {
		RequestParams params = new RequestParams();
		String username = MyAppMarket.getUsername();
		String password = MyAppMarket.getpapapa();
		final List<AppMarket> applist=new ArrayList<AppMarket>();
		
		if (tag.equalsIgnoreCase("life")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "2");
			params.put("start", start);
			params.put("length", "12");
		} else if (tag.equalsIgnoreCase("car")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "3");
			params.put("start", start);
			params.put("length", "12");
		} else if (tag.equalsIgnoreCase("news")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "4");
			params.put("start", start);
			params.put("length", "12");
		} else if (tag.equalsIgnoreCase("movie")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "5");
			params.put("start", start);
			params.put("length", "12");
		} else if (tag.equalsIgnoreCase("social")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "6");
			params.put("start", start);
			params.put("length", "12");
		} else if (tag.equalsIgnoreCase("tool")) {
			params.put("username", username);
			params.put("password", password);
			params.put("category_id", "7");
			params.put("start", start);
			params.put("length", "12");
		}
		
		HttpUtil.post(flag, params, new JsonHttpResponseHandler() {
			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				super.handleFailureMessage(arg0, arg1);
				System.out.println("onfailure");
				status = 2;
			};

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				System.out.println("onfailure");
				status = 2;
			}

			@Override
			public void onSuccess(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				super.onSuccess(jsonobject);
				String statuscode = null;
				JSONArray appitems = new JSONArray();
				try {
					statuscode = jsonobject.getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					appitems = jsonobject.getJSONArray("apps");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int count = appitems.length();
				if(count>=1){
					status = 1;
				}

				for (int i = 0; i < count; i++) {
					try {
						JSONObject items = appitems.getJSONObject(i);
						String app_name = items.getString("app_name");
						String icon_url = items.getString("icon_url");
						double size = items.getDouble("size");
						String version = items.getString("version");
						long download_count = items.getLong("download_count");
						String description = items.getString("description");
						long app_id = items.getLong("app_id");

						applist.add(new AppMarket(app_name, icon_url, size,
								version, download_count, description,app_id));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		return applist;
	}
}
