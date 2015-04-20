package com.example.appmarket.sqlite.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.appmarket.R;
import com.example.appmarket.constant.Constant;

public class ClientDataBase extends BaseDataBaseManager {

	public static final String TAG = "client database";
	public static final int VERSION = 3;
	public static final String DB_NAME = "appmarket.sqlite";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ Constant.PACKAGE_NAME;

	private Context mContext;

	public ClientDataBase(Context context) {
		super(context, R.raw.appmarket, DB_NAME, VERSION, DB_PATH);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected void onUpdate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.e(TAG, "version=" + db.getVersion());
	}

}
