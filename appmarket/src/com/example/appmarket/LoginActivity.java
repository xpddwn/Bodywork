package com.example.appmarket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appmarket.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {
	private ImageButton back;
	private TextView register;
	private Button login;
	
	private EditText login_nickname;
	private EditText login_password;
	
	private Intent RegisterIntent;
	private ProgressDialog CircleDialog;
	private int status = 0;
	
	private String nickname;
	private String password;
	
	private final String tag = "login";

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
		
		login_nickname = (EditText)findViewById(R.id.nickname);
		nickname = login_nickname.getText().toString();
		login_password = (EditText)findViewById(R.id.passport);
		password = login_password.getText().toString();
		
		login = (Button)findViewById(R.id.btn_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				method();
				
				CircleDialog = ProgressDialog.show(LoginActivity.this, null,"正在提交",true,false);
				
				Thread myThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true){
							if(status == 1)
							{
								Message msg = new Message();
								msg.what = 1;
								mHandler.sendMessage(msg);
								status = 0;
								break;
							}else if(status ==2)
							{
								Message msg = new Message();
								msg.what = 2;
								mHandler.sendMessage(msg);
								status = 0;
								break;
							}else if(status == 3)
							{
								Message msg = new Message();
								msg.what = 3;
								mHandler.sendMessage(msg);
								status = 0;
								break;
							}else{
								continue;
							}
								
						}
					}
				});
				myThread.start();
			}
		});
	}

	protected void method() {
		// TODO Auto-generated method stub
		RequestParams login_request = new RequestParams();
		login_request.put("username",nickname);
		login_request.put("password", password);
		
		HttpUtil.post(tag, login_request, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				super.onSuccess(jsonobject);
				String statuscode = null;
				try {
					statuscode = jsonobject.getString("status");
					System.out.println("statuscode " + statuscode);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Setting onsuccess" + statuscode.toString());
				if (statuscode.equalsIgnoreCase("1")) {
					status = 1;
				}
				else if(statuscode.equalsIgnoreCase("2")){
					status = 3;
				}
				else if (statuscode.equalsIgnoreCase("100")) {
					status = 2;
				}

			}

			public void onFailure(Throwable arg0) { // 失败，调用
				System.out.println("onfailure");
				status = 2;
			}

			public void onFinish() { // 完成后调用，失败，成功，都要掉
				System.out.println("Setting onfinish");
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub

				super.handleFailureMessage(arg0, arg1);
				status = 2;
				System.out.println("onfailuremessage" + arg0 + arg1);
			};
		});
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// circleProgressBar.setVisibility(View.GONE);
				CircleDialog.dismiss();
				Thread.currentThread().interrupt();
				Toast mytoast = Toast.makeText(getApplicationContext(),
						"登陆成功", Toast.LENGTH_SHORT);
				mytoast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast.show();
				LoginActivity.this.finish();
				break;
			case 2:
				CircleDialog.dismiss();
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("对不起")
						.setMessage("现在的网络不可用，稍后再试!")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing - it will close on its own
									}
								}).show();
				Thread.currentThread().interrupt();
				break;
			case 3:
				CircleDialog.dismiss();
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("对不起")
						.setMessage("用户名不存在或密码错误")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing - it will close on its own
									}
								}).show();
				Thread.currentThread().interrupt();
				break;
			}
		}
	};
}
