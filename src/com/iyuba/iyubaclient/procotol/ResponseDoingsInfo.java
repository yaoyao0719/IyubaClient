/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

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
public class ResponseDoingsInfo extends VOABaseJsonResponse{
	public String result;// 返回代码
	public String message;// 返回信息
	public String uid;// 用户id
	public String username;// 用户名
	public String counts;// 当前页总共的评论数
	public JSONArray data;
	public ArrayList<DoingsInfo> doingslist;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		doingslist=new ArrayList<DoingsInfo>();
		//blogContent=new BlogContent();
		try{
			result=jsonBody.getString("result");
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("301")){
			try{
				System.out.println("blogInfo.blogCounts"+jsonBody.getString("counts"));
				counts=jsonBody.getString("counts");
				
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				uid=jsonBody.getString("uid");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				username=jsonBody.getString("username");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{				
				data=jsonBody.getJSONArray("data");				
				
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(data!=null&&data.length()!=0){
				for(int i=0;i<data.length();i++){
					try{
					DoingsInfo doingsInfo=new DoingsInfo();
					JSONObject jsonObject= ((JSONObject)data.opt(i));
					doingsInfo.message=jsonObject.getString("message");
					doingsInfo.doid=jsonObject.getString("doid");
					doingsInfo.dateline=jsonObject.getString("dateline");
					doingsInfo.from=jsonObject.getString("from");
					doingsInfo.ip=jsonObject.getString("ip");
					doingsInfo.replynum=jsonObject.getString("replynum");
					doingsInfo.username=username;
					doingsInfo.uid=uid;
					doingslist.add(doingsInfo);
					}catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
					
			}

			
		}
		return false;
	}
}

