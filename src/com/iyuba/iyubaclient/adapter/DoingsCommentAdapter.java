package com.iyuba.iyubaclient.adapter;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.adapter.DoingsListAdapter.ViewHolder;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.RequestDoingSendComments;
import com.iyuba.iyubaclient.procotol.ResponseDoingSendComments;
import com.iyuba.iyubaclient.sqlite.entity.DoingsCommentInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.util.image.ImageLoder;

public class DoingsCommentAdapter extends BaseAdapter{
	private Context mContext;
	public  ArrayList<DoingsCommentInfo> mList=new ArrayList<DoingsCommentInfo>();
	private ViewHolder viewHolder;
	private String portraitDownload="http://voa.iyuba.com/iyubaApi/v2/api.iyuba?protocol=10005&size=big&uid=";
	int i = 0;
	private String userId;
	DoingsCommentInfo item = new DoingsCommentInfo();
	private ImageLoder imageLoder = new ImageLoder();
	/**
	 * @param mContext
	 * @param mList
	 */
	public DoingsCommentAdapter(Context mContext, ArrayList<DoingsCommentInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		handler.sendEmptyMessage(1);
	}

	/**
	 * @param mContext
	 */
	public DoingsCommentAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	public void addList(ArrayList<DoingsCommentInfo> doingsCommentList){
		mList.addAll(doingsCommentList);
		//DownLoadUserImg();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=vi.inflate(R.layout.doingscomment_item, null);
			viewHolder=new ViewHolder();
			viewHolder.message=(TextView)convertView.findViewById(R.id.doingscomment_message);
			viewHolder.time=(TextView)convertView.findViewById(R.id.doingscomment_time);
			viewHolder.userImageView=(RoundAngleImageView)convertView.findViewById(R.id.doingscomment_userPortrait);
			viewHolder.username=(TextView)convertView.findViewById(R.id.doingscomment_username);
			viewHolder.replycomment=(ImageView)convertView.findViewById(R.id.replycomment);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder=(ViewHolder) convertView.getTag();		
		}
		if(mList.get(position).message.equals("")||mList.get(position).message==null){
			viewHolder.message.setText("");
		}else{
			String zhengze = "image[0-9]{2}|image[0-9]";	
			Emotion emotion=new Emotion();
			mList.get(position).message=emotion.replace(mList.get(position).message);
			try {
				SpannableString spannableString = ExpressionUtil.getExpressionString(mContext, mList.get(position).message, zhengze);
				String a=spannableString.toString();
				Log.e("a====",a);
				viewHolder.message.setText(spannableString);
				//viewHolder.message.setText(Html.fromHtml(replaceExpressPath(a),imageGetter, null));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			//viewHolder.message.setText(Html.fromHtml(replaceExpressPath(mList.get(position).message),imageGetter,null));
		}	
		mList.get(position).userBitmap =imageLoder
				.getBitmap(Constant.IMAGE_DOWN_PATH
						+ mList.get(position).uid);
		viewHolder.username.setText(mList.get(position).username);
     	if (mList.get(position).userBitmap != null) {
     		viewHolder.userImageView.setImageBitmap(mList.get(position ).userBitmap);
		} else {
			viewHolder.userImageView.setImageResource(R.drawable.defaultavatar);
		}
		long time=Long.parseLong(mList.get(position ).dateline)*1000;
		DateFormat.format("yyyy-MM-dd hh:ss:mm", time);		
		viewHolder.time.setText(DateFormat.format("yyyy-MM-dd hh:ss:mm", time));
		viewHolder.replycomment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*item.dateline = String.valueOf(System.currentTimeMillis());
				item.message = "回复 "+mList.get(position).username+":"+"woshidahuaidan";
				item.uid = AccountManager.Instace(mContext).userId;
				item.username = AccountManager.Instace(mContext).userName;
				item.upid = "0";
				item.grade=mList.get(position).grade;
				handler.sendEmptyMessage(10);*/
			}
		});	 
		return convertView;
	}
	class ViewHolder{
		TextView username;
		TextView time;
		TextView message;
		RoundAngleImageView userImageView;
		ImageView replycomment;
	}


	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			source = source.substring(source.lastIndexOf("/") +1);
			source = "/mnt/sdcard/com.iyuba.iyubaclient/comcom/" + source;
			Log.e("source===",source);
			drawable = Drawable.createFromPath(source);
			
			//drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			drawable.setBounds(0, 0, 30,30);
		//	System.out.println("drawable.getIntrinsicWidth()=="+drawable.getIntrinsicWidth()+"   Height==="+drawable.getIntrinsicHeight());
			return drawable;
		}
	};
	private String  replaceExpressPath(String str) {
		// TODO Auto-generated method stub
		str=str.replaceAll("(.*?)src=\"(.*?)", "$1src=\"http://iyuba.com/$2");
		Log.e("Str====", str);
		return str;
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:notifyDataSetChanged();
				break;
			case 1:
				// DownLoadUserImg() ;
				break;
			case 10:
				//发送回复
			
				ClientNetwork.Instace().asynGetResponse(
						new RequestDoingSendComments(item, DataManager.Instace().doingsInfo.doid),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseDoingSendComments res = (ResponseDoingSendComments) response;
								if (res.result.equals("361")) {
									System.out.println("评论发送成功！！！！");
								} else {
								}
								handler.sendEmptyMessage(4);
							}

						});
				break;
			}
		}};
		private void DownLoadUserImg() {
			// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					System.out.println("mList.size()==="+mList.size());
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
												+ mList.get(i).uid + ".jpeg", options);
								
							} else {
								DownLoadManager.Instace(mContext).downLoadImage(
										portraitDownload +userId,userId,
										new DownLoadCallBack() {
											@Override
											public void downLoadSuccess(Bitmap image) {
												// TODO Auto-generated method stub
												if(i==mList.size()){
													mList.get(mList.size()-1).userBitmap = image;
												}else if(i<mList.size()){
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
