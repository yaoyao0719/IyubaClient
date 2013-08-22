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
public class RequestSetMessageLetterRead extends VOABaseJsonRequest{
	public static final String protocolCode="60003";
	public RequestSetMessageLetterRead(String uid,String plid) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		MD5 m=new MD5();
		setRequestParameter("uid", uid);
		setRequestParameter("plid", plid);
		setRequestParameter("sign",MD5.md5(protocolCode+uid+plid+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSetMessageLetterRead();
	}

}


