package com.iyuba.iyubaclient;

import org.ths.frame.components.ConfigManager;

import com.iyuba.iyubaclient.adapter.HelpViewPagerAdapter;
import com.iyuba.iyubaclient.widget.viewpager.DelayFlipViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	private DelayFlipViewPager viewPager;
	private HelpViewPagerAdapter helpViewpagerAdapter;
	private Button button_skip;
	private int goInfo = 0; // 0=第一次使用程序 1=从更多界面进入
	private int lastIntoCount;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help);
		this.mContext = this;
		goInfo = getIntent().getExtras().getInt("isFirstInfo");

		viewPager = (DelayFlipViewPager) findViewById(R.id.viewpager_helpview);
		viewPager.setFollowGestures(true);
		helpViewpagerAdapter = new HelpViewPagerAdapter(this);
		button_skip = (Button) findViewById(R.id.button_skip);

		viewPager.setAdapter(helpViewpagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0: // 停止变更
					if (viewPager.getCurrentItem() == helpViewpagerAdapter
							.getCount() - 1) {
						lastIntoCount = lastIntoCount + 1;
					}
					break;
				case 1: // 正在变更
					// lastIntoCount=0;
					break;
				case 2: // 已经变更
					lastIntoCount = 0;
					break;
				}
				if (lastIntoCount > 1) {
					if (arg0 == 0) {
						// Toast.makeText(mContext, "阅读完毕", Toast.LENGTH_SHORT)
						// .show();
						ConfigManager.Instance().putInt("isFirstInfo", 1);
						if (goInfo == 0) {
							Intent intent = new Intent();
							intent.setClass(mContext, LoginActivity.class);// SelectYearActivity
							startActivity(intent);
							HelpActivity.this.finish();
						}
						HelpActivity.this.finish();
					}
				}
			}
		});
		button_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConfigManager.Instance().putInt("isFirstInfo", 1);
				if (goInfo == 0) {
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);// SelectYearActivity
					startActivity(intent);
					finish();
				}
				finish();
			}
		});
	}

}
