package com.iyuba.iyubaclient.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

import android.util.Log;


/**
 * 注册协议
 * @protocolCode 10002
 * @author yaoyao
 *
 */
public class RequestRegister extends VOABaseJsonRequest {
	public static final String protocolCode="10002";
	public String md5Status="1"; // 0=未加密,1=加密
	public String emailStatus="0";

	/**
	 * 
	 * @param wordKey
	 */
	public RequestRegister(String userName,String email,String password){
		super(protocolCode);
		MD5 m=new MD5();
		setRequestParameter("username", URLEncoder.encode(userName));
		setRequestParameter("password",MD5.md5(password));
		setRequestParameter("email", email);
		Log.e("RegisterRequest", userName+"  "+email+"  "+password);
		setRequestParameter("sign", MD5.md5(protocolCode+userName+MD5.md5(password)+email+"iyubaV2"));
		setRequestParameter("md5status", md5Status);
		setRequestParameter("emailstatus", emailStatus);
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseRegister();
	}

}
