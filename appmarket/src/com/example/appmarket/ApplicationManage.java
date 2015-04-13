package com.example.appmarket;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appmarket.adapter.FragementTabAdapter;

public class ApplicationManage extends FragmentActivity {
	private Context mContext;
	private TextView updateTextView, installTextView, unisntallTextView;
	private RelativeLayout container;
	private List<Fragment> fragments;
	private FragementTabAdapter adapter;
	private TextView updateAllTextView;
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_application_manage);
		mContext = this;
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < 3; i++) {
			ApplicationManageFragment fragement = new ApplicationManageFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("flag", i);
			fragement.setArguments(bundle);
			fragments.add(fragement);
		}
		findView();
		setView();
	}
	private void setView(){
		adapter = new FragementTabAdapter(fragments, this, R.id.container, 0);
		updateTextView.setOnClickListener(onClickListener);
		installTextView.setOnClickListener(onClickListener);
		unisntallTextView.setOnClickListener(onClickListener);
		backButton.setOnClickListener(backOnClickListener);
		updateAllTextView.setOnClickListener(updateAllOnClickListener);
		adapter.showTab(0);
		updateTextView.setSelected(true);
		
	}
	
	private OnClickListener backOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private OnClickListener updateAllOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void findView(){
		container = (RelativeLayout) findViewById(R.id.container);
		updateTextView = (TextView) findViewById(R.id.update);
		installTextView = (TextView) findViewById(R.id.install);
		unisntallTextView = (TextView) findViewById(R.id.unistall);
		backButton=(Button)findViewById(R.id.back);
		updateAllTextView=(TextView)findViewById(R.id.update_all);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.application_manage, menu);
		return true;
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			reset();
			switch (arg0.getId()) {
			case R.id.update:
				adapter.showTab(0);
				updateTextView.setSelected(true);
				break;
			case R.id.unistall:
				adapter.showTab(1);
				unisntallTextView.setSelected(true);
				break;
			case R.id.install:
				adapter.showTab(2);
				installTextView.setSelected(true);
				break;
			default:
				break;
			}
		}
	};
	public void reset(){
		updateTextView.setSelected(false);
		installTextView.setSelected(false);
		unisntallTextView.setSelected(false);
	}
}
