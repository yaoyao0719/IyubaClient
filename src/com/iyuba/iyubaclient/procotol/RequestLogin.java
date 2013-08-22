package com.iyuba.iyubaclient.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import android.util.Log;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;



/**
 * 登录协议
 * @protocolCode 10001
 * @author yaoayo
 *
 */
public class RequestLogin extends VOABaseJsonRequest {
	public static final String protocolCode="10001";
	public String md5Status="1"; // 0=未加密,1=加密
	public String size="middle";
	/**
	 * 
	 * @param wordKey
	 */
	public RequestLogin(String userName,String password){
		super(protocolCode);

		setRequestParameter("username", URLEncoder.encode(userName));
		setRequestParameter("password",MD5.md5(password));
		setRequestParameter("size", size);
		setRequestParameter("sign", MD5.md5(protocolCode+userName+MD5.md5(password)+"iyubaV2"));		
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseLogin();
	}

}
