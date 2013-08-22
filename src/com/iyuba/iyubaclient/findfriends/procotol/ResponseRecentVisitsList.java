/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.GuessFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.RecentVisitInfo;

/**
 * @author yao
 * 最近来访
 */
public class ResponseRecentVisitsList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<RecentVisitInfo> list;
    public String lastPage;
    public String nextPage;
    public String prevPage;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<RecentVisitInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			lastPage = jsonBody.getString("lastPage");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			nextPage = jsonBody.getString("nextPage");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			prevPage = jsonBody.getString("prevPage");
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
						RecentVisitInfo item = new RecentVisitInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						
						item.vuid = jsonObject.getString("vuid");
						item.vusername = jsonObject.getString("vusername");
						item.dateline = jsonObject.getString("dateline");
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




