/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 最近来访人
 */
public class RequestRecentVisitsList extends VOABaseJsonRequest{
	public static final String protocolCode="52004";
	public RequestRecentVisitsList(String uid,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("pageNumber", pageNumber);
		setRequestParameter("sign", MD5.md5(protocolCode+uid+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseRecentVisitsList();
	}

}
