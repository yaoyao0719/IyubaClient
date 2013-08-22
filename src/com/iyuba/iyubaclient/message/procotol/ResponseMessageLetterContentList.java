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
public class ResponseMessageLetterContentList extends VOABaseJsonResponse {


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
		try {
			plid = jsonBody.getString("plid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result.equals("631")) {
		
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				for (int i = 0; i < data.length(); i++) {
					try {
						
						
						JSONObject jsonObject = ((JSONObject) data.opt(i));
						MessageLetterContent letter = new MessageLetterContent(jsonObject.getString("message"));
						letter.message = jsonObject.getString("message");
						letter.authorid = jsonObject.getString("authorid");
						letter.dateline = jsonObject.getString("dateline");
						letter.pmid = jsonObject.getString("authorid");
						list.add(letter);
						
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				System.out.println("changdu==="+list.size());
			}

		}
		return false;
	}
}

