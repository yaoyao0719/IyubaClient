package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.util.SDCardUtil;

import com.flurry.org.apache.avro.generic.GenericData.Array;
import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.AttentionListAdapter.ViewHolder;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.NotificationInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<NotificationInfo> mList=new ArrayList<NotificationInfo>();
	private ViewHolder viewHolder=new ViewHolder();
	int i=0;
	private String userId;
	public ReplyListAdapter(Context context) {
		this.mContext=context;
		// TODO Auto-generated constructor stub
	}
	public ReplyListAdapter(Context mContext,ArrayList<NotificationInfo> mList){
		this.mContext=mContext;
		this.mList=mList;
		handler.sendEmptyMessage(1);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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

	public void addList(ArrayList<NotificationInfo> List) {
		mList.addAll(List);
		DownLoadUserImg();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater layoutInflater=(LayoutInflater)mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=layoutInflater.inflate(R.layout.replylist_item, null);
			viewHolder.userImageView=(RoundAngleImageView)convertView.findViewById(R.id.replylist_portrait);
			viewHolder.note=(TextView)convertView.findViewById(R.id.replylist_note);
			viewHolder.dateline=(TextView)convertView.findViewById(R.id.replylist_dateline);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.dateline.setText(DateFormat.format("MM-dd hh:ss",
				Long.parseLong(mList.get(position).dateline) * 1000));
		viewHolder.note.setText(mList.get(position).note);
		if(mList.get(position).userBitmap==null||mList.get(position).userBitmap.equals("")){
			viewHolder.userImageView.setImageResource(R.drawable.defaultavatar);
		}else{
			viewHolder.userImageView.setImageBitmap(mList.get(position).userBitmap);
		}
		return convertView;
	}
	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					System.out.println("mList.size()===" + mList.size());
					for (i = 0; i < mList.size(); i++) {
						userId = mList.get(i).authorid;
						File file = new File(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						if (file.exists()) {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							mList.get(i).userBitmap = BitmapFactory.decodeFile(
									Constant.APP_DATA_PATH
											+ Constant.SDCARD_PORTEAIT_PATH
											+ mList.get(i).authorid + ".jpeg",
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
	class ViewHolder{
		RoundAngleImageView userImageView;
		TextView note;
		TextView dateline;
	}
}
