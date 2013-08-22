package com.iyuba.iyubaclient.word.procotol;

import org.json.JSONException;
import org.json.JSONObject;


import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponseWord;
import com.iyuba.iyubaclient.sqlite.entity.Words;


public class ResponseTranslate extends VOABaseJsonResponseWord {
	public String resultCode;
	public String message;
	public Words word;

	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try {
			resultCode = jsonBody.getString("ResultCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("Message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultCode.equals("201")) {
			word = new Words();
			try {
				word.key = jsonBody.getString("Key");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				word.pron = jsonBody.getString("Pron");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				word.audio = jsonBody.getString("Audio");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				word.def = jsonBody.getString("Def");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}

