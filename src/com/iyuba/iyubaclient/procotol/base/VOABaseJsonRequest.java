package com.iyuba.iyubaclient.procotol.base;

import org.ths.frame.network.protocol.BaseJsonRequest;

import android.util.Log;

public abstract class VOABaseJsonRequest extends BaseJsonRequest {
	protected String platform = "android";
	protected String format = "json";
	protected String protocolCode = "";

	public VOABaseJsonRequest(String protocolCode) {
		this.protocolCode=protocolCode;
		setAbsoluteURI(getCurrAbsoluteURL());
	}

	/**
	 * 获取当前的路径
	 * @return
	 */
	private String getCurrAbsoluteURL() {
		StringBuffer absoluteURL = new StringBuffer(getAbsoluteURI());
		Log.e("protocolCode", "protocolCode");
		
		if(protocolCode.equals("0")){
			absoluteURL.append("http://app.iyuba.com/pay/payVipApi.jsp").append("?")
			.append("platform=").append(platform).append("&")
			.append("format=").append("xml")
			;
		}else{
			absoluteURL.append(ProtocolDef.REQUEST_URL).append("?")
			.append("platform=").append(platform).append("&")
			.append("format=").append(format).append("&")
			.append("protocol=").append(this.protocolCode);
		}
		return absoluteURL.toString();
	}

	/**
	 * 设置参数
	 * @param key
	 * @param value
	 */
	protected void setRequestParameter(String key,String value){
		StringBuffer requestURLTemp=new StringBuffer(getAbsoluteURI());
		requestURLTemp.append("&").append(key).append("=").append(value);
		System.out.println("requestURLTemp.toString()==="+requestURLTemp.toString());
		setAbsoluteURI(requestURLTemp.toString());
	}

}
