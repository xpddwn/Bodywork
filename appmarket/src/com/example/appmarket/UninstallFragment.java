package com.example.appmarket;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmarket.adapter.ApplicationManageListadapter;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.sqlite.service.AppInfoService;
import com.example.appmarket.view.XListView;

public class UninstallFragment extends Fragment {
	private static final String TAG = "ApplicationManageFragment";
	private Context mContext;
	private View view;
	private List<AppMarket> infos = new ArrayList<AppMarket>();
	private ApplicationManageListadapter adapter;
	private int flag;// 0 更新 1安装 2卸载
	private XListView listView;
	private AppInfoService service;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_manage_frgment, container, false);
		mContext = getActivity();
		flag = getArguments().getInt("flag");
		iniData();
		Log.e(TAG, "on create");
		return view;
	}
	
	public void iniData() {
		service=new AppInfoService(mContext);
		if(flag==2){
			infos=service.getInstalledApp();
		}else if(flag==1){
			infos=service.getCanIntallApp();
		}
		listView = (XListView) view.findViewById(R.id.listtest);
		listView.setPullLoadEnable(true);
		adapter = new ApplicationManageListadapter(mContext, infos, listView,flag);
		listView.setAdapter(adapter);
	}
}
