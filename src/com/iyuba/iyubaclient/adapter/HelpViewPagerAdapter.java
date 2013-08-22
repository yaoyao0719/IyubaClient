package com.iyuba.iyubaclient.adapter;


import com.iyuba.iyubaclient.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	private int viewNum=3;
	
	public HelpViewPagerAdapter(Context context){
		this.mContext=context;
	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
//		Log.e("ViewPager", "销毁VIEW："+arg1);
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewNum;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// TODO Auto-generated method stub
		View view=null;
		if(arg1==0){
			view=new ImageView(mContext);
			((ImageView)view).setImageResource(R.drawable.help1);
			((ViewPager) arg0).addView(view,0);
		}else if(arg1==1){
			view=new ImageView(mContext);
			((ImageView)view).setImageResource(R.drawable.help2);
			((ViewPager) arg0).addView(view,0);
		}else if(arg1==2){
			view=new ImageView(mContext);
			((ImageView)view).setImageResource(R.drawable.help3);
			((ViewPager) arg0).addView(view,0);
		}
		return view;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==((View)arg1);
//		return false;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
}
