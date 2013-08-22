/**
 * 
 */
package com.iyuba.iyubaclient.word.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequestWord;

/**
 * @author yao
 * 查词
 */
public class RequestTranslate extends VOABaseJsonRequestWord{
	public static final String protocolCode="30001";
	public RequestTranslate(String wordKey) {
		super(protocolCode);
		setRequestParameter("wordkey", wordKey);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseTranslate();
	}

}
