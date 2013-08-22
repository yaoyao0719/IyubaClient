/**
 * 
 */
package com.iyuba.iyubaclient;

import org.ths.frame.THSActivity;
import org.ths.frame.components.ConfigManager;
import org.ths.frame.widget.dialog.CustomDialog;
import org.ths.frame.widget.dialog.WaittingDialog;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.OnResultListener;
import com.iyuba.iyubaclient.manager.PayManager;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author yao
 * 购买VIP
 */ 
public class BuyVipActivity extends THSActivity{
	private View relativeLayout_noLogin, relativetLayout_login; // 登录提示面板
	private ImageView quarter, half_year, year, lifelong;
	private TextView username, account;
	private Button loginBtn, buy_iyubi, back;
	private Context mContext;
	private String pay_account,productId;//支付金额、购买VIP时间
	private int iyubi_amount;
	UserInfo user;
	
	private CustomDialog wettingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_vip);
		mContext = this;
		wettingDialog =waitingDialog();
	
		relativeLayout_noLogin = findViewById(R.id.relativeLayout_noLogin);
		relativetLayout_login = findViewById(R.id.relativeLayout_Login);
		loginBtn = (Button) findViewById(R.id.button_to_login);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
			}
		});
		if (!AccountManager.Instace(mContext).checkLogin()) {// 未登录
			relativeLayout_noLogin.setVisibility(View.VISIBLE);
			relativetLayout_login.setVisibility(View.GONE);
		} else {
			relativeLayout_noLogin.setVisibility(View.GONE);
			relativetLayout_login.setVisibility(View.VISIBLE);
		}
		init();
	}

	private void init() {
		back = (Button) findViewById(R.id.button_back);
		quarter = (ImageView) findViewById(R.id.quarter);
		half_year = (ImageView) findViewById(R.id.half_year);
		year = (ImageView) findViewById(R.id.year);
		lifelong = (ImageView) findViewById(R.id.life_long);
		buy_iyubi = (Button) findViewById(R.id.buy_iyubi);
		username = (TextView) findViewById(R.id.buy_username);
		username.setText(AccountManager.Instace(mContext).userName);
		account = (TextView) findViewById(R.id.buy_account);
		account.setText(DataManager.Instace().userInfo.Amount+"");
		iyubi_amount = DataManager.Instace().userInfo.Amount;
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ConfigManager.Instance().loadString("validity")
						.equals("终身VIP"))
				{
					Toast.makeText(mContext, "您已经是终身VIP",
						     Toast.LENGTH_SHORT).show();
				}		
				else if (v == quarter) {
					if (iyubi_amount >= 100) {
						pay_account = "100";
						productId="1";
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				} else if (v == half_year) {
					if (iyubi_amount >= 300) {
						pay_account = "300";
						productId="3";
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				} else if (v == year) {
					if (iyubi_amount >= 550) {
						pay_account = "550";
						productId="6";
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				} else if (v == lifelong) {
					if (iyubi_amount >= 990) {
						pay_account = "990";
						productId="12";
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				}
			}
		};
		quarter.setOnClickListener(ocl);
		half_year.setOnClickListener(ocl);
		year.setOnClickListener(ocl);
		lifelong.setOnClickListener(ocl);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		buy_iyubi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url",
						"http://app.iyuba.com/wap/index.jsp?uid="
								+ AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
	}

	private void buyVip() {
	//	wettingDialog.show();
		PayManager.Instance(mContext).payAmount(
				AccountManager.Instace(mContext).userId, pay_account,productId,
				new OnResultListener() {

					@Override
					public void OnSuccessListener(String msg) {
						Message hmsg = handler.obtainMessage(2, msg);// 对话框提示支付成功
						handler.sendMessage(hmsg);
					}

					@Override
					public void OnFailureListener(String msg) {
						handler.sendEmptyMessage(1);
					}
				});
	}

	@Override
	public void finish() {
		//AccountManager.Instace(mContext).Refresh();
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!AccountManager.Instace(mContext).checkLogin()) {// 未登录
			relativeLayout_noLogin.setVisibility(View.VISIBLE);
			relativetLayout_login.setVisibility(View.GONE);
		} else {
			init();
			relativeLayout_noLogin.setVisibility(View.GONE);
			relativetLayout_login.setVisibility(View.VISIBLE);
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Dialog dialog = new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("确定要购买么")
						.setPositiveButton("购买",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										buyVip();
									}
								}).setNeutralButton("取消", null).create();
				dialog.show();// 如果要显示对话框，一定要加上这句
				break;
			case 1:
				//wettingDialog.dismiss();
				Dialog dialog1 = new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您的爱语币余额不足支付，是否充值？")
						.setPositiveButton("充值",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										Intent intent = new Intent();
										intent.setClass(mContext,
												WebActivity.class);
										intent.putExtra(
												"url",
												"http://app.iyuba.com/wap/index.jsp?uid="
														+ AccountManager
																.Instace(mContext).userId);
										startActivity(intent);
									}
								}).setNeutralButton("取消", null).create();
				dialog1.show();// 如果要显示对话框，一定要加上这句
				break;
			case 2:
				//wettingDialog.dismiss();
				Toast.makeText(mContext, "支付成功，您现在的余额是："+ msg.obj.toString(),
					     Toast.LENGTH_SHORT).show();
				ConfigManager.Instance().putString("currUserAmount",
						msg.obj.toString());
				account.setText(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};
	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder
				.setContentView(layout).create();
		return cDialog;
	}
}

