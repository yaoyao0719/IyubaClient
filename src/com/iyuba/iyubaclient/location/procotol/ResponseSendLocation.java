/**
 * 
 */
package com.iyuba.iyubaclient.location.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 *
 */
public class ResponseSendLocation extends VOABaseJsonResponse  {
	public String result;// 返回代码
	public String message;// 返回信息
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(result.equals("701")){
			System.out.println("位置发送成功");
		}
		return false;
	}



}
