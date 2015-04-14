package com.example.appmarket;

import com.example.appmarket.configs.MyAppMarket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private LinearLayout download;
	private LinearLayout classify;
	
	private TextView down;
	private TextView up;
	
	private ImageButton user;

	private RelativeLayout life;
	private RelativeLayout car;
	private RelativeLayout news;
	private RelativeLayout movie;
	private RelativeLayout social;
	private RelativeLayout tool;
	
	private Intent classifyIntent;
	private Intent lifeIntent;
	private Intent loginIntent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		down = (TextView)findViewById(R.id.download1);
		up = (TextView)findViewById(R.id.classify1);
		down.setSelected(false);
		up.setSelected(true);
		download = (LinearLayout) findViewById(R.id.title1);
		classify = (LinearLayout) findViewById(R.id.title2);
		user = (ImageButton)findViewById(R.id.user);
		life = (RelativeLayout) findViewById(R.id.life);
		car = (RelativeLayout) findViewById(R.id.life1);
		movie = (RelativeLayout) findViewById(R.id.life3);
		social = (RelativeLayout) findViewById(R.id.life4);
		news = (RelativeLayout) findViewById(R.id.life2);
		tool = (RelativeLayout) findViewById(R.id.life5);
		
		if(MyAppMarket.getRegister()==0)
		{
			loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
		}else{
			user.setBackgroundResource(R.drawable.user1);
		}
		
		
		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				down.setSelected(true);
				up.setSelected(false);
				classifyIntent = new Intent(MainActivity.this,ApplicationManage.class);
				startActivity(classifyIntent);
			}
		});

		classify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
		user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginIntent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(loginIntent);
			}
		});

		life.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "life");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});

		car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "car");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});

		news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "news");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});

		movie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "movie");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});

		social.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "social");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});

		tool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lifeIntent = new Intent(MainActivity.this,ApplicationClassify.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为life
			    bundle.putString("name", "tool");
			    lifeIntent.putExtras(bundle);
				startActivity(lifeIntent);
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		down.setSelected(false);
		up.setSelected(true);
		
		if(MyAppMarket.getRegister() == 1)
		{
			user.setBackgroundResource(R.drawable.user1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
