/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;

/**
 * @author yao
 *
 */
public class ResponseBasicUserInfo extends VOABaseJsonResponse{
	public String message	;//	返回信息		
	public String result	;//	返回代码		
	public String username	;//	用户名		
	public String credits	;//	用户邮箱		
	public String shengwang;//		声望		
	public String icoins	;//	金币		
	public String contribute;//		贡献		
	public String friends	;//	好友数		
	public String posts	;//	帖子数		
	public String doings	;//	发布的心情数		
	public String blogs	;//	日志数		
	public String albums	;//	相册数		
	public String sharings	;//	分享数		
	public String views	;//	访客数		人气
	public String gender	;//	性别		
	public String text		;//最近的心情签名	
	public int amount;
	public UserInfo userInfo=new UserInfo();
	public String relation;
	public String distance;
	public String expireTime;
	public int vipStatus;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
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
		try{
			relation=jsonBody.getString("relation");
			
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(result.equals("201")){
			try{
				username=jsonBody.getString("username");
				userInfo.username=jsonBody.getString("username");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			try{
				credits=jsonBody.getString("credits");
				userInfo.credit=jsonBody.getString("credits");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				shengwang=jsonBody.getString("shengwang");
				userInfo.shengwang=jsonBody.getString("shengwang");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				icoins=jsonBody.getString("icoins");
				userInfo.icoins=jsonBody.getString("icoins");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				contribute=jsonBody.getString("contribute");
				userInfo.contribute=jsonBody.getString("contribute");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				friends=jsonBody.getString("friends");
				userInfo.friends=jsonBody.getString("friends");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				posts=jsonBody.getString("posts");
				userInfo.posts=jsonBody.getString("posts");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				doings=jsonBody.getString("doings");
				userInfo.doings=jsonBody.getString("doings");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				albums=jsonBody.getString("albums");
				userInfo.albums=jsonBody.getString("albums");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				sharings=jsonBody.getString("sharings");
				userInfo.sharings=jsonBody.getString("sharings");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				views=jsonBody.getString("views");
				userInfo.views=jsonBody.getString("views");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				gender=jsonBody.getString("gender");
				userInfo.gender=jsonBody.getString("gender");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				text=jsonBody.getString("text");
				userInfo.text=jsonBody.getString("text");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				blogs=jsonBody.getString("blogs");
				userInfo.blogs=jsonBody.getString("blogs");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				amount=jsonBody.getInt("amount");
				userInfo.Amount=jsonBody.getInt("amount");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
			try{
				expireTime=jsonBody.getString("expireTime");
				userInfo.expireTime=jsonBody.getString("expireTime");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
			try{
				vipStatus=jsonBody.getInt("vipStatus");
				userInfo.vipStatus=jsonBody.getInt("vipStatus");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
			try{
				distance=jsonBody.getString("distance");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
