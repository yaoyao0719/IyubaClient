package com.iyuba.iyubaclient;

import org.ths.frame.runtime.RuntimeManager;

import com.flurry.android.FlurryAgent;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class BaseActivity extends Activity {
	protected Context mContext;
	private OnActivityGroupKeyDown onActivityGroupKeyDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext=this;
		RuntimeManager.setApplication(getApplication());
		RuntimeManager.setContext(this);
		RuntimeManager.setDisplayMetrics(this);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setOnActivityGroupKeyDown(
			OnActivityGroupKeyDown onActivityGroupKeyDown) {
		this.onActivityGroupKeyDown = onActivityGroupKeyDown;
	}

	public void onKeyBackEvent(KeyEvent event){
		if (onActivityGroupKeyDown != null) {
			if (onActivityGroupKeyDown.onSubActivityKeyDown(
					event.getKeyCode(), event)) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 点击返回键
					showAlertAndCancel(getResources().getString(R.string.alert_exit_app),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
									new Thread(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											super.run();
											try {
												Thread.sleep(1000);
												android.os.Process.killProcess(android.os.Process.myPid());
												System.exit(0);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}}.start();
								}
							});
				}
			}
		} else {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 点击返回键
				showAlertAndCancel(getResources().getString(R.string.alert_exit_app),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
								new Thread(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										super.run();
										try {
											Thread.sleep(1000);
											android.os.Process.killProcess(android.os.Process.myPid());
											System.exit(0);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}}.start();
							}
						});
			}
		}
	}
	
	public void onKeyDellEvent(KeyEvent event){
		onActivityGroupKeyDown.onSubActivityKeyDown(
				event.getKeyCode(), event);
	}
	
	
	public void showAlertAndCancel(String msg,
			DialogInterface.OnClickListener ocl) {
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(getResources().getString(R.string.alert_title));
		alert.setMessage(msg);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setButton(getResources().getString(R.string.alert_btn_ok), ocl);
		alert.setButton2(getResources().getString(R.string.alert_btn_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		alert.show();
	}

	private final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "M83WXLTZ1MUBZZUJCIR5");
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
