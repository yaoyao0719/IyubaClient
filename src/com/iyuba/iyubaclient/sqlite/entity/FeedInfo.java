/**
 * 
 */
package com.iyuba.iyubaclient.sqlite.entity;

import org.ths.frame.database.EntityBean;

import android.graphics.Bitmap;
import android.text.LoginFilter.UsernameFilterGeneric;

/**
 * @author yao
 *
 */
public class FeedInfo extends EntityBean{

	public String id;
	public String uid;
	public String body;
	public String feedid;
	public String title;
	public String username;
	public String idtype;
	public String hot;
	public String dateline;
	public Bitmap userBitmap;
	public String locationDesc;
	public String longitude;
	public String latitude;
}
