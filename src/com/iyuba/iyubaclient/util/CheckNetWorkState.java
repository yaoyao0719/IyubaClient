/**
 * 
 */
package com.iyuba.iyubaclient.util;

import android.R.integer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * @author yao
 *
 */
public class CheckNetWorkState {

	public int netWorkConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		State WIFIState = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		State mobileState = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();

		if (networkInfo != null) {
			boolean netWorkAvailable = networkInfo.isAvailable();
			if (WIFIState == State.CONNECTED && netWorkAvailable) {
				return 1;// wifi
			} else if (mobileState == State.CONNECTED && netWorkAvailable) {
				return 2;// mobile
			} else {
				return 3;// error;
			}
		} else {
			return 3;
		}
	}
}
