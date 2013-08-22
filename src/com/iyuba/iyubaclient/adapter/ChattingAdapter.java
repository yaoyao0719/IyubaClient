package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.entity.Attention;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.RequestAddAttention;
import com.iyuba.iyubaclient.procotol.RequestCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseAddAttention;
import com.iyuba.iyubaclient.procotol.ResponseCancelAttention;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetterContent;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.util.image.ImageLoder;

import android.Manifest.permission;
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
import android.widget.Toast;

public class ChattingAdapter extends BaseAdapter {
	protected static final String TAG = "ChattingAdapter";
	private Context context;
	private List<MessageLetterContent> mList = new ArrayList<MessageLetterContent>();
	private ImageLoder imageLoder = new ImageLoder();
	private String uid;
	int i = 0;
	private String userId;
	private String fansId;

	public ChattingAdapter(Context context,
			List<MessageLetterContent> messages, String uid) {
		this.context = context;
		this.mList = messages;
		this.uid = uid;
		handler.sendEmptyMessage(1);
	}

	public ChattingAdapter(Context context, String uid) {

		this.context = context;
		this.uid = uid;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	public void addList(ArrayList<MessageLetterContent> list) {
		mList.addAll(list);
		DownLoadUserImg();

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearList() {
		mList.clear();
	}

	public void addLetter(MessageLetterContent letter) {
		mList.add(letter);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MessageLetterContent message = mList.get(position);
		System.out.println("position===" + position + "   message=="
				+ message.authorid);
		if (message.authorid.equals(uid)) {
			message.setDirection(1);
		} else {
			message.setDirection(0);
		}
		if (convertView == null
				|| (holder = (ViewHolder) convertView.getTag()).flag != message.direction) {
			holder = new ViewHolder();
			if (message.direction == ChatMessage.MESSAGE_FROM) {
				holder.flag = ChatMessage.MESSAGE_FROM;
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatting_item_from, null);
			} else {
				holder.flag = ChatMessage.MESSAGE_TO;
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatting_item_to, null);
			}
			holder.text = (TextView) convertView
					.findViewById(R.id.chatting_content_itv);
			holder.time = (TextView) convertView
					.findViewById(R.id.chatting_time_tv);
			holder.userImageView = (RoundAngleImageView) convertView
					.findViewById(R.id.chatting_content_iv);
			convertView.setTag(holder);
		}
		String zhengze = "image[0-9]{2}|image[0-9]";
		Emotion emotion = new Emotion();
		message.message = emotion.replace(message.message);
		try {
			SpannableString spannableString = ExpressionUtil
					.getExpressionString(context, message.message, zhengze);
			holder.text.setText(spannableString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// holder.text.setText(Html.fromHtml(replaceExpressPath(message.message),imageGetter,null));
		holder.time.setText(DateFormat.format("MM-dd hh:ss",
				Long.parseLong(message.dateline) * 1000));
		if (message.userBitmap == null) {
			holder.userImageView
					.setBackgroundResource(R.drawable.defaultavatar);
		} else {
			holder.userImageView.setImageBitmap(message.userBitmap);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView text;
		RoundAngleImageView userImageView;
		TextView time;
		int flag;
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
										+ mList.get(i).authorid);

					}
				}
			}
		};
		t.start();
	}
}
