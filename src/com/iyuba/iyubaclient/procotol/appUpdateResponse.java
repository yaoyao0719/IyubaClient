package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import org.ths.frame.network.protocol.BasePlainResponse;

public class appUpdateResponse extends BasePlainResponse {
	public String result="";
	public String msg="";
	public String data="";
//	@Override
//	protected boolean resolveBodyJson(JSONObject jsonBody) {
//		// TODO Auto-generated method stub
//		
//		try {
//			status = jsonBody.getString("status");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			msg = jsonBody.getString("msg");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JSONObject jsonData = null;
//		try {
//			jsonData=jsonBody.getJSONObject("data");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(jsonData!=null){
//			try {
//				version=jsonData.getString("versiuon");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				url=jsonData.getString("url");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return true;
//	}
	@Override
	protected boolean resolveBodyJson(String body) {
		// TODO Auto-generated method stub
		String[] rs=body.split(",");
		if(rs.length==3){
			result=rs[0];
			msg=rs[1];
			data=rs[2];
		}else if(rs.length==2){
			result=rs[0];
			msg=rs[1];
		}
		Log.e("", body);
		return false;
	}

}
