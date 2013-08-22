/**
 * 
 */
package com.iyuba.iyubaclient.procotol.base;

import org.ths.frame.network.protocol.BaseJsonRequest;


/**
 * @author yao
 * 加单词
 */
public abstract class VOABaseJsonRequestWord extends BaseJsonRequest {
	protected String platform = "android";
	protected String format = "json";
	protected String protocolCode = "";

	public VOABaseJsonRequestWord(String protocolCode) {
		this.protocolCode=protocolCode;
		setAbsoluteURI(getCurrAbsoluteURL());
	}

	/**
	 * 获取当前的路径
	 * @return
	 */
	private String getCurrAbsoluteURL() {
		StringBuffer absoluteURL = new StringBuffer(getAbsoluteURI());
		absoluteURL.append(ProtocolDef.REQUEST_URL_WORD).append("?")
				.append("platform=").append(platform).append("&")
				.append("format=").append(format).append("&")
				.append("protocol=").append(this.protocolCode);
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
