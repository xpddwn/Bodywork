package com.example.appmarket.sqlite.service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmarket.entity.AppMarket;

public class AppInfoService extends BaseClientDataBaseService {

	private Context mContext;
	private SQLiteDatabase db;

	public AppInfoService(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		db=openDatabase();
	}

	/**
	 * 
	 * 获取所有安装的app
	 * @return
	 */
	public ArrayList<AppMarket> getInstalledApp() {
		if (!db.isOpen()) {
			db = openDatabase();
		}
		ArrayList<AppMarket> AppMarkets = new ArrayList<AppMarket>();
		String sql = "select * from app app where app.install_state=1";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				AppMarket info = loadApplcationInfo(cursor);
				AppMarkets.add(i, info);
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
		return AppMarkets;
	}

	/**
	 * 
	 * 获取可安装的app
	 * @return
	 */
	public ArrayList<AppMarket> getCanIntallApp() {
		if (!db.isOpen()) {
			db = openDatabase();
		}
		ArrayList<AppMarket> AppMarkets = new ArrayList<AppMarket>();
		String sql = "select * from app app where app.install_state=0 and app.apk_path!=null";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				AppMarket info = loadApplcationInfo(cursor);
				AppMarkets.add(i, info);
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
		return AppMarkets;
	}
	
	/**
	 * 
	 * 更新应用的信息
	 * @param info
	 * @return
	 */
	public boolean updateAppMarket(AppMarket info) {
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
	 * 插入一条应用信息
	 * @param info
	 * @return
	 */
	public boolean insertAppMarket(AppMarket info) {
		String sqlString = "insert into app(app_id,category_id,app_name,icon_url,"
				+ "description,size,version,install_state,apk_path)"
				+ " values(?,?,?,?,?,?,?,?,?)";
		Object[] objects = { info.app_id, info.category_id, info.app_name,
				info.icon_url, info.description, info.size, info.version,
				info.install_state, info.apk_path };
		return exeSql(sqlString, objects);
	}

	/**
	 * 
	 * 从cursor中获取应用的信息
	 * @param cursor
	 * @return
	 */
	public AppMarket loadApplcationInfo(Cursor cursor) {
		AppMarket info = new AppMarket();
		info.app_id = cursor.getInt(cursor
				.getColumnIndex(AppMarket.FIELD_APP_ID));
		info.category_id = cursor.getInt(cursor
				.getColumnIndex(AppMarket.FIELD_CATRGORY_ID));
		info.app_name = cursor.getString(cursor
				.getColumnIndex(AppMarket.FIELD_APP_NAME));
		info.description = cursor.getString(cursor
				.getColumnIndex(AppMarket.FIELD_DESCRIPPTION));
		info.icon_url = cursor.getString(cursor
				.getColumnIndex(AppMarket.FIELD_ICON_URL));
		info.install_state = cursor.getInt(cursor
				.getColumnIndex(AppMarket.FIELD_INSTALL_STATE));
		info.size = cursor.getFloat(cursor
				.getColumnIndex(AppMarket.FIELD_SIZE));
		info.version = cursor.getString(cursor
				.getColumnIndex(AppMarket.FIELD_VERSION));
		return info;
	}
}
