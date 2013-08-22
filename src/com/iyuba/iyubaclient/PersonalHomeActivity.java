/**
 * 
 */
package com.iyuba.iyubaclient;

import java.io.File;
import java.util.ArrayList;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.adapter.DoingsListAdapter;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestAddAttention;
import com.iyuba.iyubaclient.procotol.RequestBasicUserInfo;
import com.iyuba.iyubaclient.procotol.RequestCancelAttention;
import com.iyuba.iyubaclient.procotol.RequestDoingsInfo;
import com.iyuba.iyubaclient.procotol.ResponseAddAttention;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseCancelAttention;
import com.iyuba.iyubaclient.procotol.ResponseDoingsInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.util.CheckGrade;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.util.image.ImageLoder;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author yao
 *
 */
public class PersonalHomeActivity extends THSActivity implements OnScrollListener{
	private Button personalhome_back_btn;
	private TextView tvEditInfo,tvName,tvStateInfo,tvStateNum,tvFansNum,grade,tvDistance;
	private ImageView ivGender,ivLeaguer,ivPortraitMask,ivPortrait,level;//gender是性別图标，League是等级图标,ivPortraitMask是头像
	private FrameLayout btn_details_info;
	private TextView btn_follow_num,btn_journal_num;
	private String userImgSrc;
	private String stateNum,fansNum,journalNum;
	private String userId,userName,userGender,stateInfo;
	private Bitmap bm;
	private LinearLayout distanceLayout;
	private ArrayList<DoingsInfo> doingsArrayList =new ArrayList<DoingsInfo>();
	private CustomDialog waitingDialog;
	private ListView doingsList;
	private DoingsListAdapter doingsListAdapter;
	private String currPages="1";
	private int curPage=1;
	private String currentuid,currentusername;
	private FrameLayout lyEditInfo,lyAddFan,lyFollowNum,lyFansNum,lyJournalNum,lyCancelFan,lyMutualFan;
	private View topView,nodata;
	private LayoutInflater layoutInflater;
	private Boolean isLastPage=false;
	private String relation,distance;
	private Button wordButton;
	private CheckGrade checkGrade=new CheckGrade();
	private ImageLoder imageLoder = new ImageLoder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalhome);	
		waitingDialog=waitingDialog();
		Intent intent =getIntent();
		currentuid=intent.getStringExtra("fanuid");	
		initWidget();		
		DownLoadUserImg();
		handler.sendEmptyMessage(20);//访问用户基本信息
		handler.sendEmptyMessage(0);
		setAdapter();
		setListener();
		if(currentuid.equals(AccountManager.Instace(mContext).userId)){
			lyEditInfo.setVisibility(View.VISIBLE);
			handler.sendEmptyMessage(100);
		}else{
			lyEditInfo.setVisibility(View.GONE);
			lyAddFan.setVisibility(View.VISIBLE);
			handler.sendEmptyMessage(100);
		}
	
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		personalhome_back_btn=(Button)findViewById(R.id.personalhome_back_btn);
		//头部布局
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		topView=layoutInflater.inflate(R.layout.user_info_header, null);
		tvEditInfo=(TextView)topView.findViewById(R.id.tvEditInfo);
		tvName=(TextView)topView.findViewById(R.id.tvName);
		tvStateInfo=(TextView)topView.findViewById(R.id.tvStateInfo);
		ivGender=(ImageView)topView.findViewById(R.id.ivGender);
		ivLeaguer=(ImageView)topView.findViewById(R.id.ivLeaguer);
		ivPortraitMask=(ImageView)topView.findViewById(R.id.ivPortraitMask);
		ivPortrait=(RoundAngleImageView)topView.findViewById(R.id.ivPortrait);
		grade=(TextView)topView.findViewById(R.id.grade);
		level=(ImageView)topView.findViewById(R.id.personalhome_level);
		//HorizontalScrollView
		btn_details_info=(FrameLayout)topView.findViewById(R.id.fldetails_info) ;
		btn_follow_num=(TextView)topView.findViewById(R.id.btn_follow_num);
		tvFansNum=(TextView)topView.findViewById(R.id.btn_fans_num);
		btn_journal_num=(TextView)topView.findViewById(R.id.btn_journal_num);	
		tvDistance=(TextView)topView.findViewById(R.id.distance);
		distanceLayout=(LinearLayout)topView.findViewById(R.id.distance_layout);
		doingsList=(ListView)findViewById(R.id.personalhome_doingslist);
		lyEditInfo=(FrameLayout)topView.findViewById(R.id.lyEditInfo);
		lyAddFan=(FrameLayout)topView.findViewById(R.id.lyAddFan);
		lyJournalNum=(FrameLayout)topView.findViewById(R.id.lyJournalNum);
		lyFansNum=(FrameLayout)topView.findViewById(R.id.lyFansNum);
		lyFollowNum=(FrameLayout)topView.findViewById(R.id.lyFollowNum);
		lyCancelFan=(FrameLayout)topView.findViewById(R.id.lyCancelFan);
		lyMutualFan=(FrameLayout)topView.findViewById(R.id.lyMutualFan);
		
		doingsListAdapter=new DoingsListAdapter(mContext);
		wordButton=(Button)findViewById(R.id.word);
		wordButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				intent.setClass(mContext, WordListActivity.class);
				startActivity(intent);
			}
		});
		nodata=(LinearLayout)topView.findViewById(R.id.personalhome_nodata);
	}
	private void setAdapter(){
		
		doingsList.addHeaderView(topView);
		doingsList.setAdapter(doingsListAdapter);
		doingsList.setOnScrollListener(this);
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage=1;
				currPages=String.valueOf(curPage);
				doingsArrayList.clear();
				doingsListAdapter.clearList();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				ClientNetwork.Instace().asynGetResponse(new RequestDoingsInfo(currentuid, currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseDoingsInfo responseDoingsInfo=(ResponseDoingsInfo)response;
						if(responseDoingsInfo.result.equals("301")){
							stateNum=responseDoingsInfo.counts;													
							if(responseDoingsInfo.counts.equals("0")){
								nodata.setVisibility(View.VISIBLE);						
							}else{
								doingsArrayList.addAll(responseDoingsInfo.doingslist);
								doingsListAdapter.addList(responseDoingsInfo.doingslist);
							}
						}else{
							
						}
						
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(10);
						handler.sendEmptyMessage(4);
					}		
				});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				handler.sendEmptyMessage(100);
				waitingDialog.dismiss();
				break;
			case 4:				
				handler.sendEmptyMessage(3);						
				break;
			case 10:
				//setAdapter();
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.putExtra("imgSrc", userImgSrc);
				intent.setClass(mContext,DoingsActivity.class);
				startActivity(intent);
				break;
			case 20:
				ClientNetwork.Instace().asynGetResponse(
						new RequestBasicUserInfo(currentuid,AccountManager.Instace(mContext).userId), new IResponseReceiver() {
							
							@Override
							public void onResponse(BaseHttpRequest request, BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseBasicUserInfo responseBasicUserInfo=(ResponseBasicUserInfo)response;
								if(responseBasicUserInfo.result.equals("201")){
									userName=responseBasicUserInfo.username;
									stateNum=responseBasicUserInfo.doings;//状态数
									fansNum=responseBasicUserInfo.friends;
									journalNum=responseBasicUserInfo.blogs;	
									userGender=responseBasicUserInfo.gender;
									stateInfo=responseBasicUserInfo.text;	
									UserBasicInfoManager.Instace().responseBasicUserInfo=responseBasicUserInfo;		
									relation=responseBasicUserInfo.relation;
									distance=responseBasicUserInfo.distance;
									handler.sendEmptyMessage(50);	
								}
								handler.sendEmptyMessage(21);								
							}							
						});
				break;
			case 21:
				//更新UI
				tvName.setText(userName);
				handler.sendEmptyMessage(100);
				btn_follow_num.setText(stateNum);//关注数暂未获取到
				handler.sendEmptyMessage(100);
				tvFansNum.setText(fansNum);
				grade.setText(UserBasicInfoManager.Instace().responseBasicUserInfo.icoins);
				checkGrade.Check(UserBasicInfoManager.Instace().responseBasicUserInfo.icoins);
				LevelSetImage(checkGrade.Check(UserBasicInfoManager.Instace().responseBasicUserInfo.icoins));			
				handler.sendEmptyMessage(100);
				btn_journal_num.setText(journalNum);
				handler.sendEmptyMessage(100);
				if(distance.equals("")||distance==null){
					distanceLayout.setVisibility(View.GONE);
				}else{
					distanceLayout.setVisibility(View.VISIBLE);
					tvDistance.setText(distance);
				}
			    if(stateInfo!=null){
			    	String zhengze = "image[0-9]{2}|image[0-9]";	
					Emotion emotion=new Emotion();
					stateInfo=emotion.replace(stateInfo);
					try {
						SpannableString spannableString = ExpressionUtil.getExpressionString(mContext, stateInfo, zhengze);
						tvStateInfo.setText(stateInfo);
						handler.sendEmptyMessage(100);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
			    	
			    }else{
			    	tvStateInfo.setText("");
			    	handler.sendEmptyMessage(100);
			    }
			    if(userGender.equals("2")){
			    	ivGender.setImageResource(R.drawable.user_info_female);
			    }else if(userGender.equals("1")){
			    	ivGender.setImageResource(R.drawable.user_info_male);
			    }else{
			    	ivGender.setVisibility(View.GONE);
			    }
			    handler.sendEmptyMessage(100);
				handler.sendEmptyMessage(3);
				break;
			case 22:
				//更新UI
				Log.e("bm===",""+bm);
				if(bm==null){
					ivPortrait.setBackgroundResource(R.drawable.defaultavatar);
				}else{
					ivPortrait.setImageBitmap(bm);		
				}
				handler.sendEmptyMessage(100);
				break;
			case 30:
				//加关注
				ClientNetwork.Instace().asynGetResponse(new RequestAddAttention(AccountManager.Instace(mContext).userId,currentuid ) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseAddAttention res=(ResponseAddAttention)response;
						System.out.println(res.result+"!!!!!!!!!!!!");
						if(res.result.equals("500")){							
							handler.sendEmptyMessage(5);
							handler.sendEmptyMessage(0);//修改为已关注
						}else{		
							handler.sendEmptyMessage(6);
						}
						handler.sendEmptyMessage(31);
					}
					
				});
				break;
			case 31:
				//关注成功后更新UI
				lyAddFan.setVisibility(View.GONE);
				lyMutualFan.setVisibility(View.VISIBLE);
				handler.sendEmptyMessage(100);
				break;
			case 40:
				//取消关注
				ClientNetwork.Instace().asynGetResponse(new RequestCancelAttention(AccountManager.Instace(mContext).userId, currentuid) , new IResponseReceiver(){

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
						handler.sendEmptyMessage(41);
					}
					
				});
				break;
			case 41:
				//取消关注成功后更新UI
				lyAddFan.setVisibility(View.VISIBLE);
				lyMutualFan.setVisibility(View.GONE);
				handler.sendEmptyMessage(100);
				break;
			case 5:
				Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
				break;
			case 50:
				if(currentuid.equals(AccountManager.Instace(mContext).userId)){
					
				}else if(!relation.equals("0")&&!relation.equals("1")){
					if(relation.charAt(2)=='1'&&relation.charAt(0)=='1'){
						//相互关注
						lyEditInfo.setVisibility(View.GONE);
						lyMutualFan.setVisibility(View.VISIBLE);
						handler.sendEmptyMessage(100);
					}else if(relation.charAt(0)=='1'&&relation.charAt(2)=='0'){
						//我关注了这个人
						lyEditInfo.setVisibility(View.GONE);
						lyCancelFan.setVisibility(View.VISIBLE);
						handler.sendEmptyMessage(100);
					}else if(relation.charAt(0)=='0'){
						//我没关注了这个人
						lyEditInfo.setVisibility(View.GONE);
						lyAddFan.setVisibility(View.VISIBLE);
						handler.sendEmptyMessage(100);
					}
				}else if(relation.equals("1")){
					lyAddFan.setVisibility(View.GONE);
					lyCancelFan.setVisibility(View.VISIBLE);
					handler.sendEmptyMessage(100);
				}else {
					lyEditInfo.setVisibility(View.GONE);
					lyAddFan.setVisibility(View.VISIBLE);
					handler.sendEmptyMessage(100);
				}
				
				
				
				break;
			case 100:
				doingsListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

		
	};
	private void LevelSetImage(int i) {
		// TODO Auto-generated method stub
		if(i==1){
			level.setImageResource(R.drawable.level1);
		}else if(i==2){
			level.setImageResource(R.drawable.level2);
		}else if(i==3){
			level.setImageResource(R.drawable.level3);
		}else if(i==4){
			level.setImageResource(R.drawable.level4);
		}else if(i==5){
			level.setImageResource(R.drawable.level5);
		}else if(i==6){
			level.setImageResource(R.drawable.level6);
		}else if(i==7){
			level.setImageResource(R.drawable.level7);
		}else if(i==8){
			level.setImageResource(R.drawable.level8);
		}else if(i==9){
			level.setImageResource(R.drawable.level9);
		}else if(i==10){
			level.setImageResource(R.drawable.level10);
		}
		
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		personalhome_back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_details_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub			
				Intent intent=new Intent();
				intent.putExtra("currentuid", currentuid);
				intent.setClass(mContext, UserDetailInfoActivity.class);
				startActivity(intent);
			}
		});
		tvEditInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("imgSrc", userImgSrc);
				intent.setClass(mContext, EditUserInfoActivity.class);
				startActivity(intent);
			}
		});
		lyEditInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(mContext, EditUserInfoActivity.class);
				startActivity(intent);
			}
		});
		lyJournalNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("currentuid", currentuid);
				intent.putExtra("currentusername", currentusername);
				intent.setClass(mContext, BlogListActivity.class);
				startActivity(intent);
			}
		});
		lyFansNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("currentuid", currentuid);
				System.out.println("传入"+currentuid+"当前用户ID");
				intent.setClass(mContext, FansListActivity.class);
				startActivity(intent);
			}
		});
		lyFollowNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("currentuid", currentuid);
				System.out.println("传入"+currentuid+"当前用户ID");
				intent.setClass(mContext, AttentionListActivity.class);
				startActivity(intent);
			}
		});
		doingsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				if(position==0){
					
				}else{
					DataManager.Instace().doingsInfo=doingsArrayList.get(position-1);
					final String message=doingsArrayList.get(position-1).message;
					handler.sendEmptyMessage(7);
				}
				
			}
		});
		lyAddFan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("---------加关注--------");
				handler.sendEmptyMessage(30);//加关注
			}
		});
		lyCancelFan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(40);//取消关注
			}
		});
	}

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {			
					bm= imageLoder.getBitmap(Constant.IMAGE_DOWN_PATH+ currentuid);		
					handler.sendEmptyMessage(22);
				}
			}
		};
		t.start();
	}
	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder
				.setContentView(layout).create();
		return cDialog;
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			if(view.getLastVisiblePosition()==(view.getCount() - 1)){
				if(!isLastPage){
					handler.sendEmptyMessage(1);
					System.out.println("滑动到底部！！！！！");
				}
				
			}
			break;

		}
	}

	
}
