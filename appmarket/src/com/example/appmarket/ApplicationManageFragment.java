package com.example.appmarket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appmarket.adapter.ApplicationManageListadapter;
import com.example.appmarket.dal.AppMarketService;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.view.XListView;
import com.example.appmarket.view.XListView.IXListViewListener;

public class ApplicationManageFragment extends Fragment implements
		IXListViewListener {

	private static final String TAG = "ApplicationManageFragment";
	private Context mContext;
	private View view;
	private List<AppMarket> infos = new ArrayList<AppMarket>();
	private ApplicationManageListadapter adapter;
	private int flag;// 0 更新 1安装 2卸载
	private XListView listView;

	private Thread myThread;
	private String freshtime;
	private int start = 0;
	private String name;

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
		switch(flag){
		case 0:
			name = "update";
			break;
		case 1:
			name = "install";
			break;
		case 2:
			name = "uninstall";
			break;
		default:
			break;
		}
		listView = (XListView) view.findViewById(R.id.listtest);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		onRefresh();
		setdata();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "on resume");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				myThread.interrupt();
				setdata();
				XListView.setfooterhide(1);
				System.out.println("set 1");
				onLoad();
				break;
			case 2:
				myThread.interrupt();
				Toast mytoast = Toast.makeText(mContext.getApplicationContext(),
						"失败了，等会再试试，或者检查一下网络", Toast.LENGTH_SHORT);
				mytoast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast.show();
				break;
			case 3:
				myThread.interrupt();
				Toast mytoast2 = Toast.makeText(mContext.getApplicationContext(),
						"已经到底啦", Toast.LENGTH_SHORT);
				mytoast2.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast2.show();
				break;	
			}
		}
	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		XListView.setfooterhide(0);
		AppMarketService.setStatus();
		if (infos != null) {
			infos.clear();
		}

		restartthread();
		gettime();
		getdata(name, "0");
	}

	protected void setdata() {
		// TODO Auto-generated method stub
		adapter = new ApplicationManageListadapter(mContext, infos, listView,
				flag);
		listView.setAdapter(adapter);
	}

	private void getdata(String name, String start) {
		// TODO Auto-generated method stub
		try{
			infos = AppMarketService.getJSONlistshops(name, start);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void gettime() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		freshtime = formatter.format(curDate);
	}

	public void restartthread() {
		if (myThread != null) {
			if (myThread.isAlive()) {
				myThread.interrupt();
			}
			myThread = newThread();
			myThread.start();
		} else {
			myThread = newThread();
			myThread.start();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int mypos = infos.size();
		System.out.println("infos" + mypos);
		if (mypos == 8) {
			getdata(name, String.valueOf(1 * (start++)));
		} else {
			Toast mytoast = Toast.makeText(mContext.getApplicationContext(),
					"已经到底，没有更多应用加载", Toast.LENGTH_SHORT);
			mytoast.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL, 0, 0);
			mytoast.show();
			listView.stopLoadMore();
		}
		adapter.refreshData(infos);
		onLoad();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(freshtime);
	}
	
	public Thread newThread() {
		Thread myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (AppMarketService.getStatus() == 1) {
						System.out.println(" decide status = 1");
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						AppMarketService.setStatus();
						break;
					} else if (AppMarketService.getStatus() == 2) {
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						AppMarketService.setStatus();
						break;
					} else if (AppMarketService.getStatus() == 3) {
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
						AppMarketService.setStatus();
						break;
					}

					else {
						continue;
					}

				}
			}
		});
		return myThread;
	}

}