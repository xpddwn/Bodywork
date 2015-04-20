package com.example.appmarket.sqlite.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmarket.sqlite.manager.ClientDataBase;

public abstract class BaseClientDataBaseService {
	private SQLiteDatabase database = null;
	private Context mContext;

	public BaseClientDataBaseService(Context context) {
		mContext = context;
		database = openDatabase();
	}

	/**
	 * 打开数据库
	 * @return
	 */
	public SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(ClientDataBase.DB_PATH + "/"
				+ ClientDataBase.DB_NAME, null);
	}

	/**
	 * 
	 * ��װ�ĸ���ִ����ݿ����
	 * 
	 */
	public boolean exeSql(String sqlString) {
		boolean flag;
		if (!database.isOpen()) {
			database = openDatabase();
		}
		try {
			database.execSQL(sqlString);
			database.close();
			flag = true;
		} catch (Exception e) {
			flag = false;
			// TODO: handle exception
		} finally {
			if (database.isOpen()) {
				database.close();
			}
		}
		return flag;
	}

	public boolean exeSql(String sqlString, Object[] objects) {
		boolean flag;
		if (!database.isOpen()) {
			database = openDatabase();
		}
		try {
			database.execSQL(sqlString, objects);
			database.close();
			flag = true;
		} catch (Exception e) {
			flag = false;
			// TODO: handle exception
		} finally {
			if (database.isOpen()) {
				database.close();
			}
		}
		return flag;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

}
