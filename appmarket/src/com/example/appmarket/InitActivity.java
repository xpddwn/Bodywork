package com.example.appmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.appmarket.sqlite.manager.ClientDataBase;

/**
 * 
 * ����ʼ����activity
 * 
 * @author tuomao
 * 
 */
public class InitActivity extends Activity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		mContext = this;
		initDataBase();
		startMainActivity();
	}

	/**
	 * 
	 * ��ʼ����ݿ�
	 */
	public void initDataBase() {
		ClientDataBase dataBase = new ClientDataBase(mContext);
		dataBase.openDatabase();
		dataBase.closeDatabase();
	}

	public void startMainActivity() {
		Intent intent = new Intent();
		intent.setClass(mContext, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
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
