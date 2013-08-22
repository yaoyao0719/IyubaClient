package com.iyuba.iyubaclient.util;

public interface DownLoadFailCallBack {
	public void downLoadBegin();
	public void downLoadSuccess(String localFilPath);
	public void downLoadFaild(String errorInfo);
	public void downLoadCurrProcess(String Percentage);
}
