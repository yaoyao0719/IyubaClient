package com.iyuba.iyubaclient.feed.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

public class RequestNewInfo extends VOABaseJsonRequest{
	public static final String protocolCode="62001";
	public RequestNewInfo(String id) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", id);
		setRequestParameter("sign", MD5.md5(protocolCode+id+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseNewInfo();
	}

}

