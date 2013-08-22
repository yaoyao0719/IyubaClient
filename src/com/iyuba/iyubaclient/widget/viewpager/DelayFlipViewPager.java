package com.iyuba.iyubaclient.widget.viewpager;
import com.iyuba.iyubaclient.widget.viewpager.listener.OnItemChangedListener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public class DelayFlipViewPager extends ViewPager {

	private Context mContext;
	private GestureDetector gestureDetector;
	private boolean followGestures=false; // 切换模式，是否跟随收拾
	private OnItemChangedListener oicl;

	public DelayFlipViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initParameter(context);
	}

	public DelayFlipViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initParameter(context);
	}

	private void initParameter(Context context) {
		this.mContext = context;
		gestureDetector = new GestureDetector(mContext,
				new OnGestureListener() {

					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub
						if (velocityX > 0) { // 向左滑动
							int currItemIndex=getCurrentItem();
							if(currItemIndex>0){
								setCurrentItem(currItemIndex-1);
								if(oicl!=null){
									oicl.onItemChanged(currItemIndex-1);
								}
							}else{
								Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
							}
						} else { // 向右滑动
							int currItemIndex=getCurrentItem();
							if(currItemIndex<getAdapter().getCount()-1){
								setCurrentItem(currItemIndex+1);
								if(oicl!=null){
									oicl.onItemChanged(currItemIndex+1);
								}
							}else{
								Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
							}
						}
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}
				}, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(followGestures){
			return super.onTouchEvent(arg0);
		}else{
//			gestureDetector.onTouchEvent(arg0);
		}
		return true;
	}

	/**
	 * 设置是否跟随手势滑动
	 * @param followGestures
	 */
	public void setFollowGestures(boolean followGestures) {
		this.followGestures = followGestures;
	}
	
	/**
	 * 配置View变更监听
	 * @param oicl
	 */
	public void setOnItemChangedListener(OnItemChangedListener oicl) {
		this.oicl = oicl;
	}
	
}
