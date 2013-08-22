/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.FansListAdapter.ViewHolder;
import com.iyuba.iyubaclient.adapter.MessageLetterListAdapter.ViewHolder1;
import com.iyuba.iyubaclient.adapter.MessageLetterListAdapter.ViewHolder2;

import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.RequestAddAttention;
import com.iyuba.iyubaclient.procotol.RequestCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseAddAttention;
import com.iyuba.iyubaclient.procotol.ResponseCancelAttention;
import com.iyuba.iyubaclient.sqlite.entity.NotificationInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;

/**
 * @author yao
 * 
 */
public class NotificationListAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<NotificationInfo> mList = new ArrayList<NotificationInfo>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;
	private String fansId;

	/**
	 * @param mContext
	 * @param mList
	 */
	public NotificationListAdapter(Context mContext,
			ArrayList<NotificationInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public NotificationListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public void addList(ArrayList<NotificationInfo> fansList) {
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

	public void clearList() {
		mList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final NotificationInfo curItem = mList.get(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.notificationlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.notificationlist_username);
			viewHolder.lastmessage = (TextView) convertView
					.findViewById(R.id.notificationlist_lastmessage);
			viewHolder.userImageView = (RoundAngleImageView) convertView
					.findViewById(R.id.notificationlist_portrait);
			viewHolder.dateline = (TextView) convertView
					.findViewById(R.id.notificationlist_dateline);
			viewHolder.pmnum = (TextView) convertView
					.findViewById(R.id.notificationlist_pmnum);
			viewHolder.test = (TextView) convertView.findViewById(R.id.test);
			convertView.setOnClickListener(new NewListener(
					viewHolder.test));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.lastmessage.setText(Html.fromHtml(
				replaceExpressPath(curItem.note), imageGetter, null));
	
		if(curItem.userBitmap==null){
			viewHolder.userImageView.setBackgroundResource(R.drawable.defaultavatar);
		}else{
			viewHolder.userImageView.setImageBitmap(curItem.userBitmap);
		}
		if (curItem.author.equals("") || curItem.author == null) {
			viewHolder.username.setText("系统通知");
		} else {
			viewHolder.username.setText(curItem.author);
		}
		viewHolder.pmnum.setText("");
		viewHolder.dateline.setText(DateFormat.format("MM-dd hh:ss",
				Long.parseLong(curItem.dateline) * 1000));
		viewHolder.test.setText(curItem.note);

		return convertView;
	}

	class ViewHolder {
		RoundAngleImageView userImageView;
		TextView username;
		TextView pmnum;
		TextView dateline;
		TextView lastmessage;
		TextView test;
	}

	private class NewListener implements OnClickListener {
		TextView testTextView;
		public NewListener(TextView test) {
			this.testTextView = test;
		}
		@Override
		public void onClick(View v) {
			if (testTextView.getVisibility() == View.GONE) {
				testTextView.setVisibility(View.VISIBLE);
				testTextView.getHeight();
				handler.sendEmptyMessage(0);
			} else {
				testTextView.setVisibility(View.GONE);
			}

		}
	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			source = source.substring(source.lastIndexOf("/") + 1);
			source = "/mnt/sdcard/com.iyuba.iyubaclient/comcom/" + source;
			drawable = Drawable.createFromPath(source);
			drawable.setBounds(0, 0, 30, 30);
			return drawable;
		}
	};

	private String replaceExpressPath(String str) {
		// TODO Auto-generated method stub
		str = str.replaceAll("(.*?)src=\"(.*?)", "$1src=\"http://iyuba.com/$2");
		return str;
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
			case 2:
				// 加关注
				ClientNetwork.Instace().asynGetResponse(
						new RequestAddAttention("10", fansId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseAddAttention res = (ResponseAddAttention) response;
								System.out.println(res.result + "!!!!!!!!!!!!");
								if (res.result.equals("501")) {
									handler.sendEmptyMessage(5);
									handler.sendEmptyMessage(0);// 修改为已关注
								} else {
									handler.sendEmptyMessage(6);
								}
								handler.sendEmptyMessage(4);
							}

						});
				break;
			case 3:
				// 取消关注
				ClientNetwork.Instace().asynGetResponse(
						new RequestCancelAttention("10", fansId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseCancelAttention responseFansList = (ResponseCancelAttention) response;
								System.out.println(responseFansList.result
										+ "!!!!!!!!!!!!");
								if (responseFansList.result.equals("510")) {
									handler.sendEmptyMessage(5);
									handler.sendEmptyMessage(0);// 修改为已关注
								} else {
									handler.sendEmptyMessage(5);
								}
								handler.sendEmptyMessage(4);
							}

						});
				break;
			case 4:
				//

				break;
			case 5:
				Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

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
}
