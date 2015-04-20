package com.example.appmarket;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmarket.adapter.ApplicationClassifyGridadapter;
import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.example.appmarket.util.SyncImageLoader;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApplicationClassify extends Activity {
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
	private String name;
	private int start = 0;
	private Handler mHandler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_grid);
		Bundle bundle = this.getIntent().getExtras();
		name = bundle.getString("name");

		title = (TextView) findViewById(R.id.app_classtype);

		if (name.contentEquals("life")) {
			title.setText(R.string.life);
		} else if (name.contentEquals("car")) {
			title.setText(R.string.car);
		} else if (name.contentEquals("news")) {
			title.setText(R.string.news);
		} else if (name.contentEquals("movie")) {
			title.setText(R.string.movie);
		} else if (name.contentEquals("social")) {
			title.setText(R.string.social);
		} else if (name.contentEquals("tool")) {
			title.setText(R.string.tool);
		}

		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ApplicationClassify.this.finish();
			}
		});
		mcontext = this;
		pullToRefreshGridview = (PullToRefreshGridView) findViewById(R.id.grid);
		gridview = pullToRefreshGridview.getRefreshableView();
		imageLoader = new SyncImageLoader();
		setdata();
		pullToRefreshGridview.setOnScrollListener(onScrollListener);
		pullToRefreshGridview.setOnRefreshListener(onRefreshListener2);

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "on resume");
		System.out.println("system onresume");
		if (!havaLoadData) {
			handler.sendEmptyMessage(0);
		}
	}

	private void getdata(String tag, String start) {
		// TODO Auto-generated method stub
		try {
			RequestParams params = new RequestParams();
			String username = MyAppMarket.getUsername();
			String password = MyAppMarket.getpapapa();
			
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
			
			HttpUtil.post("app", params, new JsonHttpResponseHandler() {
				@Override
				protected void handleFailureMessage(Throwable arg0, String arg1) {
					super.handleFailureMessage(arg0, arg1);
					System.out.println("onfailure");
			
				};

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					// TODO Auto-generated method stub
					super.onFailure(arg0, arg1);
					System.out.println("onfailure");
				}

				@Override
				public void onSuccess(JSONObject jsonobject) {
					// TODO Auto-generated method stub
					super.onSuccess(jsonobject);
					try {
						JsonObject object=JsonUtil.stringToJsonObject(jsonobject.toString());
						int statuscode = object.get("status").getAsInt();
						ArrayList<ApplicationInfo> minfos=JsonUtil.jsonToComplexBean(object.get("apps").toString(), 
								new TypeToken<ArrayList<ApplicationInfo>>() {}.getType() );
						infos.clear();
						infos.addAll(minfos);
						havaLoadData=true;
						handler.sendEmptyMessage(1);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setdata() {
		adapter = new ApplicationClassifyGridadapter(mcontext, infos, gridview,
				imageLoader);
		gridview.setAdapter(adapter);
		System.out.println("test");
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				getdata(name, String.valueOf(start));
				break;
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	private Handler mHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setdata();
				havaLoadData = true;
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast mytoast = Toast.makeText(getApplicationContext(),
						"失败了，等会再试试，或者检查一下网络", Toast.LENGTH_SHORT);
				mytoast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast.show();
				break;
			}
		}
	};

	
	

	private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				// Log.e(TAG, "fling");
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

	private OnRefreshListener2<GridView> onRefreshListener2 = new OnRefreshListener2<GridView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			// TODO Auto-generated method stub
			Toast.makeText(mcontext, "down", 1000).show();
			pullToRefreshGridview.onRefreshComplete();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			// TODO Auto-generated method stub
			getdata(name, String.valueOf(8 * (start++)));
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
