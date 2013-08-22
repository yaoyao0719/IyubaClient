/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import android.R.integer;
import android.app.Notification;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetterContent;
import com.iyuba.iyubaclient.sqlite.entity.NotificationInfo;

/**
 * @author yao
 * 通知 
 */
public class ResponseNotificationInfo extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public String uid;
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<NotificationInfo> list;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list=new ArrayList<NotificationInfo>();
		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}try {
			prevPage = jsonBody.getInt("prevPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			firstPage = jsonBody.getInt("firstPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			nextPage = jsonBody.getInt("nextPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lastPage = jsonBody.getInt("lastPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			uid = jsonBody.getString("uid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("631")){
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(data!=null&&data.length()!=0){
				for(int i=0;i<data.length();i++){
					try{
						JSONObject jsonObject=((JSONObject) data.opt(i));
						NotificationInfo item=new NotificationInfo();
						item.author=jsonObject.getString("author");
						item.authorid=jsonObject.getString("authorid");
						item.dateline=jsonObject.getString("dateline");
						item.from_id=jsonObject.getString("from_id");
						item.from_idtype=jsonObject.getString("from_idtype");
						item.from_num=jsonObject.getString("from_num");
						item.id=jsonObject.getString("id");
						item.isnew=jsonObject.getString("new");
						item.note=jsonObject.getString("note");
						item.type=jsonObject.getString("type");
						list.add(item);
					}catch(JSONException e){
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	

}
