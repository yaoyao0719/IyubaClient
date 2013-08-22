/**
 * 
 */
package com.iyuba.iyubaclient.feed.procotol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.BlogInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;

/**
 * @author yao
 * 
 */
public class ResponseDoingById extends VOABaseJsonResponse {

	public DoingsInfo doingInfo = new DoingsInfo();
	public JSONArray data;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub

		try {
			doingInfo.message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			doingInfo.replynum = jsonBody.getString("replynum");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			doingInfo.uid = jsonBody.getString("uid");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			doingInfo.username = jsonBody.getString("username");
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			doingInfo.dateline = jsonBody.getString("dateline");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			doingInfo.from = jsonBody.getString("from");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			doingInfo.ip = jsonBody.getString("ip");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}
}
