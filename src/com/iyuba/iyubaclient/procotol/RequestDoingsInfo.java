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
 * 查看用户状态-doings
 */
public class RequestDoingsInfo extends VOABaseJsonRequest{
	public static final String protocolCode="30001";
	public String md5Status="1"; // 0=未加密,1=加密
	public static final String pageCounts="10";
	public RequestDoingsInfo(String userId,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("userId", userId);
		setRequestParameter("sign",MD5.md5(protocolCode+userId+"iyubaV2"));
		setRequestParameter("pageNumber", pageNumber);
		setRequestParameter("pageCounts", pageCounts);
	}
	

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseDoingsInfo();
	}

}
