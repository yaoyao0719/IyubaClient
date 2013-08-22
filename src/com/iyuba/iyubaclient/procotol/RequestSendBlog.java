/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.entity.SendBlog;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 发送日志
 */
public class RequestSendBlog extends VOABaseJsonRequest{
	public static final String protocolCode="30004";
	private String uid;
	private String username;
	private SendBlog sendBlog;
	MD5 m=new MD5();
	public RequestSendBlog(String uid,String username,SendBlog sendblog) {
		super(protocolCode);
		this.uid=uid;
		this.username=username;
		this.sendBlog=sendblog;
		setRequestParameter("uid", uid);
		setRequestParameter("username", URLEncoder.encode(URLEncoder.encode(username)));	
		setRequestParameter("sign",MD5.md5(protocolCode+uid+username+"iyubaV2"));
	}
	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		System.out.println("调用fillBody！！！");
		JSONObject blogdata=new JSONObject();
/*		jsonObject.put("uid", uid);
		jsonObject.put("username", username);
		jsonObject.put("sign",m.getMD5ofStr(protocolCode+uid+username+"iyubaV2"));*/
		if(sendBlog!=null){
			System.out.println("sendblog不为空");		
			blogdata.put("subject", sendBlog.subject);
			System.out.println("sendBlog.subject"+sendBlog.subject);
			blogdata.put("friend", sendBlog.friend);
			blogdata.put("password", sendBlog.password);
			blogdata.put("targetids", sendBlog.targetids);
			blogdata.put("tag", sendBlog.tag);
			blogdata.put("message", sendBlog.message);
		}
		//jsonObject.put("", blogdata);
		//System.out.println("jsonObject"+jsonObject.getString(""));
		return blogdata;
	}
	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSendBlog();
	}

}
