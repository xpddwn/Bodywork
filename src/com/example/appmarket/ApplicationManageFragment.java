package com.example.appmarket;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.appmarket.adapter.ApplicationManageListadapter;
import com.example.appmarket.entity.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;

public class ApplicationManageFragment extends Fragment {

	private static final String TAG = "ApplicationManageFragment";
	private Context mContext;
	private View view;
	private ListView listView;
	private ArrayList<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
	private ApplicationManageListadapter adapter;
	private boolean havaLoadData = false;
	private SyncImageLoader imageLoader;
	private boolean isLoading = false;// 是否正在加载
	private int lastVisibleIndex;// 最后一个可见的item
	private int flag;//0 升级 1卸载  2安装

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_manage_frgment, container, false);
		mContext = getActivity();// 由getActivity()获取其上下文
		flag=getArguments().getInt("flag");
		iniData();
		Log.e(TAG, "on create");
		return view;
	}

	public void iniData() {
		listView = (ListView) view.findViewById(R.id.list);
		imageLoader = new SyncImageLoader();
		adapter = new ApplicationManageListadapter(mContext, infos, listView,
				imageLoader,flag);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(onScrollListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "on resume");
		//如果没有加载数据，就从网络加载数据
		if (!havaLoadData) {
			infos.clear();
			for (int i = 0; i < 10; i++) {
				ApplicationInfo info = new ApplicationInfo();
				info.name = "爱奇艺" + i;
				info.description = "音乐 12.5M";
				info.picturePath = "http://p6.qhimg.com/t013f443fd02b23599f.jpg";
				infos.add(info);
			}
			havaLoadData = true;
			adapter.notifyDataSetChanged();
		}
	}

	// 完成只去加载显示的的图片
	private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				//Log.e(TAG, "fling");
				imageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				imageLoader.lock();
				break;
			default:
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后一个可见的item是第几个
			//Log.e(TAG, "scroll " + lastVisibleIndex);
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			// TODO Auto-generated method stub
		}
	};

	// 加载图片
	private void loadImage() {
		int start = listView.getFirstVisiblePosition();
		int end = listView.getLastVisiblePosition();
		if (end >= listView.getCount()) {
			end = listView.getCount() - 1;
		}
		imageLoader.setLoadLimit(start, end);
		imageLoader.unlock();
	}
}