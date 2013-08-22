package com.iyuba.iyubaclient;

import android.view.KeyEvent;

/**
 * 用于解决ActivityGroup中集中解决KeyDown事件的处理
 * @author lijingwei
 *
 */
public interface OnActivityGroupKeyDown {
	public boolean onSubActivityKeyDown(int keyCode, KeyEvent event);
}
