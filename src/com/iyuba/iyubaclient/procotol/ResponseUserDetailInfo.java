package com.iyuba.iyubaclient.procotol;

import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonResponse;

public class ResponseUserDetailInfo extends VOABaseJsonResponse {
	public String result	;//	返回代码	
	public String message	;//	返回信息	
	public String realname	;//	真实姓名	
	public String gender	;//	性别	
	public String birthday	;//	生日	
	public String constellation	;//	星座	
	public String zodiac	;//	生肖	
	public String telephone	;//	联系电话	
	public String mobile	;//	手机	
	public String idcardtype;//		证件类型	
	public String idcard	;//	证件号	
	public String address	;//	邮件地址	
	public String zipcode		;//邮编	
	public String nationality	;//	国籍	
	public String birthLocation	;//	出生地	
	public String resideLocation	;//	现住地	
	public String graduateschool	;//	毕业学校	
	public String company		;//公司	
	public String education		;//学历	
	public String occupation	;//	职业	
	public String position		;//职位	
	public String revenue		;//年收入	
	public String affectivestatus	;//	情感状态	
	public String lookingfor		;//交友目的	
	public String bloodtype	;//	血型	
	public String height	;//	身高	
	public String weight	;//	体重	
	public String bio		;//自我介绍	
	public String interest	;//	兴趣爱好	


	@Override
	protected boolean resolveBodyJson(JSONObject jsonBody) {
		// TODO Auto-generated method stub
		try{
			result=jsonBody.getString("result");
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals("211")){
			try{
				realname=jsonBody.getString("realname");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				gender=jsonBody.getString("gender");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				birthday=jsonBody.getString("birthday");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				constellation=jsonBody.getString("constellation");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				zodiac=jsonBody.getString("zodiac");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				telephone=jsonBody.getString("telephone");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				mobile=jsonBody.getString("mobile");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				idcardtype=jsonBody.getString("idcardtype");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				idcard=jsonBody.getString("idcard");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				address=jsonBody.getString("address");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				zipcode=jsonBody.getString("zipcode");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				nationality=jsonBody.getString("nationality");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				birthLocation=jsonBody.getString("birthLocation");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				resideLocation=jsonBody.getString("resideLocation");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				graduateschool=jsonBody.getString("graduateschool");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				company=jsonBody.getString("company");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				education=jsonBody.getString("education");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				occupation=jsonBody.getString("occupation");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				position=jsonBody.getString("position");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				revenue=jsonBody.getString("revenue");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				affectivestatus=jsonBody.getString("affectivestatus");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				lookingfor=jsonBody.getString("lookingfor");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				height=jsonBody.getString("height");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				weight=jsonBody.getString("weight");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				bio=jsonBody.getString("bio");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try{
				interest=jsonBody.getString("interest");
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return false;
	}

}
