/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import android.widget.Toast;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

/**
 * @author yao
 *
 */
public class ResponseEditUserInfo extends VOABaseJsonResponse {

	public String message	;//	返回信息		
	public String result	;//	返回代码	

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
		if(result.equals("221")){
		//上传成功
		}
		return false;
	}

}
