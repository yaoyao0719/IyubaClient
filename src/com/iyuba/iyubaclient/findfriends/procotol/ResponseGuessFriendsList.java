/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.GuessFriendInfo;

/**
 * @author yao
 * 猜你认识好友列表
 */
public class ResponseGuessFriendsList  extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<GuessFriendInfo> list;
    public Boolean isLastPage=false;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<GuessFriendInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if (result.equals("591")) {
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						GuessFriendInfo item = new GuessFriendInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						
						item.uid = jsonObject.getString("uid");
						item.vip = jsonObject.getString("vip");
						item.avatar = jsonObject.getString("avatar");
						item.doing = jsonObject.getString("doing");									
						item.username = jsonObject.getString("username");
						item.gender = jsonObject.getString("gender");
						item.realname = jsonObject.getString("realname");
						list.add(item);
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			}

		}else if(result.equals("592")){
			isLastPage=true;
		}
		return false;
	}
}



