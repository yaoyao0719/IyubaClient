/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;
import org.ths.frame.util.SDCardUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iyuba.iyubaclient.MessageLetterContentActivity;
import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.RecentVisitInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;

/**
 * @author yao
 *
 */
public class RecentVisitListAdapter extends BaseAdapter{
	private Context mContext;
	public  ArrayList<RecentVisitInfo> mList=new ArrayList<RecentVisitInfo>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;
	/**
	 * @param mContext
	 * @param mList
	 */
	public RecentVisitListAdapter(Context mContext, ArrayList<RecentVisitInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public RecentVisitListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	public void addList(ArrayList<RecentVisitInfo> fansList){
		mList.addAll(fansList);
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
	public void clearList(){
		mList.clear();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final RecentVisitInfo curFans = mList.get(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.fanslist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.fanslist_username);
			viewHolder.message = (TextView) convertView
					.findViewById(R.id.fanslist_message);
			viewHolder.userImageView = (RoundAngleImageView) convertView
					.findViewById(R.id.fanslist_portrait);
			viewHolder.sendMessage=(ImageView)convertView .findViewById(R.id.fans_sendmessage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.username.setText(curFans.vusername);
		if(curFans.userBitmap==null){
			viewHolder.userImageView.setBackgroundResource(R.drawable.defaultavatar);
		}else{
			viewHolder.userImageView.setImageBitmap(curFans.userBitmap);
		}
		viewHolder.message.setVisibility(View.GONE);
		viewHolder.sendMessage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				MessageLetter letter=new MessageLetter();
				letter.name=curFans.vusername;
				letter.friendid=curFans.vuid;
				DataManager.Instace().letter=letter;
				intent.setClass(mContext, MessageLetterContentActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView username;
		TextView message;//当前状态
		RoundAngleImageView userImageView;
		ImageView sendMessage;
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
						userId = mList.get(i).vuid;
						File file = new File(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						if (file.exists()) {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							mList.get(i).userBitmap = BitmapFactory.decodeFile(
									Constant.APP_DATA_PATH
											+ Constant.SDCARD_PORTEAIT_PATH
											+ mList.get(i).vuid + ".jpeg",
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

