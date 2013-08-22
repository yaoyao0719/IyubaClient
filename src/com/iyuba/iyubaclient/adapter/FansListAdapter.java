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

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.BlogListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.RequestAddAttention;
import com.iyuba.iyubaclient.procotol.RequestFansList;
import com.iyuba.iyubaclient.procotol.RequestCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseAddAttention;
import com.iyuba.iyubaclient.procotol.ResponseCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseFansList;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yao
 * 粉丝列表Adapter
 */
public class FansListAdapter extends BaseAdapter{
	private Context mContext;
	public  ArrayList<Fans> mList=new ArrayList<Fans>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;
	private String fansId;
	private ImageLoder imageLoder = new ImageLoder();
	/**
	 * @param mContext
	 * @param mList
	 */
	public FansListAdapter(Context mContext, ArrayList<Fans> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public FansListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	public void addList(ArrayList<Fans> fansList){
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
		final Fans curFans = mList.get(position);
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.username.setText(curFans.username);
		viewHolder.message.setText(curFans.doing);
		viewHolder.userImageView.setImageBitmap(curFans.userBitmap);
		if(curFans.userBitmap==null){
			viewHolder.userImageView.setBackgroundResource(R.drawable.defaultavatar);
		}else{
			viewHolder.userImageView.setImageBitmap(curFans.userBitmap);
		}	
		return convertView;
	}
	class ViewHolder{
		TextView username;
		TextView message;//当前状态
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
			case 2:
				//加关注
				ClientNetwork.Instace().asynGetResponse(new RequestAddAttention("10", fansId) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseAddAttention res=(ResponseAddAttention)response;
						System.out.println(res.result+"!!!!!!!!!!!!");
						if(res.result.equals("501")){							
							handler.sendEmptyMessage(5);
							handler.sendEmptyMessage(0);//修改为已关注
						}else{		
							handler.sendEmptyMessage(6);
						}
						handler.sendEmptyMessage(4);
					}
					
				});
				break;
			case 3:
				//取消关注
				ClientNetwork.Instace().asynGetResponse(new RequestCancelAttention("10", fansId) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseCancelAttention responseFansList=(ResponseCancelAttention)response;
						System.out.println(responseFansList.result+"!!!!!!!!!!!!");
						if(responseFansList.result.equals("510")){							
							handler.sendEmptyMessage(5);
							handler.sendEmptyMessage(0);//修改为已关注
						}else{		
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

/*	private void DownLoadUserImg() {
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
	}*/
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
