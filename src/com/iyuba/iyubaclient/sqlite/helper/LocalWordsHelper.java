/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.helper;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.sqlite.entity.WordSentence;
import com.iyuba.iyubaclient.sqlite.entity.Words;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author yao
 * 本地数据库
 */
public class LocalWordsHelper {
	public String TABLE_WORD="Words";//单词表
	public String TABLE_SENTENCE="sentences";//例句表
	
	public String FIELD_ID="id";//单词ID
	public String FIELD_WORD="Word";//单词
	public String FIELD_AUDIO="audio";//音频地址
	public String FIELD_PRON="pron";//音标
	public String FIELD_DEF="def";//单词中文意思
	public String FIELD_VIEWCOUNT="viewCount";//0
	public String FIELD_CREATEDATE="CreateDate";//创建时间
	public String FIELD_IDINDEX="idIndex";//
	public String FIELD_ORIG="orig";//
	public String FIELD_TRANS="trans ";//
	
	private SQLiteDatabase mDB=null;
	private Context mContext;
	//打开拷贝上的数据库，若不存在则重新拷贝
	public LocalWordsHelper(Context context){
		mContext=context;
		mDB=SQLiteDatabase.openOrCreateDatabase(InitWordDB.DB_PATH+"/"+InitWordDB.DB_NAME,null);
	}
	//打开数据库
	private SQLiteDatabase openDatabase(){
		return SQLiteDatabase.openOrCreateDatabase(InitWordDB.DB_PATH+"/"+InitWordDB.DB_NAME,null);
	}
	
	public Boolean isLocalWord(String word){
		if(!mDB.isOpen()){
			mDB=openDatabase();
		}
		Cursor cursor=null;
		String sql="select * from "+TABLE_WORD+" where word='"+word+"'";
		try{
			cursor=mDB.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				return true;
			}
			cursor.close();
			mDB.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return false;	
	}
	public Words SearchWord(String word){
		if(!mDB.isOpen()){
			mDB=openDatabase();
		}
		Words words=new Words();
		Cursor cursor=null;
		String sql="select * from "+TABLE_WORD+" where word='"+word+"'";
		try{
			cursor=mDB.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				words.audio=cursor.getString(cursor.getColumnIndex("audio"));
				words.key=cursor.getString(cursor.getColumnIndex("Word"));
				words.pron=URLDecoder.decode(cursor.getString(cursor.getColumnIndex("pron")));
				words.def=cursor.getString(cursor.getColumnIndex("def"));
				
			}
			cursor.close();
			mDB.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return words;	
	}
	public ArrayList<WordSentence> SearchWordSentences(String word){
		if(!mDB.isOpen()){
			mDB=openDatabase();
		}
		ArrayList<WordSentence> sentences=new ArrayList<WordSentence>();
		Cursor cursor=null;
		String sql="select * from "+TABLE_SENTENCE+" where word='"+word+"'";
		try{
			cursor=mDB.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				for(int i=0,count = cursor.getCount();i<count;i++){
					WordSentence sentence=new WordSentence();
					sentence.orig=cursor.getString(cursor.getColumnIndex("orig"));
					sentence.trans=cursor.getString(cursor.getColumnIndex("trans"));
					sentence.word=word;
					cursor.moveToNext();
					sentences.add(sentence);
				}
			}
			cursor.close();
			mDB.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return sentences;	
	}
	public Words SearchWordRandom(){
		if(!mDB.isOpen()){
			mDB=openDatabase();
		}
		Words words=new Words();
		Cursor cursor=null;
		String sql="select * from "+TABLE_WORD+" order by RANDOM() limit 1";
		try{
			cursor=mDB.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				words.audio=cursor.getString(cursor.getColumnIndex("audio"));
				words.key=cursor.getString(cursor.getColumnIndex("Word"));
				words.pron=URLDecoder.decode(cursor.getString(cursor.getColumnIndex("pron")));
				words.def=cursor.getString(cursor.getColumnIndex("def"));
			}
			cursor.close();
			mDB.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return words;	
	}
}
