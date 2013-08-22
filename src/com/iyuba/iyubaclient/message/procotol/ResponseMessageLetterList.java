/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;

/**
 * @author yao 私信列表
 */
public class ResponseMessageLetterList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<MessageLetter> list;
	public int num;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<MessageLetter>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			firstPage = jsonBody.getInt("firstPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			prevPage = jsonBody.getInt("prevPage");
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
		if (result.equals("601")) {
		
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						MessageLetter letter = new MessageLetter();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						letter.friendid = jsonObject.getString("friendid");
						letter.pmnum = jsonObject.getInt("pmnum");
						letter.lastmessage = jsonObject
								.getString("lastmessage");
						letter.name = jsonObject.getString("name");
						letter.dateline = jsonObject.getString("dateline");
						letter.plid = jsonObject.getString("plid");
						letter.isnew = jsonObject.getString("isnew");

						list.add(letter);
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
