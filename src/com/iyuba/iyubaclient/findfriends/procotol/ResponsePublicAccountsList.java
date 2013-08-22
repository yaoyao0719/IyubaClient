/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.PublicAccountInfo;
import com.iyuba.iyubaclient.sqlite.entity.SameAppFriendInfo;

/**
 * @author yao
 *
 */
public class ResponsePublicAccountsList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<PublicAccountInfo> list;
    public String  pageNumber;
    public String  totalPage;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<PublicAccountInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			pageNumber = jsonBody.getString("pageNumber");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			totalPage = jsonBody.getString("totalPage");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (result.equals("141")) {
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						PublicAccountInfo item = new PublicAccountInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));						
						item.uid = jsonObject.getString("uid");												
						item.username = jsonObject.getString("username");
						item.ps = jsonObject.getString("ps");
						item.followers=jsonObject.getString("followers");
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




