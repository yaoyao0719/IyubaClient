/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.helper;

import java.util.ArrayList;
import java.util.List;

import org.ths.frame.database.DatabaseService;
import org.ths.frame.database.EntityBean;

import android.database.Cursor;
import android.util.Log;

import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;

/**
 * @author yao
 *
 */
public class FeedInfoHelper {

	private DatabaseService databaseService;
	public FeedInfoHelper (DatabaseService databaseService){
		this.databaseService=databaseService;
	}
	//插入数据库
	public long insertFeedInfo(FeedInfo feedInfo){
		long count =databaseService.insertData(feedInfo);
		return count;
	}
	//批量插入数据库
	public long insertFeedInfo(ArrayList<FeedInfo> feedInfos){
		ArrayList<EntityBean> insertDataArrayList=new ArrayList<EntityBean>();
		insertDataArrayList.addAll(feedInfos);
		long count =databaseService.insertData(insertDataArrayList);		
		return count;
	}
	//更新数据
	public boolean updataFeedInfo(FeedInfo FeedInfo){
		return databaseService.updateData(FeedInfo);
		
	}
	//批量更新数据
	public boolean updataUserData(List<FeedInfo> feedInfos){
		ArrayList<EntityBean> insertDataArrayList=new ArrayList<EntityBean>();
		insertDataArrayList.addAll(feedInfos);
		return databaseService.updateData(insertDataArrayList)>0;
		
	}
	//根据用户Id 获取用户信息
	public FeedInfo getFeedInfo(String uid,String id){
		FeedInfo FeedInfo=new FeedInfo();
		Cursor cursor=null;
		String sql="select * from "+new FeedInfo().getTableName()+" where uid= '"+uid+"'"+" and id= '"+id+"'";
		try{
			cursor=databaseService.fetch(sql);
			if(cursor!=null){
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					FeedInfo.username=cursor.getString(cursor.getColumnIndex("username"));
					
				}
				cursor.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		databaseService.close();
		return FeedInfo;
	}
	//根据用户Id 获取用户信息
	public ArrayList<FeedInfo> getFeedInfoList(String uid){
		
		ArrayList<FeedInfo> list=new ArrayList<FeedInfo>();
		Cursor cursor=null;
		String sql="select * from "+new FeedInfo().getTableName()+" where uid= '"+uid+"'";
		try{
			cursor=databaseService.fetch(sql);
			if(cursor!=null){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					FeedInfo feedInfo=new FeedInfo();
					feedInfo.body=cursor.getString(cursor.getColumnIndex("body"));
					feedInfo.dateline=cursor.getString(cursor.getColumnIndex("dateline"));
					feedInfo.username=cursor.getString(cursor.getColumnIndex("username"));
					feedInfo.title=cursor.getString(cursor.getColumnIndex("title"));
					feedInfo.id=cursor.getString(cursor.getColumnIndex("id"));
					feedInfo.idtype=cursor.getString(cursor.getColumnIndex("idtype"));
					feedInfo.hot=cursor.getString(cursor.getColumnIndex("hot"));
					feedInfo.uid=cursor.getString(cursor.getColumnIndex("uid"));
					feedInfo.feedid=cursor.getString(cursor.getColumnIndex("feedid"));				
					list.add(feedInfo);
				}
				cursor.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		databaseService.close();
		Log.e("list",""+list.size());
		return list;
	}
}

