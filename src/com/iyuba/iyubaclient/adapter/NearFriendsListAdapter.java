/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.MessageLetterListAdapter.ViewHolder1;
import com.iyuba.iyubaclient.adapter.MessageLetterListAdapter.ViewHolder2;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.NearFriendInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author yao
 *
 */
public class NearFriendsListAdapter extends BaseAdapter{
	private Context mContext;
	public ArrayList<NearFriendInfo> mList = new ArrayList<NearFriendInfo>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;

	/**
	 * @param mContext
	 */
	public NearFriendsListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @param mContext
	 * @param mList
	 */
	public NearFriendsListAdapter(Context mContext,
			ArrayList<NearFriendInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size() ;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position ;
	}

	public void clearList() {
		mList.clear();
	}

	public void addList(ArrayList<NearFriendInfo> List) {
		mList.addAll(List);
		DownLoadUserImg();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		// 无convertView，需要new出各个控件
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.nearlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (RoundAngleImageView) convertView
					.findViewById(R.id.nearlist_portrait);
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.nearlist_username);
			viewHolder.distance = (TextView) convertView
					.findViewById(R.id.nearlist_distance);
			convertView.setTag(viewHolder);
			
			}
		else {
			viewHolder = (ViewHolder) convertView.getTag();	

		}
		// 设置资源
	
		viewHolder.username.setText(mList.get(position).username);
		if(mList.get(position).userBitmap!=null){
			viewHolder.portrait.setImageBitmap(mList.get(position).userBitmap);
		}else{
			viewHolder.portrait.setImageResource(R.drawable.defaultavatar);
		}
		Double distance=mList.get(position).distance*1000;
		if((int)Math.ceil(distance)<100){
			viewHolder.distance.setText("100米以内");
		}else if((int)Math.ceil(distance)==100){
			viewHolder.distance.setText("100米以内");
		}else if((int)Math.ceil(distance)>100&&(int)Math.ceil(distance)<1000){
			int a=(int)Math.ceil(distance)/100+1;
			viewHolder.distance.setText(a*100+"米以内");
		}else if((int)Math.ceil(distance)>1000||(int)Math.ceil(distance)==1000){
			viewHolder.distance.setText("2000米以内");
		}	
		return convertView;
	}
	class ViewHolder {
		RoundAngleImageView portrait;
		TextView username;
		TextView distance;
	}
	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					System.out.println("mList.size()===" + mList.size());
					for (i = 0; i < mList.size(); i++) {
						userId = mList.get(i).uid;
						File file = new File(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						if (file.exists()) {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							mList.get(i).userBitmap = BitmapFactory.decodeFile(
									Constant.APP_DATA_PATH
											+ Constant.SDCARD_PORTEAIT_PATH
											+ mList.get(i).uid + ".jpeg",
									options);
						} else {
							DownLoadManager.Instace(mContext).downLoadImage(
									Constant.IMAGE_DOWN_PATH + userId, userId,
									new DownLoadCallBack() {
										@Override
										public void downLoadSuccess(Bitmap image) {
											// TODO Auto-generated method stub
											if (i == mList.size()) {
												mList.get(mList.size() - 1).userBitmap = image;
											} else if (i < mList.size()) {
												mList.get(i).userBitmap = image;
											}

											handler.sendEmptyMessage(0);
										}

										@Override
										public void downLoadFaild(String error) {
											// TODO Auto-generated method stub
										}
									}, true);
						}
					}
				}
			}
		};
		t.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
			case 1:
				DownLoadUserImg();
				break;
			}
		}
	};
}

