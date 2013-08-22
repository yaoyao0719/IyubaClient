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
 * 向服务器发送当前位置
 */
public class RequestSendLocation extends VOABaseJsonRequest{
	public static final String protocolCode="70001";
	public RequestSendLocation(String uid,String x,String y) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("x", x);//经度
		setRequestParameter("y", y);//纬度
		setRequestParameter("sign", MD5.md5(protocolCode+uid+x+y+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSendLocation();
	}

}
