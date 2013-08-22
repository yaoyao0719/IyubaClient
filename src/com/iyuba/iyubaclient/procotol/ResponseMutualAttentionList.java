/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.entity.MutualAttention;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 * 相互关注列表
 */
public class ResponseMutualAttentionList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	//public MutualAttention fan = new MutualAttention();
	public JSONArray data;
	public ArrayList<MutualAttention> list;
	public int num;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<MutualAttention>();
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
		if (result.equals("570")) {
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
						MutualAttention item = new MutualAttention();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						item.bkname = jsonObject.getString("bkname");					
						item.dateline = jsonObject.getString("dateline");
						item.followuid=jsonObject.getString("followuid");
						item.fusername=jsonObject.getString("fusername");
						item.status=jsonObject.getString("status");
						list.add(item);
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

