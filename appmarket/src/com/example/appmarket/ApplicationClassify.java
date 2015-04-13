package com.example.appmarket;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmarket.adapter.ApplicationClassifyGridadapter;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public class ApplicationClassify extends Activity{
	private final String TAG = "ApplicationClassify";
	private Context mcontext;
	private TextView title;
    private PullToRefreshGridView pullToRefreshGridview;
    private ArrayList<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
    private ApplicationClassifyGridadapter adapter;
    private boolean havaLoadData = false;
	private SyncImageLoader imageLoader;
	private boolean isLoading = false;// 鏄惁姝ｅ湪鍔犺浇
	private int lastVisibleIndex;// 鏈�鍚庝竴涓彲瑙佺殑item
	private GridView gridview;
	
	private ImageButton back;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_grid);
		//鏂伴〉闈㈡帴鏀舵暟鎹�
        Bundle bundle = this.getIntent().getExtras();
        //鎺ユ敹name鍊�
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
        
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ApplicationClassify.this.finish();
			}
		});
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
		//濡傛灉娌℃湁鍔犺浇鏁版嵁锛屽氨浠庣綉缁滃姞杞芥暟鎹�
		if (!havaLoadData) {
			infos.clear();
			for (int i = 0; i < 10; i++) {
				ApplicationInfo info = new ApplicationInfo();
				info.app_name = "鐖卞鑹�" + i;
				info.description = "闊充箰 12.5M";
				info.icon_url = "http://p6.qhimg.com/t013f443fd02b23599f.jpg";
				infos.add(info);
			}
			havaLoadData = true;
			adapter.notifyDataSetChanged();
		}
	}

	// 瀹屾垚鍙幓鍔犺浇鏄剧ず鐨勭殑鍥剧墖
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
					info.app_name = "鐖卞鑹�" +" down";
					info.description = "闊充箰 12.5M";
					info.icon_url = "http://p6.qhimg.com/t013f443fd02b23599f.jpg";
					infos.add(info);
					adapter.notifyDataSetChanged();
					pullToRefreshGridview.onRefreshComplete();
					break;
				default:
					break;
				}
			}
		};
		
		// 鍔犺浇鍥剧墖
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
