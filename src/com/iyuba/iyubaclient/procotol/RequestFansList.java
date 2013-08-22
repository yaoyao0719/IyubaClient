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
 * 请求粉丝列表
 * 51002
 */
public class RequestFansList extends VOABaseJsonRequest{
	public static final String protocolCode="51002";
	public String md5Status="1"; // 0=未加密,1=加密
	public RequestFansList(String uid,String page) {
		super(protocolCode);
		setRequestParameter("uid", uid);
		setRequestParameter("pageNumber", page);
		//setRequestParameter("pageCounts", "10");
		setRequestParameter("sign", MD5.md5(protocolCode+uid+"iyubaV2"));

	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseFansList();
	}

}
