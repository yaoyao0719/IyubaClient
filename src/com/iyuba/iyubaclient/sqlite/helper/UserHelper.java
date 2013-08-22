/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.helper;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.ths.frame.database.DatabaseService;
import org.ths.frame.database.EntityBean;

import android.database.Cursor;
import android.util.Log;
import android.widget.ListAdapter;

import com.iyuba.iyubaclient.sqlite.entity.UserInfo;


/**
 * @author yao
 * 用户信息表
 */
public class UserHelper {

	private DatabaseService databaseService;
	public UserHelper(DatabaseService databaseService){
		this.databaseService=databaseService;
	}
	//插入数据库
	public long insertUserData(UserInfo userInfo){
		long count =databaseService.insertData(userInfo);
		return count;
	}
	//插入数据库
	public long insertUserDataForSingle(UserInfo userInfo){
		long count =databaseService.insertData(userInfo);
		Cursor cursor=null;
		String sql="select * from "+new UserInfo().getTableName()+" where uid= '"+userInfo.uid+"'";
		try{
			cursor=databaseService.fetch(sql);
			if(cursor!=null){
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					userInfo.username=cursor.getString(cursor.getColumnIndex("username"));
					//用户已存在，更新数据
					databaseService.updateData(userInfo);
				}
				cursor.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}
	//批量插入数据库
	public long insertUserData(ArrayList<UserInfo> userInfos){
		ArrayList<EntityBean> insertDataArrayList=new ArrayList<EntityBean>();
		insertDataArrayList.addAll(userInfos);
		long count =databaseService.insertData(insertDataArrayList);		
		return count;
	}
	//更新数据
	public boolean updataUserData(UserInfo userInfo){
		return databaseService.updateData(userInfo);
		
	}
	//批量更新数据
	public boolean updataUserData(List<UserInfo> userInfos){
		ArrayList<EntityBean> insertDataArrayList=new ArrayList<EntityBean>();
		insertDataArrayList.addAll(userInfos);
		return databaseService.updateData(insertDataArrayList)>0;
		
	}
	//根据用户Id 获取用户信息
	public UserInfo getUserInfo(String uid){
		UserInfo userInfo=new UserInfo();
		Cursor cursor=null;
		String sql="select * from "+new UserInfo().getTableName()+" where uid= '"+uid+"'";
		try{
			cursor=databaseService.fetch(sql);
			if(cursor!=null){
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					userInfo.username=cursor.getString(cursor.getColumnIndex("username"));
					
				}
				cursor.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		databaseService.close();
		return userInfo;
	}
	
}

