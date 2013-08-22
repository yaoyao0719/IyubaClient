package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;


public class ResponseRegister extends VOABaseJsonResponse {
	public String resultCode;
	public String message;
	public boolean isRegSuccess;

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
		if (resultCode.equals("111")) {
			isRegSuccess=true;
		}else{
			isRegSuccess=false;
		}
		return false;
	}

}
