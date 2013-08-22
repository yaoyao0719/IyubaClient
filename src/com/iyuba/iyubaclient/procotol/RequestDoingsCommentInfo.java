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
 *
 */
public class RequestDoingsCommentInfo extends VOABaseJsonRequest{
	public static final String protocolCode="30002";
	public String md5Status="1"; // 0=未加密,1=加密
	public static final String pageCounts="100";
	public RequestDoingsCommentInfo(String id,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("doing", id);
		setRequestParameter("sign", MD5.md5(protocolCode+id+"iyubaV2"));
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
		return new ResponseDoingsCommentInfo();
	}

}
