/**
 * 
 */
package com.iyuba.iyubaclient.location.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.feed.procotol.ResponseFeedList;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 请求周边的人
 */
public class RequestNearFriendsList extends VOABaseJsonRequest{
	public static final String protocolCode="70002";
	public RequestNearFriendsList(String uid,String pageNumber,String x,String y) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("pageCounts", "20");
		setRequestParameter("pageNumber", pageNumber);
		setRequestParameter("x", x);
		setRequestParameter("y", y);
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
		return new ResponseNearFriendsList();
	}

}
