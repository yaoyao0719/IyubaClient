package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;


/**
 * 用户登录
 * 
 * @author lijingwei
 * 
 */
public class appUpdateRequest extends VOABaseJsonRequest {

	private int version;
	
	public appUpdateRequest(int version) {
		super("");
		this.version=version;
		setAbsoluteURI("http://api.iyuba.com/mobile/android/iyubaclient/islatest.plain?currver="+this.version);
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new appUpdateResponse();
	}

}
