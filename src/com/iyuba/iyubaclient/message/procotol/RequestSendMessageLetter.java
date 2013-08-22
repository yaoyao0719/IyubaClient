/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 发私信
 */
public class RequestSendMessageLetter  extends VOABaseJsonRequest{
	public static final String protocolCode="60002";
	public RequestSendMessageLetter(String uid,String username,String context) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		MD5 m=new MD5();
		setRequestParameter("uid", uid);
		setRequestParameter("username", URLEncoder.encode(username));
		setRequestParameter("sign",MD5.md5(protocolCode+uid+"iyubaV2") );
		setRequestParameter("context", URLEncoder.encode(context));
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSendMessageLetter();
	}

}

