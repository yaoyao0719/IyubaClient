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
 * 发心情
 */
public class RequestPublishMood extends VOABaseJsonRequest{
	public static final String protocolCode="30006";
	public RequestPublishMood(String uid,String username,String message) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("username", URLEncoder.encode(username));
		setRequestParameter("message",  URLEncoder.encode(message));
		setRequestParameter("sign", MD5.md5(protocolCode+uid+username+message+"iyubaV2"));
	}
	public RequestPublishMood(String uid,String username,String message,String x,String y) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("x", x);
		setRequestParameter("y", y);
		setRequestParameter("username", URLEncoder.encode(username));
		setRequestParameter("message",  URLEncoder.encode(message));
		setRequestParameter("sign", MD5.md5(protocolCode+uid+username+message+"iyubaV2"));
	}
	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponsePublishMood();
	}

}