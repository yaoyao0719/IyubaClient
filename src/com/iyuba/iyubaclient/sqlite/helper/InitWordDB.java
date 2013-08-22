/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.iyuba.iyubaclient.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


/**
 * @author yao
 * 初始化单词数据库，包括单词表和例句表
 */
public class InitWordDB {
	  private final int BUFFER_SIZE = 400000;
	  public static final String DB_NAME = "words.sqlite"; //保存的数据库文件名
	  public static final String PACKAGE_NAME = "com.iyuba.iyubaclient";
	  public static final String DB_PATH = "/data"
	      + Environment.getDataDirectory().getAbsolutePath() + "/"
	      + PACKAGE_NAME;  //在手机里存放数据库的位置

	  private SQLiteDatabase database;
	  private Context context;

	  public InitWordDB(Context context) {
	    this.context = context;
	    
	  }
	  /**
	   * 
	   * 
	   * 功能：初始化时调用
	   */
	  public void openDatabase() {
	    this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	  }

	  /**
	   * 
	   * @param dbfile
	   * @return
	   * 功能：操作指定地址的数据库文件时调用
	   */
	  private SQLiteDatabase openDatabase(String dbfile) {
	    try {
	      if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
	        InputStream is = this.context.getResources().openRawResource(R.raw.words); //欲导入的数据库
	        FileOutputStream fos = new FileOutputStream(dbfile);
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int count = 0;
	        while ((count = is.read(buffer)) > 0) {
	          fos.write(buffer, 0, count);
	        }
	        fos.close();
	        is.close();
	      }
	      SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
	          null);
	      return db;
	    } catch (FileNotFoundException e) {
//	      Log.e("Database", "File not found");
	      e.printStackTrace();
	    } catch (IOException e) {
//	      Log.e("Database", "IO exception");
	      e.printStackTrace();
	    }
	    return null;
	  }//do something else here  
	  public void closeDatabase() {
	    this.database.close();
	  }
	}

