/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.DoingsCommentInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;

/**
 * @author yao
 *
 */
public class ResponseDoingsCommentInfo extends VOABaseJsonResponse{
	public String result;// 返回代码
	public String message;// 返回信息
	public String doid;// 心情状态id
	public String counts;// 当前页总共的评论数
	public JSONArray data;
	public ArrayList<DoingsCommentInfo> doingsCommentlist;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		doingsCommentlist=new ArrayList<DoingsCommentInfo>();
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
		if(result.equals("311")){
			try{
				counts=jsonBody.getString("counts");				
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				doid=jsonBody.getString("doid");
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
					DoingsCommentInfo doingsCommentInfo=new DoingsCommentInfo();
					JSONObject jsonObject= ((JSONObject)data.opt(i));
					doingsCommentInfo.message=jsonObject.getString("message");
					doingsCommentInfo.dateline=jsonObject.getString("dateline");
					doingsCommentInfo.grade=jsonObject.getString("grade");
					doingsCommentInfo.ip=jsonObject.getString("ip");
					doingsCommentInfo.id=jsonObject.getString("id");
					doingsCommentInfo.uid=jsonObject.getString("uid");
					doingsCommentInfo.upid=jsonObject.getString("upid");
					doingsCommentInfo.username=jsonObject.getString("username");
					doingsCommentlist.add(doingsCommentInfo);
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

