/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.entity;

import android.graphics.Bitmap;

/**
 * @author yao
 *
 */
public class NearFriendInfo {

	public String uid;
	public String username;
	public String id;
	public String signTime;//标记时间
	public String description;
	public String longitude;//经度
	public String latitude;//纬度
	public Bitmap userBitmap;
	public double distance;
}
