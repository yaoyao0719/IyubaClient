/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 * 评论心情
 */
public class ResponseDoingSendComments extends VOABaseJsonResponse{

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
		if (result.equals("361")) {
			isSendSuccess=true;
		}else{
			isSendSuccess=false;
		}
		return false;
	}

}
