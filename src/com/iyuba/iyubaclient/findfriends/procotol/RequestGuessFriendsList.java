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
 * 猜你认识好友列表
 */
public class RequestGuessFriendsList  extends VOABaseJsonRequest{
	public static final String protocolCode="52003";
	public RequestGuessFriendsList(String uid) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
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
		return new ResponseGuessFriendsList();
	}

}



