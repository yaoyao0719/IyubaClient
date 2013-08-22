/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import android.R.integer;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.BlogInfo;

/**
 * @author yao
 * 
 */
public class ResponseBlogInfo extends VOABaseJsonResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public BlogInfo blogInfo = new BlogInfo();
	public JSONArray data;
	public ArrayList<BlogContent> blogList;
	public String firstPage;
	public String prevPage;
	public String nextPage;
	public String lastPage;
	public Boolean isLastPage=false;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		blogList=new ArrayList<BlogContent>();
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
		try {
			firstPage = jsonBody.getString("firstPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			prevPage = jsonBody.getString("prevPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			nextPage = jsonBody.getString("nextPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lastPage = jsonBody.getString("lastPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result.equals("251")){
			try{
				System.out.println("blogInfo.blogCounts"+jsonBody.getString("blogCounts"));
				blogInfo.blogCounts=jsonBody.getString("blogCounts");
				
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogInfo.pageNumber=jsonBody.getString("pageNumber");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogInfo.firstPage=jsonBody.getString("firstPage");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogInfo.prevPage=jsonBody.getString("prevPage");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogInfo.nextPage=jsonBody.getString("nextPage");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogInfo.lastPage=jsonBody.getString("lastPage");
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
					BlogContent blogContent=new BlogContent();
					JSONObject jsonObject= ((JSONObject)data.opt(i));
					blogContent.replynum=jsonObject.getString("replynum");
					blogContent.viewnum=jsonObject.getString("viewnum");
					blogContent.blogid=jsonObject.getString("blogid");
					blogContent.dateline=jsonObject.getString("dateline");
					blogContent.favtimes=jsonObject.getString("favtimes");
					blogContent.friend=jsonObject.getString("friend");
					blogContent.ids=jsonObject.getString("ids");
					blogContent.message=jsonObject.getString("message");
					blogContent.noreply=jsonObject.getString("noreply");
					blogContent.password=jsonObject.getString("password");
					blogContent.sharetimes=jsonObject.getString("sharetimes");
					blogContent.subject=jsonObject.getString("subject");
					
					blogList.add(blogContent);
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
