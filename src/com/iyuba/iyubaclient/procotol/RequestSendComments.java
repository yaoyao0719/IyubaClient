package com.iyuba.iyubaclient.procotol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

import android.util.Log;



/**
 * 发布评论
 * @protocolCode 30005
 * @author yaoyao
 *
 */
public class RequestSendComments extends VOABaseJsonRequest {
	public static final String protocolCode="30005";
	/**
	 * 
	 * @param wordKey
	 */
	public RequestSendComments(String uid,String id,String type,String author,String 
			authorid,String message){
		super(protocolCode);
		MD5 m=new MD5();
		setRequestParameter("uid", uid);//日志作者id
		setRequestParameter("id", id);//日志id
		setRequestParameter("type", type);//评论类别
		setRequestParameter("author", author);//评论者
		setRequestParameter("authorid", authorid);//评论者id
		setRequestParameter("sign", MD5.md5(protocolCode+id+type+uid+author+authorid+"iyubaV2"));
		setRequestParameter("message", URLEncoder.encode(message));
		
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSendComments();
	}

}
