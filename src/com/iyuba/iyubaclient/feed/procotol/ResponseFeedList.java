/**
 * 
 */
package com.iyuba.iyubaclient.feed.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.SearchItem;

/**
 * @author yao
 * 新鲜事列表返回值
 */
public class ResponseFeedList extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<FeedInfo> list;
    public Boolean isLastPage=false;
    public int counts;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<FeedInfo>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			counts = jsonBody.getInt("counts");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (result.equals("391")) {
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						FeedInfo item = new FeedInfo();
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						item.id=jsonObject.getString("id");
						item.uid = jsonObject.getString("uid");
						item.body = jsonObject.getString("body");
						item.feedid = jsonObject.getString("feedid");
						item.title = jsonObject.getString("title");									
						item.username = jsonObject.getString("username");
						item.idtype = jsonObject.getString("idtype");
						item.hot = jsonObject.getString("hot");
					
						item.dateline = jsonObject.getString("dateline");
						Log.e("jsonObject.isNull",""+jsonObject.isNull("locationDesc"));
						if(jsonObject.isNull("locationDesc")){
							item.locationDesc="";					
						}else{
							item.locationDesc= jsonObject.getString("locationDesc");
						}
						
					//	item.latitude= jsonObject.getString("latitude");
					//	item.longitude= jsonObject.getString("longitude");
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


