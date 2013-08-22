/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.entity.Attention;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 *
 */
public class ResponseAttentionList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public Fans fan = new Fans();
	public JSONArray data;
	public ArrayList<Attention> fansList;
	public int num;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		fansList = new ArrayList<Attention>();
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
		if (result.equals("550")) {
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
						Attention fans = new Attention();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						fans.bkname = jsonObject.getString("bkname");
						fans.mutual = jsonObject.getString("mutual");
						fans.followuid = jsonObject.getString("followuid");
						fans.dateline = jsonObject.getString("dateline");
						fans.fusername = jsonObject.getString("fusername");
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
