package com.iyuba.iyubaclient.manager;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.appUpdateRequest;
import com.iyuba.iyubaclient.procotol.appUpdateResponse;

import android.content.Context;
import android.util.Log;

/**
 * 版本管理
 * 
 * @author lijingwei
 * 
 */
public class VersionManager {
	private static VersionManager instance;
	private static Context mContext;

	private VersionManager() {
	};

	public static synchronized VersionManager Instace(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new VersionManager();
		}
		return instance;
	}

	/**
	 * 检查是否有新版本
	 * 
	 * @param version
	 * @param Interface
	 *            AppUpdateCallBack aucb
	 */
	public void checkNewVersion(int version, final AppUpdateCallBack aucb) {

		ClientNetwork.Instace().asynGetResponse(new appUpdateRequest(version),
				new IResponseReceiver() {

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						appUpdateResponse aur = (appUpdateResponse) response;
						if (aur.result.equals("NO")) {
							// 有新版本
							if (aucb != null) {
								// 1.2.3||http:// iyuba.com/soft.apk
								String data = aur.data.replace("||", "★");
								String[] appUpdateInfos = data.split("★");
								if (appUpdateInfos.length == 2) {
//									Log.e("@@@@@@@@@@@@", appUpdateInfos[1]);
									aucb.appUpdateSave(appUpdateInfos[0],
											appUpdateInfos[1]);
								} else {
									aucb.appUpdateFaild();
								}
							}
						} else {
							// 检查失败
							if (aucb != null) {
								aucb.appUpdateFaild();
							}
						}
					}
				}, new DefNetStateReceiverImpl(), null);
	}

}
