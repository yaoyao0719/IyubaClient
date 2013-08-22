/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.HashMap;

import org.ths.frame.THSActivity;



import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author yao
 * 
 */
public class AboutActivity extends Activity implements Callback,OnClickListener,WeiboActionListener{

	private Button backButton, moreapp, weixin, weibo;
	protected Handler handler;
	private Context mContext;
	AbstractWeibo abstractWeibo=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);		
		mContext=this;
		backButton = (Button) findViewById(R.id.about_back_btn);
		moreapp = (Button) findViewById(R.id.moreapp);
		weixin = (Button) findViewById(R.id.weixin);
		weibo = (Button) findViewById(R.id.weibo);
		handler=new Handler(this);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutActivity.this.finish();
			}
		});
		moreapp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://app.iyuba.com/android");
				startActivity(intent);
			}
		});
		weixin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://weixin.qq.com/r/FHWEnAXE2FinrUH-9yAG"));
				startActivity(i);
			}
		});
		weibo.setOnClickListener(this);
		
	}


	
	/** 处理操作结果 */
	public boolean handleMessage(Message msg) {
		AbstractWeibo weibo1 = (AbstractWeibo) msg.obj;
		String text = AboutActivity.actionToString(msg.arg2);
		switch (msg.arg1) {
			case 1: { // 成功
				text = weibo1.getName() + " completed at " + text;
				Toast.makeText(AboutActivity.this, "成功关注‘爱语吧’", Toast.LENGTH_SHORT).show();
			}
			break;
			case 2: { // 失败
				//text = weibo1.getName() + " caught error at " + text;
				
				if(text.equals("ACTION_FOLLOWING_USER")){
					Toast.makeText(AboutActivity.this, "您已关注‘爱语吧’", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(AboutActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
				}
				
			}
			break;
			case 3: { // 取消
				text = weibo1.getName() + " canceled at " + text;
				Toast.makeText(AboutActivity.this, "取消关注", Toast.LENGTH_SHORT).show();
			}
			break;
		}

		return false;
	}
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
			case AbstractWeibo.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";
			case AbstractWeibo.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";
			case AbstractWeibo.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";
			case AbstractWeibo.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";
			case AbstractWeibo.ACTION_TIMELINE: return "ACTION_TIMELINE";
			case AbstractWeibo.ACTION_USER_INFOR: return "ACTION_USER_INFOR";
			case AbstractWeibo.ACTION_SHARE: return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.weibo:
			abstractWeibo = AbstractWeibo.getWeibo(
					AboutActivity.this, SinaWeibo.NAME);
			//abstractWeibo.SSOSetting(true);
			abstractWeibo.setWeiboActionListener(this);
			abstractWeibo.followFriend("1868097145");
			break;

		default:
			break;
		}
	}



	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(abstractWeibo!=null){
			abstractWeibo.removeAccount();
		}
		
		super.finish();
	}



	@Override
	public void onCancel(AbstractWeibo weibo, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}



	@Override
	public void onComplete(AbstractWeibo weibo, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;	
		handler.sendMessage(msg);
	}



	@Override
	public void onError(AbstractWeibo weibo, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;		
		handler.sendMessage(msg);
	}
	
}
