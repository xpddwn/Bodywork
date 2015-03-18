package com.example.appmarket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ApplicationManage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_manage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.application_manage, menu);
		return true;
	}
}
