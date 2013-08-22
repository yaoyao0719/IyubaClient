/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.entity;

import android.graphics.Bitmap;


/**
 * @author yao
 * 私信内容
 */
public class MessageLetterContent {

	public  String message;// 私信id，设置未读私信为已读需要的参数
	public  String pmid;// 私信内容id，当删除私信的时候需要的参数
	public  String authorid;// 若与url的id相同则为发送，否则为接收
	public  String dateline;//1362915420
	public  Bitmap userBitmap;
	public int direction=0;
	public static final int MESSAGE_FROM = 0;
	public static final int MESSAGE_TO = 1;
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}

	
	public MessageLetterContent(String message){
		setMessage(message);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public Bitmap getUserBitmap() {
		return userBitmap;
	}
	public void setUserBitmap(Bitmap userBitmap) {
		this.userBitmap = userBitmap;
	}

}
