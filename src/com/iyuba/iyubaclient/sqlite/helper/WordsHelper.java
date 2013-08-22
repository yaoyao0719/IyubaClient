package com.iyuba.iyubaclient.sqlite.helper;

import java.util.ArrayList;
import java.util.List;

import org.ths.frame.database.DatabaseService;
import org.ths.frame.database.EntityBean;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.sqlite.entity.Words;

import android.database.Cursor;
import android.util.Log;

public class WordsHelper {
	private DatabaseService databaseServiec;

	public WordsHelper(DatabaseService databaseServiec) {
		this.databaseServiec = databaseServiec;
	}

	/**
	 * 插入数据库
	 * 
	 * @param Wordss
	 * @return
	 */
	public long insertWordsData(Words word) {
		long count = databaseServiec.insertData(word);
		return count;
	}
	
	/**
	 * 插入前先判断数据库中是否存在此单词
	 * @param word
	 * @return
	 */
	public long insertWordsDataForSingle(Words word) {
		List<String> uniqFields=new ArrayList<String>();
		uniqFields.add("key");
		uniqFields.add("forUser");
//		Log.e("##########", word.key+"|"+word.forUser);
		long count = databaseServiec.insertDataForSingle(word,uniqFields,new String[]{word.key,word.forUser});
		
		return count;
	}
	
	/**
	 * 批量插入数据库
	 * 
	 * @param Wordss
	 * @return
	 */
	public long insertWordssData(ArrayList<Words> words) {
		ArrayList<EntityBean> insertData = new ArrayList<EntityBean>();
		insertData.addAll(words);
		long count = databaseServiec.insertData(insertData);
		return count;
	}

	/**
	 * 更新数据
	 * 
	 * @param Words
	 * @return
	 */
	public boolean updataWordsData(Words word) {
		return databaseServiec.updateData(word);
	}
	
	/**
	 * 批量更新数据
	 * 
	 * @param Words
	 * @return
	 */
	public boolean updataWordsData(List<Words> words) {
		ArrayList<EntityBean> insertData = new ArrayList<EntityBean>();
		insertData.addAll(words);
		return databaseServiec.updateData(insertData)>0;
	}

	public ArrayList<Words> getWords() {
		ArrayList<Words> Wordss = new ArrayList<Words>();
		Cursor cursor = databaseServiec
				.fetchData(new Words().getTableName());
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Words WordsTemp = new Words();
				WordsTemp._id = cursor.getInt(cursor.getColumnIndex("_id"));
				WordsTemp.key = cursor.getString(cursor
						.getColumnIndex("key"));
				WordsTemp.pron = cursor.getString(cursor
						.getColumnIndex("pron"));
				WordsTemp.def = cursor.getString(cursor
						.getColumnIndex("def"));
				WordsTemp.audio = cursor.getString(cursor
						.getColumnIndex("audio"));
				WordsTemp.fromApp = cursor.getString(cursor
						.getColumnIndex("fromApp"));
				WordsTemp.forUser = cursor.getString(cursor
						.getColumnIndex("forUser"));
				WordsTemp.status = cursor.getInt(cursor
						.getColumnIndex("status"));
				Wordss.add(WordsTemp);
			}
		}
		databaseServiec.close();
		return Wordss;
	}

	/**
	 * 部分类别获取数据
	 * 
	 * @param count
	 * @param offset
	 * @return
	 */
	public ArrayList<Words> getWordss(int count, int offset) {
		ArrayList<Words> Wordss = new ArrayList<Words>();
		Cursor cursor = databaseServiec.fetchData(
				new Words().getTableName(), count, offset, "_id");
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Words WordsTemp = new Words();
				WordsTemp._id = cursor.getInt(cursor.getColumnIndex("_id"));
				WordsTemp.key = cursor.getString(cursor
						.getColumnIndex("key"));
				WordsTemp.pron = cursor.getString(cursor
						.getColumnIndex("pron"));
				WordsTemp.def = cursor.getString(cursor
						.getColumnIndex("def"));
				WordsTemp.audio = cursor.getString(cursor
						.getColumnIndex("audio"));
				WordsTemp.fromApp = cursor.getString(cursor
						.getColumnIndex("fromApp"));
				WordsTemp.forUser = cursor.getString(cursor
						.getColumnIndex("forUser"));
				WordsTemp.status = cursor.getInt(cursor
						.getColumnIndex("status"));
				Wordss.add(WordsTemp);
			}
		}
		databaseServiec.close();
		return Wordss;
	}

	/**
	 * 条件筛选查询
	 * @param count
	 * @param offset
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public ArrayList<Words> getWordss(int count, int offset,
			String fieldName, String value) {
		ArrayList<Words> Wordss = new ArrayList<Words>();
		Cursor cursor = databaseServiec.fetchData(
				new Words().getTableName(), count, offset, "_id",
				fieldName + "=?", new String[] { value });
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Words WordsTemp = new Words();
				WordsTemp._id = cursor.getInt(cursor.getColumnIndex("_id"));
				WordsTemp.key = cursor.getString(cursor
						.getColumnIndex("key"));
				WordsTemp.pron = cursor.getString(cursor
						.getColumnIndex("pron"));
				WordsTemp.def = cursor.getString(cursor
						.getColumnIndex("def"));
				WordsTemp.audio = cursor.getString(cursor
						.getColumnIndex("audio"));
				WordsTemp.fromApp = cursor.getString(cursor
						.getColumnIndex("fromApp"));
				WordsTemp.forUser = cursor.getString(cursor
						.getColumnIndex("forUser"));
				WordsTemp.status = cursor.getInt(cursor
						.getColumnIndex("status"));
				Wordss.add(WordsTemp);
			}
		}
		databaseServiec.close();
		return Wordss;
	}
	/**
	 * 条件筛选查询
	 * @param count
	 * @param offset
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public Words getWord(String uid) {
		Words word=new Words();

		StringBuffer selection = new StringBuffer();
		selection.append("");
		Cursor cursor=databaseServiec.fetchDataRandom(uid);
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Words WordsTemp = new Words();
				WordsTemp._id = cursor.getInt(cursor.getColumnIndex("_id"));
				WordsTemp.key = cursor.getString(cursor
						.getColumnIndex("key"));
				WordsTemp.pron = cursor.getString(cursor
						.getColumnIndex("pron"));
				WordsTemp.def = cursor.getString(cursor
						.getColumnIndex("def"));
				WordsTemp.audio = cursor.getString(cursor
						.getColumnIndex("audio"));
				WordsTemp.fromApp = cursor.getString(cursor
						.getColumnIndex("fromApp"));
				WordsTemp.forUser = cursor.getString(cursor
						.getColumnIndex("forUser"));
				WordsTemp.status = cursor.getInt(cursor
						.getColumnIndex("status"));
				word=WordsTemp;
			}
		}
		databaseServiec.close();
		return word;
	}

	/**
	 * 条件筛选查询
	 * @param fieldName
	 * @param value
	 * @param fieldName_2 
	 * @param value_2 !=
	 * @param groupBy 
	 * @return
	 */
	public ArrayList<Words> getWordss(
			String selection, String[] values,String groupBy) {
		ArrayList<Words> Wordss = new ArrayList<Words>();
		Cursor cursor = databaseServiec.fetchData(
				new Words().getTableName(), "_id",
				selection , values,groupBy);
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Words WordsTemp = new Words();
				WordsTemp._id = cursor.getInt(cursor.getColumnIndex("_id"));
				WordsTemp.key = cursor.getString(cursor
						.getColumnIndex("key"));
				WordsTemp.pron = cursor.getString(cursor
						.getColumnIndex("pron"));
				WordsTemp.def = cursor.getString(cursor
						.getColumnIndex("def"));
				WordsTemp.audio = cursor.getString(cursor
						.getColumnIndex("audio"));
				WordsTemp.fromApp = cursor.getString(cursor
						.getColumnIndex("fromApp"));
				WordsTemp.forUser = cursor.getString(cursor
						.getColumnIndex("forUser"));
				WordsTemp.status = cursor.getInt(cursor
						.getColumnIndex("status"));
				Wordss.add(WordsTemp);
			}
		}
		databaseServiec.close();
		return Wordss;
	}
	
	public boolean deleteWord(Words word){
		return databaseServiec.deleteData(word)>0;
	}
	
	public boolean deleteWord(String key,String value){
		return databaseServiec.deleteData(new Words().getTableName(),key,value)>0;
	}
	
	public boolean deleteWord(List<Words> words){
		ArrayList<EntityBean> delEB=new ArrayList<EntityBean>();
		delEB.addAll(words);
		return databaseServiec.deleteData(delEB)>0;
	}
	
}
