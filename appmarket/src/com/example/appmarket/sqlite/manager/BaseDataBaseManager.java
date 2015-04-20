package com.example.appmarket.sqlite.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * 
 * ���ܣ�����res/rawĿ¼����ݿ��ļ���data�У���ֹ�û���ɾ
 * �ο���http://www.cnblogs.com/xiaowenji/archive/2011/01/03/1925014.html
 * 
 * 
 */
public abstract class BaseDataBaseManager {
	public static final String TAG = "BaseDataBaseManager";
	private final int BUFFER_SIZE = 3000000;
	private String dbName;// �������ݿ��ļ���
	private String dbPath; // ���ֻ�������ݿ��λ��
	private SQLiteDatabase database;
	private Context context;
	private int rarResource;
	private int version = 1;// Ĭ�ϵ�version

	public String getDbName() {
		return dbName;
	}

	public String getDbPath() {
		return dbPath;
	}

	
	public BaseDataBaseManager(Context context, int rawResource, String dbName,
			String dbPath) {
		this.context = context;
		this.rarResource = rawResource;
		this.dbName = dbName;
		this.dbPath = dbPath;
	}

	public BaseDataBaseManager(Context context, int rawResource, String dbName,
			int version, String dbPath) {
		this.context = context;
		this.rarResource = rawResource;
		this.dbName = dbName;
		this.dbPath = dbPath;
		this.version = version;
	}

	/**
	 * 
	 * 
	 * ���ܣ���ʼ��ʱ����
	 */
	public SQLiteDatabase openDatabase() {
		this.database = this.openDatabase(dbPath + "/" + dbName);
		return database;
	}

	/**
	 * 
	 * @param dbfile
	 * @return ���ܣ�����ָ����ַ����ݿ��ļ�ʱ����
	 */
	private SQLiteDatabase openDatabase(String dbfile) {
		SQLiteDatabase db = null;
		try {
			if (!(new File(dbfile).exists())) {// �ж���ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ���ݿ�
				InputStream is = this.context.getResources().openRawResource(
						rarResource); // �������ݿ�
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
				db = SQLiteDatabase.openDatabase(dbfile, null,
						SQLiteDatabase.OPEN_READWRITE);
				db.setVersion(version);
			} else {
				db = SQLiteDatabase.openDatabase(dbfile, null,
						SQLiteDatabase.OPEN_READWRITE);
				if (db != null) {// ���汾����
					int kk = db.getVersion();
					if (db.getVersion() < version) {
						onUpdate(db);
						db.setVersion(version);
					}
				}
			}
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}// do something else here

	public void closeDatabase() {
		this.database.close();
	}

	abstract protected void onUpdate(SQLiteDatabase db);
}