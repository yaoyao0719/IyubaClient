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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.FansListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Attention;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.image.ImageLoder;

/**
 * @author yao
 * 
 */
public class AttentionListAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<Attention> mList = new ArrayList<Attention>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;
	private String fansId;
	private ImageLoder imageLoder = new ImageLoder();

	/**
	 * @param mContext
	 * @param mList
	 */
	public AttentionListAdapter(Context mContext, ArrayList<Attention> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public AttentionListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public void addList(ArrayList<Attention> fansList) {
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
		final Attention curFans = mList.get(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.fanslist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.fanslist_username);
			viewHolder.message = (TextView) convertView
					.findViewById(R.id.fanslist_message);
			viewHolder.userImageView = (ImageView) convertView
					.findViewById(R.id.fanslist_portrait);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.username.setText(curFans.fusername);
		viewHolder.message.setText(curFans.doing);
		if (curFans.userBitmap == null) {
			viewHolder.userImageView
					.setBackgroundResource(R.drawable.defaultavatar);
		} else {
			viewHolder.userImageView.setImageBitmap(curFans.userBitmap);
		}
		return convertView;
	}

	class ViewHolder {
		TextView username;
		// Button attentionBtn;
		// Button cancelattentionBtn;
		TextView message;// 当前状态
		ImageView userImageView;
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
						mList.get(i).userBitmap = imageLoder
								.getBitmap(Constant.IMAGE_DOWN_PATH
										+ mList.get(i).followuid);

					}
				}
			}
		};
		t.start();
	}
}
