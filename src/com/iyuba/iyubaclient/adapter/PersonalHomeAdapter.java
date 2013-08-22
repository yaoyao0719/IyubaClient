/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.components.ConfigManager;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.DoingsListAdapter.ViewHolder;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.util.Constant;

/**
 * @author yao
 * 个人主页
 */
public class PersonalHomeAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<DoingsInfo> mList = new ArrayList<DoingsInfo>();
	private ViewHolder viewHolder;
	private TopViewHolder topViewHolder;
	private Bitmap userBitmap;
	private String userId;
	private String userImgSrc;
	int i = 0;
/*	private String stateNum,fansNum,journalNum;
	private String userName,userGender,stateInfo;*/
	private String currentuid;
	/**
	 * @param mContext
	 * @param mList
	 */
	public PersonalHomeAdapter(Context mContext, ArrayList<DoingsInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(2);
	}

	/**
	 * @param mContext
	 */
	public PersonalHomeAdapter(Context mContext,String uid) {
		this.mContext = mContext;
		this.currentuid=uid;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size()+1;
	}

	public void addList(ArrayList<DoingsInfo> doingsList) {
		mList.addAll(doingsList);
		DownLoadUserImg();
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
		return position+1;
	}
	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = position;
		if (p == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	public void clearList() {
		mList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type=getItemViewType(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (type) {
			case 0:
				convertView=vi.inflate(R.layout.user_info_header, null);
				topViewHolder=new TopViewHolder();
				topViewHolder.details_info=(FrameLayout)convertView.findViewById(R.id.fldetails_info);
				topViewHolder.fans_num=(TextView)convertView.findViewById(R.id.btn_fans_num);
				topViewHolder.follow_num=(TextView)convertView.findViewById(R.id.btn_follow_num);
				topViewHolder.journal_num=(TextView)convertView.findViewById(R.id.btn_journal_num);
				topViewHolder.tvName=(TextView)convertView.findViewById(R.id.tvName);
				topViewHolder.ivGender=(ImageView)convertView.findViewById(R.id.ivGender);
				topViewHolder.tvStateInfo=(TextView)convertView.findViewById(R.id.tvStateInfo);
				topViewHolder.lyAddFan=(FrameLayout)convertView.findViewById(R.id.lyAddFan);
				topViewHolder.lyEditInfo=(FrameLayout)convertView.findViewById(R.id.lyEditInfo);
				topViewHolder.lyCancelFan=(FrameLayout)convertView.findViewById(R.id.lyCancelFan);
				topViewHolder.lyMutualFan=(FrameLayout)convertView.findViewById(R.id.lyMutualFan);
				break;
			case 1:
				convertView = vi.inflate(R.layout.doingslist_item, null);
				viewHolder = new ViewHolder();
				viewHolder.replyNum = (TextView) convertView
						.findViewById(R.id.doingslist_replyNum);
				viewHolder.message = (TextView) convertView
						.findViewById(R.id.doingslist_message);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.doingslist_time);
				viewHolder.userImageView = (ImageView) convertView
						.findViewById(R.id.doingslist_userPortrait);
				viewHolder.username = (TextView) convertView
						.findViewById(R.id.doingslist_username);
				viewHolder.shareNum = (TextView) convertView
						.findViewById(R.id.doingslist_shareNum);
				convertView.setTag(viewHolder);
				break;	
			default:
				break;
			}	
		} else {
			switch (type) {
			case 0:
				topViewHolder=(TopViewHolder)convertView.getTag();
				break;
			case 1:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			default:
				break;
			}			
		}
		switch (type) {
		case 0:
			topViewHolder.tvName.setText(UserBasicInfoManager.Instace().responseBasicUserInfo.username);
			topViewHolder.fans_num.setText(UserBasicInfoManager.Instace().responseBasicUserInfo.friends);
			topViewHolder.follow_num.setText(UserBasicInfoManager.Instace().responseBasicUserInfo.friends);
			topViewHolder.journal_num.setText(UserBasicInfoManager.Instace().responseBasicUserInfo.blogs);			
			if(currentuid.equals(AccountManager.Instace(mContext).userId)){
				topViewHolder.lyEditInfo.setVisibility(View.VISIBLE);
			}else{
				topViewHolder.lyEditInfo.setVisibility(View.GONE);
				topViewHolder.lyAddFan.setVisibility(View.VISIBLE);
			}
			if(UserBasicInfoManager.Instace().responseBasicUserInfo.gender.equals("1")){
				topViewHolder.ivGender.setImageResource(R.drawable.user_info_female);
			}else if(UserBasicInfoManager.Instace().responseBasicUserInfo.gender.equals("2")){
				topViewHolder.ivGender.setImageResource(R.drawable.user_info_male);
			}
			 if(UserBasicInfoManager.Instace().responseBasicUserInfo.message!=null){
				 topViewHolder.tvStateInfo.setText(Html.fromHtml(UserBasicInfoManager.Instace().responseBasicUserInfo.message, imageGetter, null));
			    }else{
			    	topViewHolder.tvStateInfo.setText("");
			    }
			break;
		case 1:
			viewHolder.replyNum.setText(mList.get(position-1).replynum);
			viewHolder.shareNum.setText(mList.get(position-1).replynum);
			viewHolder.message.setText(Html.fromHtml(replaceExpressPath(mList.get(position-1).message),imageGetter,null));
			viewHolder.username.setText(mList.get(position-1).username);
			viewHolder.userImageView.setImageBitmap(mList.get(position-1).userBitmap);
			/*
			 * if (curDoings.userBitmap != null) {
			 * viewHolder.userImageView.setImageBitmap(curDoings.userBitmap); } else
			 * { viewHolder.userImageView.setImageResource(R.drawable.icon); }
			 */
			long time = Long.parseLong(mList.get(position-1).dateline) * 1000;
			DateFormat.format("yyyy-MM-dd hh:ss:mm", time);
			viewHolder.time.setText(DateFormat.format("yyyy-MM-dd hh:ss:mm", time));
			break;
		default:
			break;
		}
		
		return convertView;
	}
	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			source = source.substring(source.lastIndexOf("/") +1);
			source = "/mnt/sdcard/com.iyuba.iyubaclient/comcom/" + source;
			drawable = Drawable.createFromPath(source);
			//drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			drawable.setBounds(0, 0, 30,30);
			System.out.println("drawable.getIntrinsicWidth()=="+drawable.getIntrinsicWidth()+"   Height==="+drawable.getIntrinsicHeight());
			return drawable;
		}
	};
	private String  replaceExpressPath(String str) {
		// TODO Auto-generated method stub
		str=str.replaceAll("(.*?)src=\"(.*?)", "$1src=\"http://iyuba.com/$2");
		return str;
	}
	class ViewHolder {
		TextView username;
		TextView time;
		TextView replyNum;
		TextView message;
		TextView shareNum;
		ImageView userImageView;
	}
    class TopViewHolder{
    	private TextView tvEditInfo,tvName,tvStateInfo,tvStateNum,tvFansNum;
    	private ImageView ivGender,ivLeaguer,ivPortraitMask,ivPortrait;//gender是性別图标，League是等级图标,ivPortraitMask是头像
    	private FrameLayout details_info;
    	private TextView follow_num,journal_num,fans_num;
    	private FrameLayout lyEditInfo,lyAddFan,lyFollowNum,lyFansNum,lyJournalNum,lyCancelFan,lyMutualFan;
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
				//联网获取用户数据
				ClientNetwork.Instace().asynGetResponse(
						new RequestBasicUserInfo(currentuid,AccountManager.Instace(mContext).userId), new IResponseReceiver() {
							
							@Override
							public void onResponse(BaseHttpRequest request, BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseBasicUserInfo responseBasicUserInfo=(ResponseBasicUserInfo)response;
								System.out.println("responseBasicUserInfo.result=="+responseBasicUserInfo.result);
								if(responseBasicUserInfo.result.equals("201")){
									/*userName=responseBasicUserInfo.username;
									stateNum=responseBasicUserInfo.doings;//状态数
									fansNum=responseBasicUserInfo.friends;
									journalNum=responseBasicUserInfo.blogs;	
									userGender=responseBasicUserInfo.gender;
									stateInfo=responseBasicUserInfo.text;	*/							
									UserBasicInfoManager.Instace().responseBasicUserInfo=responseBasicUserInfo;									
								}
								handler.sendEmptyMessage(0);								
							}							
						});
				break;
			}
		}
	};

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		userImgSrc = ConfigManager.Instance().loadString("userImgSrc");
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					System.out.println("mList.size()==="+mList.size());
					for (i = 0; i < mList.size(); i++) {
						userId = mList.get(i).uid;
						File file = new File(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						System.out.println(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH + userId
								+ ".jpeg");
						if (file.exists()) {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							mList.get(i).userBitmap = BitmapFactory.decodeFile(
									Constant.APP_DATA_PATH
											+ Constant.SDCARD_PORTEAIT_PATH
											+ userId + ".jpeg", options);
							System.out.println("存在！！！");
						} else {
							DownLoadManager.Instace(mContext).downLoadImage(
									userImgSrc, userId, new DownLoadCallBack() {
										@Override
										public void downLoadSuccess(Bitmap image) {
											// TODO Auto-generated method stub
											mList.get(i).userBitmap = image;
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
