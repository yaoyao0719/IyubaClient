package com.iyuba.iyubaclient.entity;

import android.graphics.Bitmap;

public class Fans {

	public String uid	;//	我关注的uid			
	public String username	;//	我关注的用户名			
	public String bkname	;//	我关注着的备注(当互相关注时显示)			
	public String dateline	;//	添加关注的时间，系统秒数			
	public String mutual	;//	是否互相关注，1为是		
	public Bitmap userBitmap;
    public String doing;
}
