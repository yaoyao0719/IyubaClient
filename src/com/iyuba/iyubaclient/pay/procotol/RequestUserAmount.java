/**
 * 
 */
package com.iyuba.iyubaclient.pay.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 请求爱语币值
 */
public class RequestUserAmount extends VOABaseJsonRequest{
	private String appId;// 0
	private String sign;//strAmount+strAppId+strUserId+strProductId+"iyuba"
	private String	productId ;
	private String uid;
	private String amount;
	public RequestUserAmount(String uid,String amount,String productId ){
		super("1");
		setRequestParameter("userId", uid);
		setRequestParameter("appId", "0");
		setRequestParameter("amount", amount);
		setRequestParameter("productId", productId);
		setRequestParameter("sign", MD5.md5(amount+"0"+uid+productId+"iyuba"));
	}
	
	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponsePayVip();
	}
	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}