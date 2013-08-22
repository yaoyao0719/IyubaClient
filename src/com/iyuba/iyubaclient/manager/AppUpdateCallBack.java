package com.iyuba.iyubaclient.manager;

public interface AppUpdateCallBack {
	/**
	 * 提示有新版本更新
	 */
	public void appUpdateSave(String version_code,String newAppNetworkUrl);
	/**
	 * 检查新版本失败或没有新版本更新
	 */
	public void appUpdateFaild();
	/**
	 * 执行新版本更新动作
	 * @param newAppNetworkUrl 更新路径
	 */
	public void appUpdateBegin(String newAppNetworkUrl);
}
