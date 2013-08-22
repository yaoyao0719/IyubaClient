/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import android.R.integer;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 查看日志
 * protocolCode：20006
 */
public class RequestBlogInfo extends VOABaseJsonRequest{
	public static final String protocolCode="20006";
	public String md5Status="1"; // 0=未加密,1=加密
	public static final String pageCounts="20";
	public RequestBlogInfo(String userId,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("id", userId);
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
		return new ResponseBlogInfo();
	}

}
