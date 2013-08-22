package com.iyuba.iyubaclient;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.runtime.RuntimeManager;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.AppUpdateCallBack;
import com.iyuba.iyubaclient.manager.VersionManager;
import com.iyuba.iyubaclient.util.DownLoadFailCallBack;
import com.iyuba.iyubaclient.util.FailOpera;
import com.iyuba.iyubaclient.util.FileDownProcessBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SettingActivity extends BaseActivity{

	
	private LinearLayout account,about,update,help,feedback,exit;
	private LinearLayout song,voachangsu,meiyuzenmshuo,cet4,voavideo;
	private String version_code;
	private String appUpdateUrl;
	private ProgressBar progressBar_downLoad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initWidget();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		account=(LinearLayout)findViewById(R.id.setting_account);
		about=(LinearLayout)findViewById(R.id.setting_about);
		update=(LinearLayout)findViewById(R.id.setting_update);
		help=(LinearLayout)findViewById(R.id.setting_help);
		feedback=(LinearLayout)findViewById(R.id.setting_feedback);
		exit=(LinearLayout)findViewById(R.id.setting_exit);
		progressBar_downLoad=(ProgressBar)findViewById(R.id.progressBar_update);
		//应用
		song=(LinearLayout)findViewById(R.id.song);
		voachangsu=(LinearLayout)findViewById(R.id.voachangsu_btn);
		meiyuzenmshuo=(LinearLayout)findViewById(R.id.meiyuzenmshuo);
		cet4=(LinearLayout)findViewById(R.id.cet4);
		voavideo=(LinearLayout)findViewById(R.id.voavideo);
		account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				intent.setClass(mContext, AccountManagerActivity.class);
				startActivity(intent);
			}
		});
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				intent.setClass(mContext, AboutActivity.class);
				startActivity(intent);
			}
		});
		update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VersionManager.Instace(mContext).checkNewVersion(RuntimeManager.getAppInfo().versionCode, 
						new AppUpdateCallBack() {
							
							@Override
							public void appUpdateSave(String version_code, String newAppNetworkUrl) {
								// TODO Auto-generated method stub
								SettingActivity.this.version_code=version_code;
								appUpdateUrl=newAppNetworkUrl;
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
						});
			}
		});
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("isFirstInfo", 1);
				intent.setClass(mContext, HelpActivity.class);
				startActivity(intent);
			}
		});
		feedback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();			
				intent.setClass(mContext, FeedBackActivity.class);
				startActivity(intent);
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//注销用户，跳到登录页面
				AccountManager.Instace(mContext).loginOut();
				Intent intent=new Intent();
				intent.setClass(mContext,LoginActivity.class);
				startActivity(intent);
				SettingActivity.this.finish();
			}
		});
		song.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://iyuba.com/apps.php?mod=app&id=209");
				startActivity(intent);
			}
		});
		voachangsu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://iyuba.com/apps.php?mod=app&id=201");
				startActivity(intent);
			}
		});
		meiyuzenmshuo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://iyuba.com/apps.php?mod=app&id=213");
				startActivity(intent);
			}
		});
		cet4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://iyuba.com/apps.php?mod=app&id=207");
				startActivity(intent);
			}
		});
		voavideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://iyuba.com/apps.php?mod=app&id=217");
				startActivity(intent);
			}
		});
	}
	Handler handler=new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				update.setEnabled(false);
				progressBar_downLoad.setVisibility(View.VISIBLE);
				showAlertAndCancel(getResources().getString(R.string.about_update_alert_1)+
						version_code+getResources().getString(R.string.about_update_alert_2), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								progressBar_downLoad.setMax(100);
								FileDownProcessBar fdpb=new FileDownProcessBar(
										mContext, progressBar_downLoad);
								fdpb.downLoadApkFile(appUpdateUrl, "iYubaClient"+version_code, new DownLoadFailCallBack() {
									
									@Override
									public void downLoadSuccess(String localFilPath) {
										// TODO Auto-generated method stub
										update.setEnabled(true);
										progressBar_downLoad.setVisibility(View.INVISIBLE);
										FailOpera.Instace(mContext).openFile(localFilPath);
									}
									
									@Override
									public void downLoadFaild(String errorInfo) {
										// TODO Auto-generated method stub
										update.setEnabled(true);
										progressBar_downLoad.setVisibility(View.INVISIBLE);
										Toast.makeText(mContext,
												R.string.about_error,
												Toast.LENGTH_SHORT)
												.show();
									}
									
									@Override
									public void downLoadCurrProcess(String Percentage) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void downLoadBegin() {
										// TODO Auto-generated method stub
										
									}
								});
							}					
				});
				break;
			case 1:
				progressBar_downLoad.setVisibility(View.INVISIBLE);
				Toast.makeText(mContext, R.string.about_update_isnew,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

}
