package com.iyuba.iyubaclient.manager;

/**
 * 
 * @author zqq
 * 
 *         功能：协议结果监�?
 * 
 */
public interface OnResultListener {
	public void OnSuccessListener(String msg);// 操作成功
	public void OnFailureListener(String msg);// 操作失败
}
