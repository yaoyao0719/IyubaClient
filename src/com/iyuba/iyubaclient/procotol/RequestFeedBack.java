/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;



/**
 * @author yao
 * 发送意见反馈
 */
public class RequestFeedBack extends VOABaseJsonRequest {
	public static final String protocolCode="91001";
	/**
	 * 
	 * @param wordKey
	 */
	public RequestFeedBack(String userId,String email,String content){
		super(protocolCode);
		setRequestParameter("uid", userId);
		setRequestParameter("email", email);
		setRequestParameter("platform", "android");
		setRequestParameter("app", "iyuba");
		setRequestParameter("content", URLEncoder.encode(content));
		setRequestParameter("sign", MD5.md5(protocolCode+userId+"iyubaV2"));
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseFeedback();
	}

}

