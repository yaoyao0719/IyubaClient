package com.iyuba.iyubaclient;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ths.frame.THSActivity;
import org.ths.frame.helper.INormalCallBack;

import com.flurry.android.FlurryAgent;
import com.iyuba.iyubaclient.manager.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class RegisterActivity extends THSActivity{
	private String userNameString, userPwdString,
			reUserPwdString, emailString;
	private EditText regLoginName, regEmail, regPwd, regRepwd;
	private Button regBtn, cancelBtn,regBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initWidget();
	}


	public void initWidget() {
		regBackBtn=(Button)findViewById(R.id.register_back_btn);
		regLoginName = (EditText) findViewById(R.id.register_loginName);
		regEmail = (EditText) findViewById(R.id.register_email);
		regPwd = (EditText) findViewById(R.id.register_loginPwd);
		regRepwd = (EditText) findViewById(R.id.register_reLoginPwd);
		regBtn = (Button) findViewById(R.id.register_registerBtn);
		cancelBtn = (Button) findViewById(R.id.register_cancelBtn);
		regBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (verification()) {// 注册
					AccountManager.Instace(mContext).register(userNameString,
							userPwdString, emailString,
							new INormalCallBack() {

								@Override
								public void success() {
									// TODO Auto-generated method stub
									Intent intent = getIntent();
									intent.putExtra("userNameString",
											userNameString);
									intent.putExtra("userPwdString",
											userPwdString);
									setResult(Activity.RESULT_OK, intent);
									finish();
									handler.sendEmptyMessage(0);
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
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		regBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 验证
	 */
	public boolean verification() {
		userNameString = regLoginName.getText().toString();
		userPwdString = regPwd.getText().toString();
		reUserPwdString = regRepwd.getText().toString();
		emailString = regEmail.getText().toString();
		if (userNameString.length() == 0) {
			regLoginName.setError(getResources().getString(
					R.string.regist_check_username_1));
			return false;
		}
		if (!checkUserId(userNameString)) {
			regLoginName.setError(getResources().getString(
					R.string.regist_check_username_2));
			return false;
		}
		if (emailString.length() == 0) {
			regEmail.setError(getResources().getString(
					R.string.regist_check_email_1));
			return false;
		}
		if (!emailCheck(emailString)) {
			regEmail.setError(getResources().getString(
					R.string.regist_check_email_2));
			return false;
		}
		if (userPwdString.length() == 0) {
			regPwd.setError(getResources().getString(
					R.string.regist_check_userpwd_1));
			return false;
		}
		if (!checkUserPwd(userPwdString)) {
			regPwd.setError(getResources().getString(
					R.string.regist_check_userpwd_1));
			return false;
		}
		if (!reUserPwdString.equals(userPwdString)) {
			regRepwd.setError(getResources().getString(
					R.string.regist_check_reuserpwd));
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
		if (userId.length() < 2|| userId.length() > 10)
			return false;
		/*String check = "^[a-zA-Z0-9\u4e00-\u9fa5_]+$";
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

	/**
	 * email格式匹配
	 * 
	 * @param email
	 * @return
	 */
	public boolean emailCheck(String email) {
		String check = "^([a-z0-ArrayA-Z]+[-|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(mContext, R.string.tip_regist_success_text,
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(mContext,
						AccountManager.Instace(mContext).errorInfo,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "BMKYR893PG94P9KSB72V");
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}


}
