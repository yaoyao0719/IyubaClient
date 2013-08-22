/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.entity;

import android.graphics.Bitmap;

/**
 * @author yao
 * 状态回复
 */
public class DoingsCommentInfo {

	public String message;
	public String uid;//回复人id
	public String id;//回复内容id标识
	public String username;//回复人
	public String upid;//上一层回复标识(回复的id)
	public String grade;//楼层
	public String dateline;//回复发布时间，系统秒数
	public String ip;
	public Bitmap userBitmap;
	
}
