package com.iyuba.iyubaclient.pay.procotol;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ths.frame.network.protocol.BaseHttpResponse;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



import android.util.Log;

public class ResponsePayVip extends BaseHttpResponse {
	public String result;
	public String msg;
	public String amount;


	@Override
	public void receiveResult(String result) {
		// TODO Auto-generated method stub
		Log.e("receiveResult", "receiveResult"+result);
		toXml(result);
	}

	public  void toXml(String resultStr) {
		// 此处是将&进行转码成&amp;
		resultStr = resultStr.replaceAll("[&]", "&amp;");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// NetMeetingResult nmr = new NetMeetingResult();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(resultStr
					.getBytes("utf-8")));
			NodeList r1 = doc.getElementsByTagName("result");
			NodeList r2 = doc.getElementsByTagName("msg");
			NodeList r3 = doc.getElementsByTagName("amount");			
			result= r1.item(0).getFirstChild().getNodeValue();
			msg=r2.item(0).getFirstChild().getNodeValue();
			amount=r3.item(0).getFirstChild().getNodeValue();
			Log.e("", "result="+result+"msg==="+msg+"amount=="+amount);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
