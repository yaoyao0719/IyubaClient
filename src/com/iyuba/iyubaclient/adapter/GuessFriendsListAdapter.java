/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.util.SDCardUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.NearFriendsListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.GuessFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.NearFriendInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;

/**
 * @author yao
 *
 */
public class GuessFriendsListAdapter extends BaseAdapter{
	private Context mContext;
	public ArrayList<GuessFriendInfo> mList = new ArrayList<GuessFriendInfo>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;

	/**
	 * @param mContext
	 */
	public GuessFriendsListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @param mContext
	 * @param mList
	 */
	public GuessFriendsListAdapter(Context mContext,
			ArrayList<GuessFriendInfo> mList) {
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

	public void addList(ArrayList<GuessFriendInfo> List) {
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
					R.layout.guessfriendlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (RoundAngleImageView) convertView
					.findViewById(R.id.guesslist_portrait);
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.guesslist_username);
			viewHolder.message=(TextView)convertView.findViewById(R.id.guesslist_message);
			viewHolder.gender=(ImageView)convertView.findViewById(R.id.guesslist_gender);
			convertView.setTag(viewHolder);			
			}
		else {
			viewHolder = (ViewHolder) convertView.getTag();	

		}
		// 设置资源
	
		viewHolder.username
				.setText(mList.get(position).username);
		if(mList.get(position).userBitmap!=null){
			viewHolder.portrait.setImageBitmap(mList.get(position).userBitmap);
		}else{
			viewHolder.portrait.setImageResource(R.drawable.defaultavatar);
		}
	
		String zhengze = "image[0-9]{2}|image[0-9]";	
		Emotion emotion=new Emotion();
		mList.get(position).doing=emotion.replace(mList.get(position).doing);
		try {
			SpannableString spannableString = ExpressionUtil.getExpressionString(mContext, mList.get(position).doing, zhengze);
			viewHolder.message.setText(spannableString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		//0保密 1男 2女
		if(mList.get(position).gender.equals("0")){
			viewHolder.gender.setVisibility(View.GONE);
		}else if(mList.get(position).gender.equals("1")){
			viewHolder.gender.setImageResource(R.drawable.user_info_male);
		}else if(mList.get(position).gender.equals("2")){
			viewHolder.gender.setImageResource(R.drawable.user_info_female);
		}else {
			viewHolder.gender.setVisibility(View.GONE);
		}
		return convertView;
	}



	class ViewHolder {
		RoundAngleImageView portrait;
		TextView username;
		TextView message;
		ImageView gender;
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


