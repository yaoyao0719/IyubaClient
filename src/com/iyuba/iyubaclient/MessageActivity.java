package com.iyuba.iyubaclient;

import java.util.ArrayList;
import java.util.List;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.adapter.MessageLetterListAdapter;
import com.iyuba.iyubaclient.adapter.NotificationListAdapter;
import com.iyuba.iyubaclient.adapter.ReplyListAdapter;
import com.iyuba.iyubaclient.feed.procotol.RequestFindBlogById;
import com.iyuba.iyubaclient.feed.procotol.ResponseFindBlogById;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.message.procotol.RequestMessageLetterList;
import com.iyuba.iyubaclient.message.procotol.RequestNotificationInfo;
import com.iyuba.iyubaclient.message.procotol.RequestSetMessageLetterRead;
import com.iyuba.iyubaclient.message.procotol.ResponseNotificationInfo;
import com.iyuba.iyubaclient.message.procotol.ResponseSetMessageLetterRead;
import com.iyuba.iyubaclient.message.procotol.ResponseMessageLetterList;
import com.iyuba.iyubaclient.procotol.RequestBlogInfo;
import com.iyuba.iyubaclient.procotol.ResponseBlogInfo;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.NotificationInfo;
import com.iyuba.iyubaclient.sqlite.entity.ReplyInfo;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.iyuba.iyubaclient.widget.listview.DynamicListView;
import com.iyuba.iyubaclient.widget.listview.DynamicListView.DynamicListViewListener;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

import android.R.integer;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MessageActivity extends BaseActivity implements OnScrollListener{

	
	private Button message_letter,message_notification,message_reply;
	private Button message_letter_press,message_notification_press,message_reply_press;
	private Context mContext;
	private View letterView,notificationView,replyView;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPageAdapter;
	private List<View> mListViews;
	private LayoutInflater mInflater;
	private CustomDialog waitingDialog;
	private Boolean isLetterLastPage=true;
	private Boolean isReplyLastPage=true;
	private Boolean isNotificationLastPage=true;

	private int whichView=0;
	private Boolean isRerefreshBoolean=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagelist);
		mContext=this;
		initWidget();
		waitingDialog=waitingDialog();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		message_letter=(Button)findViewById(R.id.message_letter);
		message_notification=(Button)findViewById(R.id.message_notification);
		message_reply=(Button)findViewById(R.id.message_reply);
		message_letter_press=(Button)findViewById(R.id.message_letter_press);
		message_notification_press=(Button)findViewById(R.id.message_notification_press);
		message_reply_press=(Button)findViewById(R.id.message_reply_press);
		message_letter.setVisibility(View.GONE);
		message_letter_press.setVisibility(View.VISIBLE);
		message_notification.setVisibility(View.VISIBLE);
		message_notification_press.setVisibility(View.GONE);
		message_reply.setVisibility(View.VISIBLE);
		message_reply_press.setVisibility(View.GONE);
		viewPageAdapter=new ViewPagerAdapter();
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0){//私信
					letterView.setVisibility(View.VISIBLE);			
					message_letter.setVisibility(View.GONE);
					message_letter_press.setVisibility(View.VISIBLE);
					message_notification.setVisibility(View.VISIBLE);
					message_notification_press.setVisibility(View.GONE);
					message_reply.setVisibility(View.VISIBLE);
					message_reply_press.setVisibility(View.GONE);
				}else if(arg0==1){//消息（用户留言、评论）
					replyView.setVisibility(View.VISIBLE);
					message_letter.setVisibility(View.VISIBLE);
					message_letter_press.setVisibility(View.GONE);
					message_notification.setVisibility(View.VISIBLE);
					message_notification_press.setVisibility(View.GONE);
					message_reply.setVisibility(View.GONE);
					message_reply_press.setVisibility(View.VISIBLE);
				}else if(arg0==2){//通知
					notificationView.setVisibility(View.VISIBLE);
					message_letter.setVisibility(View.VISIBLE);
					message_letter_press.setVisibility(View.GONE);	
					message_reply.setVisibility(View.VISIBLE);
					message_reply_press.setVisibility(View.GONE);
					message_notification.setVisibility(View.GONE);
					message_notification_press.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		viewPager.setAdapter(viewPageAdapter);
		mListViews=new ArrayList<View>();
		mInflater=getLayoutInflater();
		letterView=mInflater.inflate(R.layout.message_letter, null);
		replyView=mInflater.inflate(R.layout.message_reply, null);
		notificationView=mInflater.inflate(R.layout.message_notification, null);
		mListViews.add(letterView);
		mListViews.add(replyView);
		mListViews.add(notificationView);
		initLetterView();
		initReplyView();
		initNotificationView();
		initTab();
	}
    //私信部分
	private PullToRefreshListView letterListView;
	private ArrayList<MessageLetter> letterList=new ArrayList<MessageLetter>();
	private MessageLetterListAdapter adapter;
	private String currPages="1";
	private int curPage=1;
	//通知部分
	private PullToRefreshListView notificationListView;
	private ArrayList<NotificationInfo> notificationList=new ArrayList<NotificationInfo>();
	private NotificationListAdapter adapterNoti;
	//回复部分
	private PullToRefreshListView replyListView;
	private ReplyListAdapter adapterReply;
	private ArrayList<NotificationInfo> replyList=new ArrayList<NotificationInfo>();
	private void initLetterView() {
		// TODO Auto-generated method stub
		letterListView=(PullToRefreshListView)letterView.findViewById(R.id.letterlist);
		adapter=new MessageLetterListAdapter(mContext);
		whichView=2;
		letterListView.setAdapter(adapter);	
		handler.sendEmptyMessage(0);	
		letterListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				adapter.clearList();
				isRerefreshBoolean=true;
			}
		});
		letterListView.setOnScrollListener(this);
	}
	private void initReplyView() {
		// TODO Auto-generated method stub
		whichView=1;
		replyListView=(PullToRefreshListView)replyView.findViewById(R.id.replylist);
		adapterReply=new ReplyListAdapter(mContext);
		replyListView.setAdapter(adapterReply);
		handler_reply.sendEmptyMessage(0);
		replyListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler_reply.sendEmptyMessage(0);
				adapterReply.clearList();
				isRerefreshBoolean=true;
			}
		});
		replyListView.setOnScrollListener(this);

	}

	private void initNotificationView() {
		// TODO Auto-generated method stub
		whichView=3;
		notificationListView=(PullToRefreshListView)notificationView.findViewById(R.id.notificationlist);
		adapterNoti=new NotificationListAdapter(mContext);
		notificationListView.setAdapter(adapterNoti);
		handler_notification.sendEmptyMessage(0);
		notificationListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler_notification.sendEmptyMessage(0);
				adapterNoti.clearList();
				isRerefreshBoolean=true;
			}
		});
		
		notificationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				arg0.findViewById(R.id.test);
				
			}
		});
		notificationListView.setOnScrollListener(this);
	}

	Handler handler_reply=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage=1;
				currPages=String.valueOf(curPage);
				//adapterReply.clearList();
				replyList.clear();
				handler_reply.sendEmptyMessage(1);
				handler_reply.sendEmptyMessage(2);
				break;

			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(new RequestNotificationInfo(AccountManager.Instace(mContext).userId,currPages,"doing,blogcomment,comment,uid,pic") , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseNotificationInfo res=(ResponseNotificationInfo)response;
						if(res.result.equals("631")){
							replyList.clear();
							replyList.addAll(res.list);						
							adapterReply.addList(res.list);
							if(res.lastPage==res.nextPage){
								isReplyLastPage=true;
							}else{
								isReplyLastPage=false;
							}		
						}else{						
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler_reply.sendEmptyMessage(4);						
					}				
				});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler_reply.sendEmptyMessage(3);
				adapterReply.notifyDataSetChanged();
				if(isRerefreshBoolean){
					replyListView.onRefreshComplete();
				}	
				setListener();
				break;
	
			case 7:
				handler_reply.sendEmptyMessage(3);
				//跳到这个日志
				ClientNetwork.Instace().asynGetResponse(new RequestFindBlogById(DataManager.Instace().replyInfo.from_id) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseFindBlogById res=(ResponseFindBlogById)response;
						if(res.result.equals("251")){					
							DataManager.Instace().blogContent=res.blogContent;	
							Intent intent=new Intent();
							intent.setClass(mContext,BlogActivity.class);
							startActivity(intent);
						}else if(res.result.equals("250")){
							handler.sendEmptyMessage(13);
						}
						handler.sendEmptyMessage(3);
						
					}
					
				});		
				break;
			case 8:
				//跳到这个状态
				handler.sendEmptyMessage(3);
				Intent intent1=new Intent();
				intent1.putExtra("doid",DataManager.Instace().replyInfo.from_id);
				intent1.setClass(mContext,DoingFromHomeActivity.class);
				startActivity(intent1);
				break;
			case 10:
				//设置是否已读
				ClientNetwork.Instace().asynGetResponse(new RequestSetMessageLetterRead(AccountManager.Instace(mContext).userId,DataManager.Instace().letter.plid) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSetMessageLetterRead res=(ResponseSetMessageLetterRead)response;
						if(res.result.equals("621")){
							System.out.println("设置成功");
						}else{		
							System.out.println("设置失败");
						}
					
					}	
				});
				break;
			default:
				break;
			}
		}
		
	};
	Handler handler_notification=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage=1;
				currPages=String.valueOf(curPage);
				//adapterNoti.clearList();
				notificationList.clear();
				handler_notification.sendEmptyMessage(1);
				handler_notification.sendEmptyMessage(2);
				break;
			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(new RequestNotificationInfo("21664",currPages,"system,blog,comment,invite,app") , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseNotificationInfo res=(ResponseNotificationInfo)response;
						if(res.result.equals("631")){
							notificationList.clear();
							notificationList.addAll(res.list);						
							adapterNoti.addList(res.list);
							if(res.lastPage==res.nextPage){
								isNotificationLastPage=true;
							}else{
								isNotificationLastPage=false;
							}		
						}else{						
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler_notification.sendEmptyMessage(4);						
					}				
				});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler_notification.sendEmptyMessage(3);
				adapterNoti.notifyDataSetChanged();
				if(isRerefreshBoolean){
					notificationListView.onRefreshComplete();
				}	
				setListener();
				break;
	
			case 7:
				handler_notification.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.setClass(mContext,MessageLetterContentActivity.class);
				startActivity(intent);				
				break;
			default:
				break;
			}
		}
		
	};
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage=1;
				currPages=String.valueOf(curPage);
				//adapter.clearList();
				letterList.clear();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;

			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(new RequestMessageLetterList(AccountManager.Instace(mContext).userId) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseMessageLetterList res=(ResponseMessageLetterList)response;
						if(res.result.equals("601")){
							letterList.clear();
							letterList.addAll(res.list);
							System.out.println("res.list=== "+res.list.get(res.list.size()-1).lastmessage);
							adapter.addList(res.list);	
							if(res.lastPage==res.nextPage){
								isLetterLastPage=true;
							}else{
								isLetterLastPage=false;
							}		
						}else{						
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(4);
						
					}
					
				});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler.sendEmptyMessage(3);
				adapter.notifyDataSetChanged();
				if(isRerefreshBoolean){
					letterListView.onRefreshComplete();
				}
				setListener();
				break;
	
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.setClass(mContext,MessageLetterContentActivity.class);
				startActivity(intent);				
				break;
			case 10:
				//设置是否已读
				ClientNetwork.Instace().asynGetResponse(new RequestSetMessageLetterRead(AccountManager.Instace(mContext).userId,DataManager.Instace().letter.plid) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSetMessageLetterRead res=(ResponseSetMessageLetterRead)response;
						if(res.result.equals("621")){
							System.out.println("设置成功");
						}else{		
							System.out.println("设置失败");
						}
					
					}	
				});
				break;
			default:
				break;
			}
		}
		
	};
	private void setListener(){
		letterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub		
				if(position==1){					
					Intent intent=new Intent();
					intent.setClass(mContext, FindFriendListActivity.class);
					startActivity(intent);
				}else{
					DataManager.Instace().letterlist=letterList;
					DataManager.Instace().letter=letterList.get(position-2);
					handler.sendEmptyMessage(7);//进入私信聊天界面
					handler.sendEmptyMessage(10);
				}
				
			}
		});
		replyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position==0){					
				}else{
					DataManager.Instace().nofiList=replyList;
					DataManager.Instace().replyInfo=replyList.get(position-1);
					if(DataManager.Instace().replyInfo.from_idtype.equals("blog")){
						handler_reply.sendEmptyMessage(7);//进入该日志
					}else if(DataManager.Instace().replyInfo.from_idtype.equals("doing")){
						handler_reply.sendEmptyMessage(8);//进入该状态
					}
					
				
				}
			}
		});
		
	}
	private void initTab() {
		// TODO Auto-generated method stub
		message_letter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(0);
			}
		});
		message_reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(1);
			}
		});
		message_notification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(2);
			}
		});
	}


	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {

			((ViewPager) collection).addView(mListViews.get(position), 0);

			return mListViews.get(position);
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView(mListViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}
		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

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
				//listView.addFooterView(footer);
				if(whichView==1){
					if(!isLetterLastPage){
						handler.sendEmptyMessage(1);	
					}
				}else if(whichView==2){
					if(!isReplyLastPage){
						handler_reply.sendEmptyMessage(1);	
					}
				}else if(whichView==3){
					if(!isNotificationLastPage){
						handler_notification.sendEmptyMessage(1);	
					}
				}
				isRerefreshBoolean=false;
			System.out.println("滑动到底部");
			break;

		}
	}
	}


}
