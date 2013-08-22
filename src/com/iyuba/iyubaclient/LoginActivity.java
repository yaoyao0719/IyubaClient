/**
 * 
 */
package com.iyuba.iyubaclient;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ths.frame.THSActivity;
import org.ths.frame.helper.INormalCallBack;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.setting.SettingConfig;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yao
 * 
 */
public class LoginActivity extends THSActivity {

	private String userName, userPwd;
	private EditText loginName, loginPassword;
	private Button loginButton, RegisterButton;
	private TextView forgetPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		loginName = (AutoCompleteTextView) findViewById(R.id.login_username);
		loginPassword = (EditText) findViewById(R.id.login_password);
		loginButton = (Button) findViewById(R.id.bnLogin);
		RegisterButton = (Button) findViewById(R.id.bnRegist);
		forgetPassword = (TextView) findViewById(R.id.forgetpassword);
		forgetPassword
				.setText(Html
						.fromHtml("<a href=\"http://reg.iyuba.com/FindPage\">忘记密码？</a>"));
		forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
		forgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://reg.iyuba.com/FindPage");
				startActivity(intent);
			}
		});
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (verification()) {
					AccountManager.Instace(mContext).login(
							loginName.getText().toString(),
							loginPassword.getText().toString(),
							new INormalCallBack() {

								@Override
								public void success() {
									// TODO Auto-generated method stub
									handler.sendEmptyMessage(0);
									SettingConfig.Instance().setAutoLogin(true);
									Intent intent = new Intent();
									intent.setClass(mContext,
											MainActivity.class);
									startActivity(intent);
									finish();
								}

								@Override
								public void failure() {
									// TODO Auto-generated method stub
									handler.sendEmptyMessage(1);
								}
							});
				}
			}
		});
		RegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, RegisterActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				String userName = data.getStringExtra("userNameString");
				String userPwd = data.getStringExtra("userPwdString");
				Message msg = new Message();
				msg.what = 2;
				msg.obj = userName + "~iyb~" + userPwd;
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						Thread.sleep(1000);
						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			LoginActivity.this.finish();
			return false;
		}
		return false;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String loginSuccessTip = getResources().getString(
						R.string.tip_login_success_text);// 欢迎回来，%1$s！
				if (AccountManager.Instace(mContext).userName != null
						&& !AccountManager.Instace(mContext).userName
								.equals("")) {
					loginSuccessTip = String.format(loginSuccessTip,
							AccountManager.Instace(mContext).userName);
				} else {
					loginSuccessTip = String.format(loginSuccessTip,
							AccountManager.Instace(mContext).userId);
				}
				Toast.makeText(mContext, loginSuccessTip, Toast.LENGTH_SHORT)
						.show();
				if (SettingConfig.Instance().isRememberPwd()) {// 保存账户密码
					AccountManager.Instace(mContext).saveCacheUserNameAndPwd(
							userName, userPwd);
				} else {
					AccountManager.Instace(mContext).saveCacheUserNameAndPwd(
							userName, userPwd);
					System.out.println("保存密码");
				}
				SettingConfig.Instance().setAutoLogin(true);
				break;
			case 1:
				System.out.println("errorInfo"+AccountManager.Instace(mContext).errorInfo);
				Toast.makeText(mContext,
						AccountManager.Instace(mContext).errorInfo,
						Toast.LENGTH_LONG).show();
				break;
			case 2: // 填充账户和密码
				String[] np = String.valueOf(msg.obj).split("~iyb~");
				loginName.setText(np[0]);
				loginPassword.setText(np[1]);
				break;
			}
		}
	};

	/**
	 * 验证
	 */
	public boolean verification() {
		userName = loginName.getText().toString();
		userPwd = loginPassword.getText().toString();
		if (userName.length() == 0) {
			loginName.setError(getResources().getString(
					R.string.login_check_user_id_null));
			return false;
		}
		if (!checkUserId(userName)) {
			loginName.setError(getResources().getString(
					R.string.login_check_effective_user_id));
			return false;
		}

		if (userPwd.length() == 0) {
			loginPassword.setError(getResources().getString(
					R.string.login_check_user_pwd_null));
			return false;
		}
		if (!checkUserPwd(userPwd)) {
			loginPassword.setError(getResources().getString(
					R.string.login_check_user_pwd_constraint));
			return false;
		}
		return true;
	}

	/**
	 * 匹配用户名
	 * 
	 * @param userId 
	 * @return
	 */
	public boolean checkUserId(String userId) {
		if (userId.length() < 2 || userId.length() > 50)
			return false;
	/*	String check = "^[a-zA-Z0-9\u4e00-\u9fa5_]+$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(userId);
		return matcher.matches();*/
		return true;
	}

	/**
	 * 匹配密码
	 * 
	 * @param userPwd
	 * @return
	 */
	public boolean checkUserPwd(String userPwd) {
		if (userPwd.length() < 6 || userPwd.length() > 12)
			return false;
		return true;
	}

}
