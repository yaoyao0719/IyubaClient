/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.BlogInfo;

/**
 * @author yao 粉丝列表
 */
public class ResponseFansList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public Fans fan = new Fans();
	public JSONArray data;
	public ArrayList<Fans> fansList;
	public int num;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		fansList = new ArrayList<Fans>();
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
		if (result.equals("560")) {
			try {
				num = jsonBody.getInt("num");

			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						Fans fans = new Fans();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						fans.mutual = jsonObject.getString("mutual");
						fans.uid = jsonObject.getString("uid");
						fans.dateline = jsonObject.getString("dateline");
						fans.username = jsonObject.getString("username");
						fans.doing=jsonObject.getString("doing");
						fansList.add(fans);
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
						
					}
				}

			}

		}
		return false;
	}

}
