/**
 * 
 */
package com.iyuba.iyubaclient.findfriends.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.feed.procotol.ResponseFeedList;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 *
 */
public class RequestSameAppFriendsList extends VOABaseJsonRequest{
	public static final String protocolCode="90003";
	public RequestSameAppFriendsList(String uid,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("pagesize", "20");
		setRequestParameter("pagenum", pageNumber);
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSameAppFriendsList();
	}

}


