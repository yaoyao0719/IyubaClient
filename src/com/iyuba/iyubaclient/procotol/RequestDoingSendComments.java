/**
 * 
 */
package com.iyuba.iyubaclient.procotol;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;
import com.iyuba.iyubaclient.sqlite.entity.DoingsCommentInfo;

/**
 * @author yao
 * 回复心情、评论心情
 */
public class RequestDoingSendComments extends VOABaseJsonRequest{

	public static final String protocolCode="30007";
	
	public RequestDoingSendComments(DoingsCommentInfo doingsCommentInfo,String doid) {
		super(protocolCode);
		setRequestParameter("uid", doingsCommentInfo.uid);
		setRequestParameter("username", doingsCommentInfo.username);
		setRequestParameter("doid",doid);
		setRequestParameter("upid", doingsCommentInfo.upid);
		setRequestParameter("message", URLEncoder.encode(doingsCommentInfo.message));
		if(doingsCommentInfo.grade==null||doingsCommentInfo.grade.equals("")){
			
		}else{
			setRequestParameter("grade", doingsCommentInfo.grade);
		}	
		setRequestParameter("sign",MD5.md5(protocolCode+doingsCommentInfo.uid+doingsCommentInfo.username+doingsCommentInfo.message+"iyubaV2"));	
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseDoingSendComments();
	}

}
