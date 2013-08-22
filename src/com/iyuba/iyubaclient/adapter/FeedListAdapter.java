/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.AttentionListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.util.image.ImageLoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author yao 新鲜事
 */
public class FeedListAdapter extends BaseAdapter {

	private Context mContext;
	public ArrayList<FeedInfo> mList = new ArrayList<FeedInfo>();
	private ViewHolder viewHolder;
	int i = 0;
	private ImageLoder imageLoder = new ImageLoder();

	/**
	 * @param mContext
	 * @param mList
	 */
	public FeedListAdapter(Context mContext, ArrayList<FeedInfo> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	/**
	 * @param mContext
	 */
	public FeedListAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void addList(ArrayList<FeedInfo> list) {
		mList.addAll(list);
		DownLoadUserImg();
	}

	public void clearList() {
		if (mList.size() == 0 || mList == null) {

		} else {
			mList.clear();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.feedlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.feedlist_username);
			viewHolder.body = (TextView) convertView
					.findViewById(R.id.feedlist_body);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.feedlist_message);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.feedlist_time);
			viewHolder.userImageView = (RoundAngleImageView) convertView
					.findViewById(R.id.feedlist_userPortrait);
			viewHolder.loc= (TextView) convertView
					.findViewById(R.id.feedlist_locationDesc);
			viewHolder.weizhi= (ImageView) convertView
					.findViewById(R.id.feedlist_weizhi);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.userName.setText(mList.get(position).username);
		// title 有可能包括表情

		String zhengze = "image[0-9]{2}|image[0-9]";
		Emotion emotion = new Emotion();
		mList.get(position).title = emotion.replace(mList.get(position).title);
		try {
			SpannableString spannableString = ExpressionUtil
					.getExpressionString(mContext, mList.get(position).title,
							zhengze);
			viewHolder.title.setText(spannableString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		viewHolder.time.setText(DateFormat.format("yyyy-MM-dd hh:mm:ss",
				Long.parseLong(mList.get(position).dateline) * 1000));
		if (mList.get(position).userBitmap != null) {
			viewHolder.userImageView
					.setImageBitmap(mList.get(position).userBitmap);
		} else {
			viewHolder.userImageView.setImageResource(R.drawable.defaultavatar);
		}
		viewHolder.body.setText(Html.fromHtml(mList.get(position).body));
		if(mList.get(position).locationDesc!=null&&!mList.get(position).locationDesc.equals("")){
			viewHolder.loc.setText(mList.get(position).locationDesc);
		}else{
			viewHolder.weizhi.setVisibility(View.GONE);
			viewHolder.loc.setVisibility(View.GONE);
		}	
		return convertView;
	}

	class ViewHolder {
		TextView userName;
		TextView title;
		TextView body;
		RoundAngleImageView userImageView;
		TextView time;
		TextView loc;
		ImageView weizhi;
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
			default:
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
						mList.get(i).userBitmap = imageLoder
								.getBitmap(Constant.IMAGE_DOWN_PATH
										+ mList.get(i).uid);

					}
				}
			}
		};
		t.start();
	}

}
