/**
 * 
 */
package com.iyuba.iyubaclient.search.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.message.procotol.ResponseMessageLetterList;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 搜索好友
 */
public class RequestSearchList extends VOABaseJsonRequest{
	public static final String protocolCode="52001";
	public RequestSearchList(String uid,String search,String type,String pageNumber) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("search", URLEncoder.encode(search));
		setRequestParameter("type", type);
		setRequestParameter("pageNumber", pageNumber);
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
		return new ResponseSearchList();
	}

}

