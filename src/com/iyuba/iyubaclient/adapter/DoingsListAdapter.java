/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.BlogListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
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
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author yao 心情(状态)
 */
public class DoingsListAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<DoingsInfo> mList = new ArrayList<DoingsInfo>();
	private ViewHolder viewHolder;
	private String userId;
	int i = 0;

	/**
	 * @param mContext
	 * @param mList
	 */
	public DoingsListAdapter(Context mContext, ArrayList<DoingsInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public DoingsListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public void addList(ArrayList<DoingsInfo> doingsList) {
		mList.addAll(doingsList);
		DownLoadUserImg();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void clearList() {
		mList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final DoingsInfo curDoings = mList.get(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.doingslist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.replyNum = (TextView) convertView
					.findViewById(R.id.doingslist_replyNum);
			viewHolder.message = (TextView) convertView
					.findViewById(R.id.doingslist_message);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.doingslist_time);
			viewHolder.userImageView = (RoundAngleImageView) convertView
					.findViewById(R.id.doingslist_userPortrait);
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.doingslist_username);
			viewHolder.shareNum = (TextView) convertView
					.findViewById(R.id.doingslist_shareNum);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.replyNum.setText(curDoings.replynum);
		viewHolder.shareNum.setText(curDoings.replynum);

		String zhengze = "image[0-9]{2}|image[0-9]";
		Emotion emotion = new Emotion();
		curDoings.message = emotion.replace(curDoings.message);
		try {
			SpannableString spannableString = ExpressionUtil
					.getExpressionString(mContext, curDoings.message, zhengze);
			viewHolder.message.setText(spannableString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		viewHolder.username.setText(curDoings.username);
		if (curDoings.userBitmap != null) {
			viewHolder.userImageView.setImageBitmap(curDoings.userBitmap);
		} else {
			viewHolder.userImageView.setImageResource(R.drawable.defaultavatar);
		}

		long time = Long.parseLong(curDoings.dateline) * 1000;
		DateFormat.format("yyyy-MM-dd hh:ss:mm", time);
		viewHolder.time.setText(DateFormat.format("yyyy-MM-dd hh:ss:mm", time));
		return convertView;
	}


	class ViewHolder {
		TextView username;
		TextView time;
		TextView replyNum;
		TextView message;
		TextView shareNum;
		RoundAngleImageView userImageView;
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


	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
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
}

