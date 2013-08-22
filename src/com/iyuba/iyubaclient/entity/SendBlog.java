/**
 * 
 */
package com.iyuba.iyubaclient.entity;

import org.ths.frame.database.EntityBean;

/**
 * @author yao
 *写日志要提交的类
 */
public class SendBlog extends EntityBean{

	public String subject	;//日志的title
	public String friend	;//日志权限 0全站可见 1好友可见 2指定好友 3密码
	public String password	;//当权限值为3时，此处生效
	public String targetids;//	当权限值为2时，此处生效
	public String tag;//	标签
	public String message	;//日志具体内容
	/**
	 * @param subject
	 * @param message
	 */
	public SendBlog(String subject, String message) {
		super();
		this.subject = subject;
		this.message = message;
		this.friend="0";
		this.password="";
		this.targetids="";
		this.tag="";
	}
	

}
