/**
 * 
 */
package com.iyuba.iyubaclient.message.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetterContent;

/**
 * @author yao
 * 私信内容
 */
public class ResponseSendMessageLetter extends VOABaseJsonResponse {


	public String result;// 返回代码
	public String message;// 返回信息
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<MessageLetterContent> list;
	public int num;
	public String plid;


	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		list = new ArrayList<MessageLetterContent>();

		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result.equals("611")) {
			System.out.println("私信发送成功");
		}
		return false;
	}
}

