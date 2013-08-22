package com.iyuba.iyubaclient;

import java.util.Timer;
import java.util.TimerTask;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.runtime.RuntimeManager;

import cn.sharesdk.framework.AbstractWeibo;

import com.iyuba.iyubaclient.feed.procotol.RequestNewInfo;
import com.iyuba.iyubaclient.feed.procotol.ResponseNewInfo;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.AppUpdateCallBack;
import com.iyuba.iyubaclient.manager.VersionManager;
import com.iyuba.iyubaclient.setting.SettingConfig;
import com.iyuba.iyubaclient.util.DownLoadFailCallBack;
import com.iyuba.iyubaclient.util.FailOpera;
import com.iyuba.iyubaclient.util.FileDownProcessBar;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements AppUpdateCallBack {
	private Context mContext;
	private ViewAnimator container = null;
	private String userName, userPwd;
	private ImageView btnHome, btnApplication, btnFriend, btnMessage,
			btnSetting;
	private Button friendNewInfo, messageNewInfo;
	private RelativeLayout homeLayout, applicationLayout, messageLayout,
			friendLayout, settingLayout;

	private int currSelectActivity = 0;

	private String version_code;
	private String appUpdateUrl;
	private ProgressBar progressBar_downLoad; // 下载进度条
	private View appNewImg;
	public int system = 0;
	public int letter = 0;
	public int notice = 0;
	public int follow = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mContext = this;
		init();
		AbstractWeibo.initSDK(this);
		RuntimeManager.setApplication(getApplication());
		RuntimeManager.setDisplayMetrics(this);

		/*
		 * if (!SettingConfig.Instance().isFirstUserApp()) { // 首次使用设置默认界面背景常亮
		 * SettingConfig.Instance().setScreenLit(true);
		 * SettingConfig.Instance().setFirstUserApp(true); }
		 */

		if (SettingConfig.Instance().isAutoLogin()) { // 自动登录
			String[] nameAndPwd = AccountManager.Instace(MainActivity.this)
					.getCacheUserNameAndPwd();
			userName = nameAndPwd[0];
			userPwd = nameAndPwd[1];
			if (ConfigManager.Instance().loadBoolean("isLogin")) {

			} else {
				AccountManager.Instace(MainActivity.this).login(userName,
						userPwd, null);

			}

		}
		try {
			currSelectActivity = savedInstanceState.getInt("message");
		} catch (Exception e) {
			currSelectActivity = 0;
		}

		initWidget();
		mTimer.schedule(mTimerTask, 0, 30000);
	}
	private Timer mTimer = new Timer();
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			handler.sendEmptyMessage(10);
		}
	};
	private void init() {
		Log.e("手机型号", "" + getHandSetInfo());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// checkAppUpdate();
	}

	private String getHandSetInfo() {
		String handSetInfo = "手机型号:" + android.os.Build.MODEL + ",SDK版本:"
				+ android.os.Build.VERSION.SDK + ",系统版本:"
				+ android.os.Build.VERSION.RELEASE + ",软件版本:"
				+ getAppVersionName(MainActivity.this);
		return handSetInfo;
	}

	// 获取当前版本号
	private String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					"com.iyuba.iyubaclient", 0);
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public void initWidget() {

		btnHome = (ImageView) findViewById(R.id.btn_home);
		btnHome.setImageResource(R.drawable.homeicon_press);
		btnApplication = (ImageView) findViewById(R.id.btn_application);
		btnFriend = (ImageView) findViewById(R.id.btn_friend);
		btnMessage = (ImageView) findViewById(R.id.btn_message);
		btnSetting = (ImageView) findViewById(R.id.btn_setting);
		homeLayout = (RelativeLayout) findViewById(R.id.homelayout);
		applicationLayout = (RelativeLayout) findViewById(R.id.applicationlayout);
		messageLayout = (RelativeLayout) findViewById(R.id.messagelayout);
		friendLayout = (RelativeLayout) findViewById(R.id.friendlayout);
		settingLayout = (RelativeLayout) findViewById(R.id.settinglayout);
		friendNewInfo = (Button) findViewById(R.id.friendnewinfo);
		friendNewInfo.setVisibility(View.GONE);
		messageNewInfo = (Button) findViewById(R.id.messagenewinfo);
		messageNewInfo.setVisibility(View.GONE);
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v == btnHome) {
					if (currSelectActivity != 0) {
						setActivity(HomeActivityNew.class);
						// container.setDisplayedChild(0);
						currSelectActivity = 0;
						btnHome.setImageResource(R.drawable.homeicon_press);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						homeLayout.setBackgroundResource(R.drawable.indicator);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == btnApplication) {
					if (currSelectActivity != 1) {
						setActivity(ApplicationActivityTest.class);
						// container.setDisplayedChild(1);
						currSelectActivity = 1;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon_press);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						applicationLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == btnFriend) {
					if (currSelectActivity != 3) {
						setActivity(FriendsActivity.class);
						// container.setDisplayedChild(2);
						currSelectActivity = 3;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon_press);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						friendLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == btnMessage) {
					if (currSelectActivity != 2) {
						setActivity(MessageActivity.class);
						// container.setDisplayedChild(3);
						currSelectActivity = 2;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage
								.setImageResource(R.drawable.messageicon_press);
						btnSetting.setImageResource(R.drawable.settingicon);
						messageLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == btnSetting) {
					if (currSelectActivity != 4) {
						setActivity(SettingActivity.class);
						// container.setDisplayedChild(4);
						currSelectActivity = 4;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting
								.setImageResource(R.drawable.settingicon_press);
						settingLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);

					}
				}
			}
		};

		OnClickListener oclNew = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v == homeLayout) {
					if (currSelectActivity != 0) {
						setActivity(HomeActivityNew.class);
						// container.setDisplayedChild(0);
						currSelectActivity = 0;
						btnHome.setImageResource(R.drawable.homeicon_press);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						homeLayout.setBackgroundResource(R.drawable.indicator);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == applicationLayout) {
					if (currSelectActivity != 1) {
						setActivity(ApplicationActivityTest.class);
						// container.setDisplayedChild(1);
						currSelectActivity = 1;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon_press);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						applicationLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == friendLayout) {
					if (currSelectActivity != 3) {
						setActivity(FriendsActivity.class);
						// container.setDisplayedChild(2);
						currSelectActivity = 3;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon_press);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting.setImageResource(R.drawable.settingicon);
						friendLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == messageLayout) {
					if (currSelectActivity != 2) {
						setActivity(MessageActivity.class);
						// container.setDisplayedChild(3);
						currSelectActivity = 2;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage
								.setImageResource(R.drawable.messageicon_press);
						btnSetting.setImageResource(R.drawable.settingicon);
						messageLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);
						settingLayout.setBackgroundDrawable(null);
					}
				} else if (v == settingLayout) {
					if (currSelectActivity != 4) {
						setActivity(SettingActivity.class);
						// container.setDisplayedChild(4);
						currSelectActivity = 4;
						btnHome.setImageResource(R.drawable.homeicon);
						btnApplication
								.setImageResource(R.drawable.applicationicon);
						btnFriend.setImageResource(R.drawable.friendicon);
						btnMessage.setImageResource(R.drawable.messageicon);
						btnSetting
								.setImageResource(R.drawable.settingicon_press);
						settingLayout
								.setBackgroundResource(R.drawable.indicator);
						homeLayout.setBackgroundDrawable(null);
						applicationLayout.setBackgroundDrawable(null);
						messageLayout.setBackgroundDrawable(null);
						friendLayout.setBackgroundDrawable(null);

					}
				}
			}
		};
		btnHome.setOnClickListener(ocl);
		btnApplication.setOnClickListener(ocl);
		btnFriend.setOnClickListener(ocl);
		btnMessage.setOnClickListener(ocl);
		btnSetting.setOnClickListener(ocl);

		homeLayout.setOnClickListener(oclNew);
		applicationLayout.setOnClickListener(oclNew);
		messageLayout.setOnClickListener(oclNew);
		friendLayout.setOnClickListener(oclNew);
		settingLayout.setOnClickListener(oclNew);
		container = (ViewAnimator) findViewById(R.id.containerBody);
		container.setAnimateFirstView(true);

		setActivity(HomeActivityNew.class);
		homeLayout.setBackgroundResource(R.drawable.indicator);
	}

	/**
	 * 配置要显式的Activity
	 * 
	 * @param cls
	 */
	public void setActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final Window window = getLocalActivityManager().startActivity(
				String.valueOf(0), intent);
		final View view = window != null ? window.getDecorView() : null;
		if (view != null) {
			container.removeAllViews();
			container.setInAnimation(AnimationUtils.loadAnimation(mContext,
					android.R.anim.fade_in));
			container.setOutAnimation(AnimationUtils.loadAnimation(mContext,
					android.R.anim.fade_out));
			container.addView(view);
			view.setFocusable(true);
			container.showNext();
			// container.setDisplayedChild(0);

		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		/*
		 * 注意，此函数不同于onKeyDown方法，onKeyDown函数只监听单一事件，而dispatchKeyEvent会监听所有的按键动作包括（
		 * 按下、抬起等），所以，如果不加以判断，很可能会重复执行
		 */

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			((BaseActivity) getLocalActivityManager().getCurrentActivity())
					.onKeyBackEvent(event);
			return true;
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_DEL
				&& event.getAction() == KeyEvent.ACTION_UP) {
			// // Log.e("$$$$$$$$$$$$", String.valueOf(event.getKeyCode()));
			((BaseActivity) getLocalActivityManager().getCurrentActivity())
					.onKeyDellEvent(event);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 检查新版本
	 */
	public void checkAppUpdate() {
		VersionManager.Instace(this).checkNewVersion(
				RuntimeManager.getAppInfo().versionCode, this);
	}

	@Override
	public void appUpdateSave(String version_code, String newAppNetworkUrl) {
		// TODO Auto-generated method stub
		this.version_code = version_code;
		this.appUpdateUrl = newAppNetworkUrl;
		handler.sendEmptyMessage(0);
	}

	@Override
	public void appUpdateFaild() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);
	}

	@Override
	public void appUpdateBegin(String newAppNetworkUrl) {
		// TODO Auto-generated method stub

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (appNewImg != null) {
					appNewImg.setVisibility(View.VISIBLE);
					progressBar_downLoad.setVisibility(View.VISIBLE);
				}

				showAlertAndCancel(
						getResources().getString(R.string.about_update_alert_1)
								+ version_code
								+ getResources().getString(
										R.string.about_update_alert_2),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 下载更新
								progressBar_downLoad.setMax(100); // 设置progressBar最大值
								FileDownProcessBar fdpb = new FileDownProcessBar(
										mContext, progressBar_downLoad);
								fdpb.downLoadApkFile(appUpdateUrl,
										"VoaEnglish_" + version_code,
										new DownLoadFailCallBack() {

											@Override
											public void downLoadSuccess(
													String localFilPath) {
												// TODO Auto-generated method
												progressBar_downLoad
														.setVisibility(View.INVISIBLE);
												FailOpera.Instace(mContext)
														.openFile(localFilPath);
											}

											@Override
											public void downLoadFaild(
													String errorInfo) {
												// TODO Auto-generated method
												// stub
												progressBar_downLoad
														.setVisibility(View.INVISIBLE);
												Toast.makeText(mContext,
														R.string.about_error,
														Toast.LENGTH_SHORT)
														.show();
											}

											@Override
											public void downLoadCurrProcess(
													String Percentage) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void downLoadBegin() {
												// TODO Auto-generated method
												// stub

											}
										});
							}
						});
				break;
			case 1:
				if (appNewImg != null) {
					appNewImg.setVisibility(View.INVISIBLE);
					progressBar_downLoad.setVisibility(View.INVISIBLE);
				}
				Toast.makeText(mContext, R.string.about_update_isnew,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				dialog.dismiss(); // 关闭progressdialog
				break;
			case 10:
				ClientNetwork.Instace().asynGetResponse(
						new RequestNewInfo(
								AccountManager.Instace(mContext).userId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseNewInfo res = (ResponseNewInfo) response;
								system = res.system;
								notice = res.notice;
								letter = res.letter;
								follow = res.follow;
								handler.sendEmptyMessage(11);
							}
						});
				break;
			case 11:
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (follow != 0) {
							friendNewInfo
									.setVisibility(View.VISIBLE);
							friendNewInfo.setText(follow + "");
						} else if (follow == 0) {
							friendNewInfo
									.setVisibility(View.GONE);
						}
						if ((letter + notice) != 0) {
							messageNewInfo
									.setVisibility(View.VISIBLE);
							messageNewInfo.setText(letter
									+ notice + "");
						} else if (follow == 0) {
							messageNewInfo
									.setVisibility(View.GONE);
						}
					}
				});
				break;
			}
		}
	};

	private void showAlertAndCancel(String msg,
			DialogInterface.OnClickListener ocl) {
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(R.string.alert_title);
		alert.setMessage(msg);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setButton(getResources().getString(R.string.alert_btn_ok), ocl);
		alert.setButton2(getResources().getString(R.string.alert_btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// appUpdateBtn.setEnabled(true);
						// progressBar_downLoad.setVisibility(View.INVISIBLE);
					}
				});
		alert.show();
	}

	ProgressDialog dialog;

	@SuppressWarnings("static-access")
	private void showAlert(String msg) {
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为圆形进度
		dialog.show(this,
				getResources().getString(R.string.about_update_running_title),
				msg, true);
		dialog.dismiss();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.e("onSaveInstanceState", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		outState.putInt("message", currSelectActivity);
	}

}
