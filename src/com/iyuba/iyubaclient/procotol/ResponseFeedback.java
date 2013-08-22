/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 *
 */
public class ResponseFeedback extends VOABaseJsonResponse {
	public String resultCode;
	public String message;
	public String userId;
	public String userName;
	public String realName;
	public String email;
	public String balance;

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
		
		return false;
	}

}
