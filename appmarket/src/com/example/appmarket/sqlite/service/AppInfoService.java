package com.example.appmarket.sqlite.service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmarket.sqlite.model.ApplicationInfo;

public class AppInfoService extends BaseClientDataBaseService {

	private Context mContext;
	private SQLiteDatabase db;// �������

	public AppInfoService(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * ��ȡ��װ��Ӧ���б�
	 * 
	 * @return
	 */
	public ArrayList<ApplicationInfo> getInstalledApp() {
		if (!db.isOpen()) {
			db = openDatabase();
		}
		ArrayList<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
		String sql = "select * from app app where app.install_state=1";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				ApplicationInfo info = loadApplcationInfo(cursor);
				applicationInfos.add(i, info);
				cursor.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return applicationInfos;
	}

	/**
	 * ��ȡ�ɰ�װ��Ӧ���б�
	 * 
	 * @return
	 */
	public ArrayList<ApplicationInfo> getCanIntallApp() {
		if (!db.isOpen()) {
			db = openDatabase();
		}
		ArrayList<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
		String sql = "select * from app app where app.install_state=0 and app.apk_path!=null";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				ApplicationInfo info = loadApplcationInfo(cursor);
				applicationInfos.add(i, info);
				cursor.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return applicationInfos;
	}

	/**
	 * 
	 * ����һ��Application�����������Ϣ
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateApplicationInfo(ApplicationInfo info) {
		String sqlString = "update app set category_id=?,app_name=?, icon_url=?,"
				+ "description=?,size=?,version=?,install_state=?,"
				+ "apk_path=? where app_id=?";
		Object[] objects = { info.category_id, info.app_name, info.icon_url,
				info.description, info.size, info.version, info.install_state,
				info.apk_path, info.app_id };
		return exeSql(sqlString, objects);
	}

	/**
	 * 
	 * ��app�������һ����¼
	 * 
	 * @param info
	 * @return
	 */
	public boolean insertApplicationInfo(ApplicationInfo info) {
		String sqlString = "insert into app(appid,category_id,app_name,icon_url,"
				+ "description,size,version,install_state,apk_path"
				+ " values(?,?,?,?,?,?,?,?,?)";
		Object[] objects = { info.app_id, info.category_id, info.app_name,
				info.icon_url, info.description, info.size, info.version,
				info.install_state, info.apk_path };
		return exeSql(sqlString, objects);
	}

	/**
	 * 
	 * ��cursor�������app����Ϣ
	 * 
	 * @param cursor
	 * @return
	 */
	public ApplicationInfo loadApplcationInfo(Cursor cursor) {
		ApplicationInfo info = new ApplicationInfo();
		info.app_id = cursor.getInt(cursor
				.getColumnIndex(ApplicationInfo.FIELD_APP_ID));
		info.category_id = cursor.getInt(cursor
				.getColumnIndex(ApplicationInfo.FIELD_CATRGORY_ID));
		info.app_name = cursor.getString(cursor
				.getColumnIndex(ApplicationInfo.FIELD_APP_NAME));
		info.description = cursor.getString(cursor
				.getColumnIndex(ApplicationInfo.FIELD_DESCRIPPTION));
		info.icon_url = cursor.getString(cursor
				.getColumnIndex(ApplicationInfo.FIELD_ICON_URL));
		info.install_state = cursor.getInt(cursor
				.getColumnIndex(ApplicationInfo.FIELD_INSTALL_STATE));
		info.size = cursor.getFloat(cursor
				.getColumnIndex(ApplicationInfo.FIELD_SIZE));
		info.version = cursor.getString(cursor
				.getColumnIndex(ApplicationInfo.FIELD_VERSION));
		return info;
	}
}
