package com.iyuba.iyubaclient.location.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.NearFriendInfo;
/**
 * @author yao
 * 附近的人
 */
public class ResponseNearFriendsList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<NearFriendInfo> list;
    public Boolean isLastPage=false;
    public int total;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<NearFriendInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			total = jsonBody.getInt("total");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (result.equals("711")) {
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						NearFriendInfo item = new NearFriendInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						item.username=jsonObject.getString("username");
						item.id=jsonObject.getString("id");
						item.uid = jsonObject.getString("uid");
						item.description = jsonObject.getString("description");
						item.latitude = jsonObject.getString("latitude");
						item.longitude = jsonObject.getString("longitude");									
						item.signTime = jsonObject.getString("signTime");
						item.distance = jsonObject.getDouble("distance");
						list.add(item);
						
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			}

		}else if(result.equals("392")){
			isLastPage=true;
		}
		return false;
	}
}



