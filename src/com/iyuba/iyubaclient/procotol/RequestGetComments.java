package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;



/**
 * 获取评论
 * @protocolCode 20007
 * @author yao
 *
 */
public class RequestGetComments extends VOABaseJsonRequest {
	public static final String protocolCode="20007";
	public static final String pageCounts="10";
	/**
	 * 
	 * @param wordKey
	 */
	public RequestGetComments(String id,String type,int pageNum){
		super(protocolCode);
		setRequestParameter("sign", MD5.md5(protocolCode+id+type+"iyubaV2"));
		setRequestParameter("type", type);//评论类型	blogid	日志评论 picid	图片评论 uid	用户评论sid	分享评论
		setRequestParameter("id", id);
		setRequestParameter("pageNum", String.valueOf(pageNum));
		setRequestParameter("pageCounts", String.valueOf(pageCounts));
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseGetComments();
	}

}
