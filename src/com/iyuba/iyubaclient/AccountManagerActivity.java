/**
 * 
 */
package com.iyuba.iyubaclient;

import java.io.File;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;
import com.iyuba.iyubaclient.util.CheckGrade;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.util.image.ImageLoder;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.iyuba.iyubaclient.manager.DatabaseManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author yao 账户管理
 */
public class AccountManagerActivity extends THSActivity {

	private Button back, vip, charge;
	private TextView name, coin, date, points;// 昵称，爱语币，vip截止日期
	private TextView follows, fans, blogs, hot;// 关注，粉丝，日志，人气
	private UserInfo userInfo = null;
	private RoundAngleImageView imageView;
	private CustomDialog waitingDialog;
	private Bitmap userBitmap;
	private String userId;
	private CheckGrade checkGrade = new CheckGrade();
	private ImageView level;
	private ImageLoder imageLoder = new ImageLoder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountmanager);
		waitingDialog = waitingDialog();
		userId = AccountManager.Instace(mContext).userId;
		initWidget();
		handler.sendEmptyMessage(0);
		DownLoadUserImg();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		imageView = (RoundAngleImageView) findViewById(R.id.accountmanager_image);
		back = (Button) findViewById(R.id.accountmanager_back_btn);
		vip = (Button) findViewById(R.id.accountmanager_vip);
		charge = (Button) findViewById(R.id.accountmanager_charge);
		name = (TextView) findViewById(R.id.accountmanager_name);
		coin = (TextView) findViewById(R.id.accountmanager_coin);
		date = (TextView) findViewById(R.id.accountmanager_date);
		hot = (TextView) findViewById(R.id.accountmanager_hot);
		follows = (TextView) findViewById(R.id.accountmanager_follow);
		fans = (TextView) findViewById(R.id.accountmanager_fans);
		blogs = (TextView) findViewById(R.id.accountmanager_blogs);
		points = (TextView) findViewById(R.id.point);
		level = (ImageView) findViewById(R.id.account_level);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		vip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, BuyVipActivity.class);
				startActivity(intent);
			}
		});
		charge.setOnClickListener(new View.OnClickListener() {

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

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				ClientNetwork.Instace().asynGetResponse(
						new RequestBasicUserInfo(
								AccountManager.Instace(mContext).userId,
								AccountManager.Instace(mContext).userId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseBasicUserInfo responseBasicUserInfo = (ResponseBasicUserInfo) response;
								if (responseBasicUserInfo.result.equals("201")) {
									userInfo = responseBasicUserInfo.userInfo;
									userInfo.uid = userId;
									userInfo.username = AccountManager
											.Instace(mContext).userName;
									DataManager.Instace().userInfo = userInfo;
									handler.sendEmptyMessage(6);// 将UserInfo数据保存到本地数据库
								}
								handler.sendEmptyMessage(4);

							}
						});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				setListener();
				handler.sendEmptyMessage(3);
				break;
			case 5:
				if (userBitmap == null) {
					imageView.setImageResource(R.drawable.defaultavatar);
				} else {
					imageView.setImageBitmap(userBitmap);
				}
				break;
			case 6:
				long count = DatabaseManager.Instace(mContext).getUserHelper()
						.insertUserDataForSingle(userInfo);
				Log.e("count", "" + count);
				break;
			case 10:

				break;
			default:
				break;
			}
		}
	};

	private void setListener() {
		// TODO Auto-generated method stub
		name.setText(userInfo.username);
		coin.setText(":  " + DataManager.Instace().userInfo.Amount + "");
		if (DataManager.Instace().userInfo.vipStatus == 1) {
			date.setText("VIP "
					+ DateFormat.format(
							"yyyy-MM-dd",
							Long.parseLong(DataManager.Instace().userInfo.expireTime) * 1000));
		} else {
			date.setText("普通用户");
		}
		hot.setText(userInfo.shengwang);
		follows.setText(userInfo.friends);
		fans.setText(userInfo.friends);
		blogs.setText(userInfo.blogs);
		points.setText(userInfo.icoins);
		LevelSetImage(checkGrade.Check(userInfo.icoins));
		
	}

	private void LevelSetImage(int i) {
		// TODO Auto-generated method stub
		if (i == 1) {
			level.setImageResource(R.drawable.level1);
		} else if (i == 2) {
			level.setImageResource(R.drawable.level2);
		} else if (i == 3) {
			level.setImageResource(R.drawable.level3);
		} else if (i == 4) {
			level.setImageResource(R.drawable.level4);
		} else if (i == 5) {
			level.setImageResource(R.drawable.level5);
		} else if (i == 6) {
			level.setImageResource(R.drawable.level6);
		} else if (i == 7) {
			level.setImageResource(R.drawable.level7);
		} else if (i == 8) {
			level.setImageResource(R.drawable.level8);
		} else if (i == 9) {
			level.setImageResource(R.drawable.level9);
		} else if (i == 10) {
			level.setImageResource(R.drawable.level10);
		}

	}

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					userBitmap = imageLoder.getBitmap(Constant.IMAGE_DOWN_PATH
							+ userId);
					handler.sendEmptyMessage(5);
				}
			}
		};
		t.start();
	}

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}
}
