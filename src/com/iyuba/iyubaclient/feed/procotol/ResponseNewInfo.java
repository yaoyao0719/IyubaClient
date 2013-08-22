package com.iyuba.iyubaclient.feed.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import android.R.integer;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

public class ResponseNewInfo extends VOABaseJsonResponse {
	//{"system":0,"letter":2,"notice":1,"follow":0}
	public int system=0;
	public int letter=0;
	public int notice=0;
	public int follow=0;
	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try {
			system=jsonBody.getInt("system");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			letter=jsonBody.getInt("letter");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			notice=jsonBody.getInt("notice");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			follow=jsonBody.getInt("follow");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


}
