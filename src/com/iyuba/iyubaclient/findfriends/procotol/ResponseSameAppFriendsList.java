/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.SameAppFriendInfo;

/**
 * @author yao
 *
 */
public class ResponseSameAppFriendsList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<SameAppFriendInfo> list;
    public int friendCounts;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<SameAppFriendInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			friendCounts = jsonBody.getInt("friendCounts");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (result.equals("261")) {
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						SameAppFriendInfo item = new SameAppFriendInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						
						item.uid = jsonObject.getString("uid");												
						item.username = jsonObject.getString("username");
						item.appid = jsonObject.getString("appid");
						item.appname = jsonObject.getString("appname");
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




