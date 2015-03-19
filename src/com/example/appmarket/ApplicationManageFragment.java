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
	private boolean isLoading = false;// �Ƿ����ڼ���
	private int lastVisibleIndex;// ���һ���ɼ���item
	private int flag;//0 ���� 1ж��  2��װ

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_manage_frgment, container, false);
		mContext = getActivity();// ��getActivity()��ȡ��������
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
		//���û�м������ݣ��ʹ������������
		if (!havaLoadData) {
			infos.clear();
			for (int i = 0; i < 10; i++) {
				ApplicationInfo info = new ApplicationInfo();
				info.name = "������" + i;
				info.description = "���� 12.5M";
				info.picturePath = "http://p6.qhimg.com/t013f443fd02b23599f.jpg";
				infos.add(info);
			}
			havaLoadData = true;
			adapter.notifyDataSetChanged();
		}
	}

	// ���ֻȥ������ʾ�ĵ�ͼƬ
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
			// �������һ���ɼ���item�ǵڼ���
			//Log.e(TAG, "scroll " + lastVisibleIndex);
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			// TODO Auto-generated method stub
		}
	};

	// ����ͼƬ
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