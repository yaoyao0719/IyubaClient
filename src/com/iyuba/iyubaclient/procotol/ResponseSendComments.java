package com.iyuba.iyubaclient.procotol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;


public class ResponseSendComments extends VOABaseJsonResponse {
	public String result;
	public String message;
	
	public boolean isSendSuccess=false;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try {
			result = jsonBody.getString("result");
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
		if (result.equals("341")) {
			isSendSuccess=true;
		}else{
			isSendSuccess=false;
		}
		return false;
	}

}
