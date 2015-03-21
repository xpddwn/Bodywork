package com.example.appmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private ImageButton download;
	private ImageButton classify;

	private RelativeLayout life;
	private RelativeLayout car;
	private RelativeLayout news;
	private RelativeLayout movie;
	private RelativeLayout social;
	private RelativeLayout tool;
	
	private Intent classifyIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		

		download = (ImageButton) findViewById(R.id.download);
		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				classifyIntent = new Intent(MainActivity.this,ApplicationManage.class);
				startActivity(classifyIntent);
			}
		});

		classify = (ImageButton) findViewById(R.id.classify);
		classify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		life = (RelativeLayout) findViewById(R.id.life);
		life.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开生活类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});

		car = (RelativeLayout) findViewById(R.id.life1);
		car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开车务类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});

		news = (RelativeLayout) findViewById(R.id.life2);
		news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开新闻类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});

		movie = (RelativeLayout) findViewById(R.id.life3);
		movie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开影音类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});

		social = (RelativeLayout) findViewById(R.id.life4);
		social.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开社交类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});

		tool = (RelativeLayout) findViewById(R.id.life5);
		tool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this).setTitle("消息")
						.setMessage("打开工具类型应用列表").setPositiveButton("确定", null)
						.show();
			}
		});
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
