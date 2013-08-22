/**
 * 
 */
package com.iyuba.iyubaclient.feed.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 根据日志id找日志
 * 20008 
 */
public class RequestFindBlogById extends VOABaseJsonRequest{
	public static final String protocolCode="20008";
	public RequestFindBlogById(String blogid) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("blogid", blogid);
		setRequestParameter("sign", MD5.md5(protocolCode+blogid+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseFindBlogById();
	}

}



