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
 *编辑用户信息
 */
public class RequestEditUserInfo extends VOABaseJsonRequest{
	public static final String protocolCode="20003";
	public String md5Status="1"; // 0=未加密,1=加密
	
	public RequestEditUserInfo(String userId,String key,String value) {
		super(protocolCode);
		setRequestParameter("id", userId);
		setRequestParameter("sign", MD5.md5(protocolCode+userId+"iyubaV2"));
		setRequestParameter("key", key);
		setRequestParameter("value", value);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseEditUserInfo();
	}

}
