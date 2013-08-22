/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 *
 */
public class RequestMessageLetterContentList  extends VOABaseJsonRequest{
	public static final String protocolCode="60004";
	public RequestMessageLetterContentList(String uid,String friendid) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("friendid", friendid);
		setRequestParameter("sign",MD5.md5(protocolCode+uid+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseMessageLetterContentList();
	}

}

