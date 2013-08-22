/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import javax.security.auth.PrivateCredentialPermission;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.R;

import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.RequestAddAttention;
import com.iyuba.iyubaclient.procotol.RequestCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseAddAttention;
import com.iyuba.iyubaclient.procotol.ResponseCancelAttention;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yao 私信列表Adapter
 */
public class MessageLetterListAdapter extends BaseAdapter {

	private Context mContext;
	public ArrayList<MessageLetter> mList = new ArrayList<MessageLetter>();
	private ViewHolder1 viewHolder1;
	private ViewHolder2 viewHolder2;
	private final int TYPE_1 = 0;
	private final int TYPE_2 = 1;
	int i = 0;
	private String userId;

	/**
	 * @param mContext
	 */
	public MessageLetterListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @param mContext
	 * @param mList
	 */
	public MessageLetterListAdapter(Context mContext,
			ArrayList<MessageLetter> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		if (position == 0) {
			return 0;
		} else {
			return mList.get(position + 1);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position + 1;
	}

	public void clearList() {
		mList.clear();
	}

	public void addList(ArrayList<MessageLetter> List) {
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
			switch (type) {
			case TYPE_1:
				convertView = layoutInflater.inflate(
						R.layout.button_writeletter, null);
				convertView.setPadding(0, 12, 0, 12);
				viewHolder1 = new ViewHolder1();
				viewHolder1.button_writeletter = (LinearLayout) convertView
						.findViewById(R.id.writeletterlayout);
				convertView.setTag(viewHolder1);
				break;
			case TYPE_2:
				convertView = layoutInflater.inflate(
						R.layout.messageletter_item, null);
				viewHolder2 = new ViewHolder2();
				viewHolder2.messageletter_portrait = (RoundAngleImageView) convertView
						.findViewById(R.id.messageletter_portrait);
				viewHolder2.messageletter_username = (TextView) convertView
						.findViewById(R.id.messageletter_username);
				viewHolder2.messageletter_pmnum = (TextView) convertView
						.findViewById(R.id.messageletter_pmnum);
				viewHolder2.messageletter_lastmessage = (TextView) convertView
						.findViewById(R.id.messageletter_lastmessage);
				viewHolder2.messageletter_dateline = (TextView) convertView
						.findViewById(R.id.messageletter_dateline);
				convertView.setTag(viewHolder2);
				break;
			}
		} else {
			switch (type) {
			case TYPE_1:
				viewHolder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				viewHolder2 = (ViewHolder2) convertView.getTag();			
				break;
			default:
				break;
			}

		}
		// 设置资源
		switch (type) {
		case TYPE_1:
			break;
		case TYPE_2:
			
			viewHolder2.messageletter_lastmessage.setText(Html.fromHtml(
					replaceExpressPath(mList.get(position - 1).lastmessage),
					imageGetter, null));
			viewHolder2.messageletter_username
					.setText(mList.get(position - 1).name);
			viewHolder2.messageletter_dateline.setText(DateFormat.format(
					"MM-dd hh:ss",
					Long.parseLong(mList.get(position - 1).dateline) * 1000));
			viewHolder2.messageletter_pmnum.setText("["
					+ mList.get(position - 1).pmnum + "]");
			if(mList
					.get(position - 1).userBitmap==null){
				viewHolder2.messageletter_portrait.setBackgroundResource(R.drawable.defaultavatar);
			}else{
				viewHolder2.messageletter_portrait.setImageBitmap(mList
						.get(position - 1).userBitmap);
			}	
			break;

		}
		return convertView;
	}

	class ViewHolder1 {
		LinearLayout button_writeletter;
	}

	class ViewHolder2 {
		RoundAngleImageView messageletter_portrait;
		TextView messageletter_username;
		TextView messageletter_pmnum;
		TextView messageletter_dateline;
		TextView messageletter_lastmessage;
	}

	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = position;
		if (p == 0) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
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

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					System.out.println("mList.size()===" + mList.size());
					for (i = 0; i < mList.size(); i++) {
						userId = mList.get(i).friendid;
						File file = new File(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						if (file.exists()) {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							mList.get(i).userBitmap = BitmapFactory.decodeFile(
									Constant.APP_DATA_PATH
											+ Constant.SDCARD_PORTEAIT_PATH
											+ mList.get(i).friendid + ".jpeg",
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
