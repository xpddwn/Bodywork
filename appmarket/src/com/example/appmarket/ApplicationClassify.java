package com.example.appmarket;

import java.util.ArrayList;

import com.example.appmarket.adapter.ApplicationClassifyGridadapter;
import com.example.appmarket.entity.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationClassify extends Activity{
	private final String TAG = "ApplicationClassify";
	private Context mcontext;
	private TextView title;
    private PullToRefreshGridView pullToRefreshGridview;
    private ArrayList<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
    private ApplicationClassifyGridadapter adapter;
    private boolean havaLoadData = false;
	private SyncImageLoader imageLoader;
	private boolean isLoading = false;// 是否正在加载
	private int lastVisibleIndex;// 最后一个可见的item
	private GridView gridview;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_grid);
		//新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String name = bundle.getString("name");
        
        title = (TextView)findViewById(R.id.app_classtype);
        
        if(name.contentEquals("life")){
        	title.setText(R.string.life);
        }
        else if(name.contentEquals("car")){
        	title.setText(R.string.car);
        }
        else if(name.contentEquals("news")){
        	title.setText(R.string.news);
        }
        else if(name.contentEquals("movie")){
        	title.setText(R.string.movie);
        }
        else if(name.contentEquals("social")){
        	title.setText(R.string.social);
        }
        else if(name.contentEquals("tool")){
        	title.setText(R.string.tool);
        }
		mcontext = this;
		pullToRefreshGridview = (PullToRefreshGridView)findViewById(R.id.grid);
		gridview=pullToRefreshGridview.getRefreshableView();
		imageLoader = new SyncImageLoader();
		adapter = new ApplicationClassifyGridadapter(mcontext, infos, gridview,
				imageLoader);
		gridview.setAdapter(adapter);
		pullToRefreshGridview.setOnScrollListener(onScrollListener);
		pullToRefreshGridview.setOnRefreshListener(onRefreshListener2);	

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
				lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
				// TODO Auto-generated method stub
			}
		};
		
		private OnRefreshListener2<GridView> onRefreshListener2=new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				Toast.makeText(mcontext, "down", 1000).show();
				pullToRefreshGridview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
			}
		};
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(mcontext, "up", 1000).show();
					ApplicationInfo info = new ApplicationInfo();
					info.name = "爱奇艺" +" down";
					info.description = "音乐 12.5M";
					info.picturePath = "http://p6.qhimg.com/t013f443fd02b23599f.jpg";
					infos.add(info);
					adapter.notifyDataSetChanged();
					pullToRefreshGridview.onRefreshComplete();
					break;
				default:
					break;
				}
			}
		};
		
		// 加载图片
		private void loadImage() {
			int start = gridview.getFirstVisiblePosition();
			int end = gridview.getLastVisiblePosition();
			if (end >= gridview.getCount()) {
				end = gridview.getCount() - 1;
			}
			imageLoader.setLoadLimit(start, end);
			imageLoader.unlock();
		}

}
