/**
 * 
 */
package com.iyuba.iyubaclient;

import com.iyuba.iyubaclient.manager.AccountManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


/**
 * @author yao
 *
 */
public class ApplicationActivityTest extends BaseActivity{
	private LinearLayout voa,csvoa,voavideo,bbc,cet4,cet6,read4,read6,
	meiyu,song,toeic,toefl,jlpt3,jlpt1,jlpt2,kaoyan;
	private Context mContext;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activiy_app);
	        mContext=this;
	        initWidget();
	    }
	private void initWidget() {
		// TODO Auto-generated method stub
		voa=(LinearLayout)findViewById(R.id.voa);
		voa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "VOA慢速英语");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=401&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		csvoa=(LinearLayout)findViewById(R.id.csvoa);
		csvoa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "VOA常速英语");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=402&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		voavideo=(LinearLayout)findViewById(R.id.voavideo);
		voavideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "VOA视频英语");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=408&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		bbc=(LinearLayout)findViewById(R.id.bbc);
		bbc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "BBC六分钟英语");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=403&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		cet4=(LinearLayout)findViewById(R.id.cet4);
		cet4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "英语四级听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=404&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		cet6=(LinearLayout)findViewById(R.id.cet6);
		cet6.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "英语六级听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=405&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		read4=(LinearLayout)findViewById(R.id.read4);
		read4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "英语四级阅读");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=409&uid="+AccountManager.Instace(mContext).userId+"&vip=1");
				startActivity(intent);
			}
		});
		read6=(LinearLayout)findViewById(R.id.read6);
		read6.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "英语六级阅读");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=410&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		meiyu=(LinearLayout)findViewById(R.id.meiyu);
		meiyu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "美语怎么说");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=407&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		song=(LinearLayout)findViewById(R.id.song);
		song.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "听歌学英语");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=406&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		
		toeic=(LinearLayout)findViewById(R.id.toeic);
		toeic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "托业听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=415&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		toefl=(LinearLayout)findViewById(R.id.toefl);
		toefl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "托福听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=416&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		jlpt1=(LinearLayout)findViewById(R.id.jlpt1);
		jlpt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "日语1级听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=412&uid="+AccountManager.Instace(mContext).userId+"&vip=1");
				startActivity(intent);
			}
		});
		jlpt2=(LinearLayout)findViewById(R.id.jlpt2);
		jlpt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "日语2级听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=413&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		jlpt3=(LinearLayout)findViewById(R.id.jlpt3);
		jlpt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "日语3级听力");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=414&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
		kaoyan=(LinearLayout)findViewById(R.id.kaoyan);
		kaoyan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("appname", "考研阅读");
				intent.putExtra("url", "http://m.iyuba.com.cn/mobileApp.jsp?appId=411&uid="+AccountManager.Instace(mContext).userId);
				startActivity(intent);
			}
		});
	}
}
