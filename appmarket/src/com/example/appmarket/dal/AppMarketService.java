package com.example.appmarket.dal;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AppMarketService {
	public static JSONArray appitems = null;
	public static int status = 0;

	private static final String flag = "app";

	public static int getStatus() {

		return status;
	}

	public static void setStatus() {
		status = 0;
	}

	public static ArrayList<ApplicationInfo> getJSONlistshops(String tag, String start)
			throws Exception {
		RequestParams params = new RequestParams();
		String username = MyAppMarket.getUsername();
		String password = MyAppMarket.getpapapa();
		final ArrayList<ApplicationInfo> applist=new ArrayList<ApplicationInfo>();
		
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
		} else if (tag.equalsIgnoreCase("new")) {
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
				try {
					JsonObject object=JsonUtil.stringToJsonObject(jsonobject.toString());
					int statuscode = object.get("status").getAsInt();
					ArrayList<ApplicationInfo> infos=JsonUtil.jsonToComplexBean(object.get("apps").toString(), 
							new TypeToken<ArrayList<ApplicationInfo>>() {}.getType() );
					applist.addAll(infos);
					applist.get(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		return applist;
	}
}
