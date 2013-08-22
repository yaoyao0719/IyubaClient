package com.iyuba.iyubaclient;

import java.io.IOException;

import org.ths.frame.THSActivity;
import org.ths.frame.components.ConfigManager;
import org.ths.frame.helper.INormalCallBack;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.setting.SettingConfig;
import com.iyuba.iyubaclient.sqlite.helper.InitWordDB;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.CopyFileToSD;
import com.iyuba.iyubaclient.util.OnFinishedListener;


import android.R.integer;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

/**
 * @author yao 初始化程序 首先拷贝单词数据，拷贝好以后判断时候登陆过，若登陆过，则进入主页，否则进入帮助界面
 */
public class InitActivity extends THSActivity {
	private InitWordDB wordDB;
	private static final String SHORTCUT_INSTALL = "com.android.launcher.action.INSTALL_SHORTCUT";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		handler.sendEmptyMessage(1);// 将表情拷贝到SD卡上		
	}
	/**
	 * 是否已创建快捷方式
	 * @return
	 */
	private boolean hasShortcut()
	{
        boolean isInstallShortcut = false;
        final String AUTHORITY ="com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites?notify=true");
        Cursor c = getContentResolver().query(CONTENT_URI, new String[] {"title", "iconResource" }, "title=?", new String[] {getString(R.string.app_name).trim()}, null);
        if(null != c && c.getCount() > 0)
        {
            isInstallShortcut = true ;
        }
        
        return isInstallShortcut;
	}
	/**
	 * 为程序创建桌面快捷方式
	 */
	private void addShortcut() {
		Intent shortcut = new Intent(SHORTCUT_INSTALL);

		// 显示的名字
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "爱语吧");

		// 显示的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 不允许重复创建
		shortcut.putExtra("duplicate", false);

		// 这个是快捷方式所实现的功能
		Intent intent = new Intent(this, InitActivity.class);
		intent.setAction("com.ldj.shortcuts.self");
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setAction("android.intent.action.MAIN");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		
		
		/*ComponentName comp = new ComponentName(this.getPackageName(), this.getPackageName() + "." +this.getLocalClassName());   
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);*/
		// 发送广播用以创建shortcut
		this.sendBroadcast(shortcut);
		Toast.makeText(this, "快捷方式创建成功", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 删除程序的快捷方式
	 */
	private void delShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = this.getPackageName() + "."
				+ this.getLocalClassName();
		ComponentName comp = new ComponentName(this.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		sendBroadcast(shortcut);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				int goInfo = ConfigManager.Instance().loadInt("isFirstInfo");
				if (goInfo == 0) {
					addShortcut();
					System.out.println("goInfo===" + goInfo);
					Intent intent = new Intent();
					intent.setClass(mContext, HelpActivity.class);
					intent.putExtra("isFirstInfo", 0);// 传递给帮助界面，是在启动界面进入的
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					System.out.println("————————启动LoginActivity-------");
					startActivity(intent);
				}

				InitActivity.this.finish();
				break;
			case 1:
				CopyDB();
				break;
			case 2:
				String[] nameAndPwd = AccountManager.Instace(mContext)
						.getCacheUserNameAndPwd();
				System.out.println("nameAndPwd==" + nameAndPwd.length);
				if (nameAndPwd.length == 2) {
					String userName = nameAndPwd[0];
					String userPwd = nameAndPwd[1];
					System.out.println("nameAndPwd  000==" + nameAndPwd[0]
							+ "  " + nameAndPwd[1]);
					if (ConfigManager.Instance().loadBoolean("isLogin")) {
						Log.e("isLogin", ""+ConfigManager.Instance().loadBoolean("isLogin"));
						AccountManager.Instace(mContext).userId=ConfigManager.Instance().loadString("userId");
						AccountManager.Instace(mContext).userName=ConfigManager.Instance().loadString("userName");
						AccountManager.Instace(mContext).isLoginStatus=true;
						Intent intent = new Intent();
						intent.setClass(mContext, MainActivity.class);
						startActivity(intent);
						InitActivity.this.finish();
					} else {
						if (userName != null && !userName.isEmpty()
								&& !userName.equals("") && userPwd != null
								&& !userPwd.isEmpty() && !userPwd.equals("")) {
							AccountManager.Instace(mContext).login(userName,
									userPwd, new INormalCallBack() {

										@Override
										public void success() {
											// TODO Auto-generated method stub
											// handler.sendEmptyMessage(1);
											Intent intent = new Intent();
											intent.setClass(mContext,
													MainActivity.class);
											startActivity(intent);
											System.out
													.println("handler.sendEmptyMessage(2)  success");
											InitActivity.this.finish();
										}

										@Override
										public void failure() {
											// TODO Auto-generated method stub
											System.out
													.println("handler.sendEmptyMessage(2)  failure");
											handler.sendEmptyMessage(2);
										}
									});
						} else {
							System.out
									.println("handler.sendEmptyMessage(2)  handler.sendEmptyMessageDelayed(0, 3000);");
							handler.sendEmptyMessageDelayed(0, 3000);
						}
					}

				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	 * 功能：拷贝单词数据库到sd卡
	 */
	private void CopyDB() {
		// TODO Auto-generated method stub	
		wordDB=new InitWordDB(mContext);
		wordDB.openDatabase();
		wordDB.closeDatabase();
		handler.sendEmptyMessage(2);// 初始化年份列表
	}

}
