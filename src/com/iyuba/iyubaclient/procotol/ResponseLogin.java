package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;


public class ResponseLogin extends VOABaseJsonResponse {
	public String resultCode;
	public String message;
	public String userId;
	public String userName;
	public String realName;
	public String email;
	public int Amount;
	public String imgSrc;
	public int vipStatus;
	public String expireTime;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try {
			resultCode = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultCode.equals("101")) {
			try {
				userId = jsonBody.getString("uid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userName = jsonBody.getString("username");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				email = jsonBody.getString("email");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				imgSrc = jsonBody.getString("imgSrc");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			try {
				Amount = jsonBody.getInt("Amount");
				Log.e("Amount", ""+Amount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				vipStatus = jsonBody.getInt("vipStatus");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				expireTime = jsonBody.getString("expireTime");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
