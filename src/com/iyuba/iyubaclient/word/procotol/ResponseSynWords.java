/**
 * 
 */
package com.iyuba.iyubaclient.word.procotol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import android.util.Log;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequestWord;
import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponseWord;
import com.iyuba.iyubaclient.sqlite.entity.Words;


/**
 * @author yao
 *
 */
public class ResponseSynWords extends VOABaseJsonResponseWord {
	public String resultCode;
	public String message;
	public String userId;
	public ArrayList<Words> words;

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
		if (resultCode.equals("401")) {
			try {
				userId = jsonBody.getString("UserId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JSONArray jsonArrayData = null;
			try {
				jsonArrayData = jsonBody.getJSONArray("Data");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (jsonArrayData != null && jsonArrayData.length() != 0) {
				words = new ArrayList<Words>();
				for (int i = 0; i < jsonArrayData.length(); i++) {
					JSONObject jsonElement = null;
					try {
						jsonElement = jsonArrayData.getJSONObject(i);
						Words wordTemp = new Words();
						wordTemp.key = jsonElement.getString("Key");
						wordTemp.pron = jsonElement.getString("Pron");
						wordTemp.def = jsonElement.getString("Def");
						wordTemp.audio = jsonElement.getString("Audio");
						wordTemp.forUser = userId;
						wordTemp.fromApp = jsonElement.getString("FromApp");
						wordTemp.status = jsonElement.getInt("Status");
						words.add(wordTemp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Log.e("同步回复", words.size() + "");
		}
		return false;
	}

}
