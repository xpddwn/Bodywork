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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private final String tag = "register";
	private ImageButton back;

	private EditText mynickname;
	private EditText mypassword;
	private EditText identification;

	private Button myRegister;

	private String nickname;
	private String password;
	private String repassword;

	private ProgressDialog circleDialog;

	private int status = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);

		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
		});

		mynickname = (EditText) findViewById(R.id.nickname);
		mypassword = (EditText) findViewById(R.id.passport);
		identification = (EditText) findViewById(R.id.repassport);

		myRegister = (Button) findViewById(R.id.btn_register);

		myRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nickname = mynickname.getText().toString();
				password = mypassword.getText().toString();
				repassword = identification.getText().toString();

				if (nickname.equalsIgnoreCase("")
						|| password.equalsIgnoreCase("")
						|| repassword.equalsIgnoreCase("")) {
					new AlertDialog.Builder(RegisterActivity.this)
							.setTitle("ע�����")
							.setMessage("����������ע����Ϣ")
							.setNegativeButton("ȷ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing - it will close on its
											// own
										}
									}).show();
				} else if (!password.equalsIgnoreCase(repassword)) {
					new AlertDialog.Builder(RegisterActivity.this)
							.setTitle("ע�����")
							.setMessage("������������벻ͬ")
							.setNegativeButton("ȷ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing - it will close on its
											// own
										}
									}).show();
				} else {
					method();
					circleDialog = ProgressDialog.show(RegisterActivity.this,
							null, "�����ύ", true, false);

					Thread myThread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							while (true) {
								if (status == 1) {
									Message msg = new Message();
									msg.what = 1;
									mHandler.sendMessage(msg);
									status = 0;
									break;
								} else if (status == 2) {
									Message msg = new Message();
									msg.what = 2;
									mHandler.sendMessage(msg);
									status = 0;
									break;
								} else if (status == 3) {
									Message msg = new Message();
									msg.what = 3;
									mHandler.sendMessage(msg);
									status = 0;
									break;
								} else {
									continue;
								}
							}
						}
					});
					myThread.start();
				}
			}
		});
	}

	private void method() {
		// TODO Auto-generated method stub
		RequestParams register_request = new RequestParams();
		register_request.put("username", nickname);
		register_request.put("password", password);

		HttpUtil.post(tag, register_request, new JsonHttpResponseHandler() {
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
				if (statuscode.equalsIgnoreCase("0")) {
					status = 1;
				} else if (statuscode.equalsIgnoreCase("2")) {
					status = 3;
				} else if (statuscode.equalsIgnoreCase("100")) {
					status = 2;
				}

			}

			public void onFailure(Throwable arg0) { // ʧ�ܣ�����
				System.out.println("onfailure");
				status = 2;
			}

			public void onFinish() { // ��ɺ���ã�ʧ�ܣ��ɹ�����Ҫ��
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

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// circleProgressBar.setVisibility(View.GONE);
				circleDialog.dismiss();
				Thread.currentThread().interrupt();
				Toast mytoast = Toast.makeText(getApplicationContext(),
						"ע��ɹ�,������½", Toast.LENGTH_SHORT);
				mytoast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				mytoast.show();
				RegisterActivity.this.finish();
				break;
			case 2:
				circleDialog.dismiss();
				new AlertDialog.Builder(RegisterActivity.this)
						.setTitle("�Բ���")
						.setMessage("���ڵ����粻���ã��Ժ�����!")
						.setNegativeButton("ȷ��",
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
				circleDialog.dismiss();
				new AlertDialog.Builder(RegisterActivity.this)
						.setTitle("�Բ���")
						.setMessage("���û����Ѿ���ʹ�ã�������")
						.setNegativeButton("ȷ��",
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
