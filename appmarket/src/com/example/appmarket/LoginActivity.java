package com.example.appmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;


public class LoginActivity extends Activity {
	private ImageButton back;
	private TextView register;
	
	private Intent RegisterIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		back = (ImageButton)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
		});
		
		register = (TextView)findViewById(R.id.user_register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(RegisterIntent);
			}
		});
	}
}
