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
 * 功能：拷贝res/raw目录下数据库文件到data中，防止用户误删
 * 参考：http://www.cnblogs.com/xiaowenji/archive/2011/01/03/1925014.html
 * 
 *
 */
public abstract class BaseDataBaseManager {
	public static final String TAG="BaseDataBaseManager";
	private final int BUFFER_SIZE = 3000000;
	private String dbName  ;//保存的数据库文件名
	private String  dbPath; //在手机里存放数据库的位置
	private SQLiteDatabase database;
	private Context context;
	private int rarResource;
	private int version=1;//默认的version	
	
	public String getDbName() {
		return dbName;
	}
	public String getDbPath() {
		return dbPath;
	}
	
	/**
	 * 
	 * 
	 * @param context  上下文
	 * @param rawResource 数据库资源文件
	 * @param dbName 保存在手机上的数据库的名称
	 */
	public BaseDataBaseManager(Context context,int rawResource,String dbName,String dbPath) {
	    this.context = context;
	    this.rarResource=rawResource;
	    this.dbName=dbName;
	    this.dbPath =dbPath;
	}
	public BaseDataBaseManager(Context context,int rawResource,String dbName,int version,String dbPath) {
	    this.context = context;
	    this.rarResource=rawResource;
	    this.dbName=dbName;
	    this.dbPath =dbPath;
	    this.version=version;
	}
	
	  /**
	   * 
	   * 
	   * 功能：初始化时调用
	   */
	  public SQLiteDatabase openDatabase() {
	    this.database = this.openDatabase(dbPath + "/" + dbName);
	    return database;
	  }
	  
	  
	  /**
	   * 
	   * @param dbfile
	   * @return
	   * 功能：操作指定地址的数据库文件时调用
	   */
	  private SQLiteDatabase openDatabase(String dbfile) {
		SQLiteDatabase db=null;
	    try {
	     if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
	        InputStream is = this.context.getResources().openRawResource(rarResource); //欲导入的数据库
	        FileOutputStream fos = new FileOutputStream(dbfile);
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int count = 0;
	        while ((count = is.read(buffer)) > 0) {
	          fos.write(buffer, 0, count);
	        }
	        fos.close();
	        is.close();
	        db = SQLiteDatabase.openDatabase(dbfile,null,SQLiteDatabase.OPEN_READWRITE);
	  	    db.setVersion(version);
	      }else{
	    	  db = SQLiteDatabase.openDatabase(dbfile,null,SQLiteDatabase.OPEN_READWRITE);
	    	  if(db!=null){//检查版本更新
	    		  int kk=db.getVersion();
	    		  if(db.getVersion()<version){
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
	  }//do something else here  
	  public void closeDatabase() {
	    this.database.close();
	  }
	  abstract protected void onUpdate(SQLiteDatabase db);
	}