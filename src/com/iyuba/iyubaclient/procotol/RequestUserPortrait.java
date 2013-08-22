package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.network.protocol.BaseJsonRequest;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

public class RequestUserPortrait extends BaseJsonRequest{

	public RequestUserPortrait(String userId){
		setAbsoluteURI("www.iyuba.com/uc_server/avatar.php?size=small&uid="+userId);
	}
	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseUserPortrait();
	}

}
