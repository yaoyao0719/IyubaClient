/**
 * 
 */
package com.iyuba.iyubaclient.feed.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.search.procotol.ResponseSearchList;

/**
 * @author yao
 *
 */
public class RequestFeedList extends VOABaseJsonRequest{
	public static final String protocolCode="31001";
	public RequestFeedList(String uid,String pageNumber,String find) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		setRequestParameter("uid", uid);
		setRequestParameter("pageCounts", "20");
		setRequestParameter("pageNumber", pageNumber);
		setRequestParameter("find", find);//0 查自己 1 查全站 2 查关注
		if(find.equals("2")){
			setRequestParameter("feeds", "profile,blog,doing");//0 查自己 1 查全站 2 查关注
		}else if(find.equals("1")){
			setRequestParameter("feeds", "friend,blog,doing,album,share");//0 查自己 1 查全站 2 查关注
		}
		
		setRequestParameter("sign", MD5.md5(protocolCode+uid+"iyubaV2") );
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseFeedList();
	}

}


