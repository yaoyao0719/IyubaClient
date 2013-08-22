/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.MD5;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequest;

/**
 * @author yao
 * 请求通知
 *   condition可取的参数
 *   doing 心情评论
 *   blog  日志评论
 *   comment 评论回复
 *   uid    留言板
 *   pic    图片评论
 *   follow 关注通知
 *   system 系统通知
 *   invite 邀请通知
 *   app    应用通知
 */
public class RequestNotificationInfo extends VOABaseJsonRequest{
	public static final String protocolCode="61002";
	public RequestNotificationInfo(String uid,String pageNumber,String condition) {
		super(protocolCode);
		// TODO Auto-generated constructor stub
		MD5 m=new MD5();
		setRequestParameter("uid", uid);
		setRequestParameter("pageNumber", pageNumber);
		setRequestParameter("pageCounts", "20");
		setRequestParameter("asc", "1");
		setRequestParameter("condition", condition);
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
		return new ResponseNotificationInfo();
	}

}


