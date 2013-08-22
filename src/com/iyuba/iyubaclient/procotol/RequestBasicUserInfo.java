/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 显示用户的基本资料信息
 * protocolCode 20001
 */

public class RequestBasicUserInfo extends VOABaseJsonRequest{
	public static final String protocolCode="20001";
	public String md5Status="1"; // 0=未加密,1=加密
	
	public RequestBasicUserInfo(String userId,String myid) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		MD5 m=new MD5();
		setRequestParameter("id", userId);
		setRequestParameter("myid", myid);
		setRequestParameter("sign",MD5.md5(protocolCode+userId+"iyubaV2"));
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseBasicUserInfo();
	}

}
