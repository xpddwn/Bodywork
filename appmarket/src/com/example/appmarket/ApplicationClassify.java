package com.example.appmarket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmarket.adapter.ApplicationClassifyGridadapter;
import com.example.appmarket.dal.AppMarketService;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.view.XListView;
import com.example.appmarket.view.XListView.IXListViewListener;

public class ApplicationClassify extends Activity implements IXListViewListener{
	private final String TAG = "ApplicationClassify";
	private TextView title;
	private List<AppMarket> infos = new ArrayList<AppMarket>();
	private ApplicationClassifyGridadapter adapter;
	private XListView gridview;
	private Thread myThread;

	private ImageButton back;
	private String name;
	private int start = 0;
	private String freshtime = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_grid);
		Bundle bundle = this.getIntent().getExtras();
		name = bundle.getString("name");
		
		initview();
		gridview = (XListView) this.findViewById(R.id.listtest1);
		gridview.setPullLoadEnable(true);
		gridview.setXListViewListener(this);
		onRefresh();
		setdata();
	}
	
	public void initview(){
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
		
	}

	@Override
	public void onResume() {
		super.onResume();
		if(infos != null)
		{
			infos.clear();
		}
	}	
	
	private void getdata(String tag, String start) {
		// TODO Auto-generated method stub
		try{
			infos = AppMarketService.getJSONlistshops(tag, start);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void setdata() {
		adapter = new ApplicationClassifyGridadapter(this, infos, gridview);
		gridview.setAdapter(adapter);
		System.out.println("test");
	}

	private Handler mHandler1 = new Handler() {
		public void handleMessage(Message msg) {
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
				Toast mytoast = Toast.makeText(getApplicationContext(),
						"失败了，等会再试试，或者检查一下网络", Toast.LENGTH_SHORT);
				mytoast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast.show();
				break;
			}
		}
	};
	
	public void gettime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		freshtime = formatter.format(curDate);
	}
	
	public void restartthread() {
		if(myThread != null)	
		{	if (myThread.isAlive()) {
				myThread.interrupt();
			}
			myThread = newThread();
			myThread.start();
		}
		else{
			myThread = newThread();
			myThread.start();
		}
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
						mHandler1.sendMessage(msg);
						AppMarketService.setStatus();
						break;
					} else if (AppMarketService.getStatus() == 2) {
						Message msg = new Message();
						msg.what = 2;
						mHandler1.sendMessage(msg);
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
		getdata(name, String.valueOf(start));
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		int mypos = infos.size();
		if (mypos==8) {
			getdata(name, String.valueOf(8*(start++)));
		} else {
			gridview.stopLoadMore();
		}
		adapter.refreshData(infos);
		onLoad();
	}
	
	private void onLoad() {
		gridview.stopRefresh();
		gridview.stopLoadMore();
		gridview.setRefreshTime(freshtime);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(infos != null)
		System.out.println("ClassifyActivity OnDestroy");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(infos != null)
		System.out.println("ClassifyActivity OnPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(infos != null)
		System.out.println("ClassifyActivity OnRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("ClassifyActivity OnStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("ClassifyActivity OnStop");
	}

}
