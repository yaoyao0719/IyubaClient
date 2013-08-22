package com.iyuba.iyubaclient;

import java.util.ArrayList;
import java.util.List;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iyuba.iyubaclient.adapter.GuessFriendsListAdapter;
import com.iyuba.iyubaclient.adapter.NearFriendsListAdapter;
import com.iyuba.iyubaclient.adapter.PublicAccountsListAdapter;
import com.iyuba.iyubaclient.adapter.SameAppFriendListAdapter;
import com.iyuba.iyubaclient.findfriends.procotol.RequestGuessFriendsList;
import com.iyuba.iyubaclient.findfriends.procotol.RequestPublicAccountsList;
import com.iyuba.iyubaclient.findfriends.procotol.RequestSameAppFriendsList;
import com.iyuba.iyubaclient.findfriends.procotol.ResponseGuessFriendsList;
import com.iyuba.iyubaclient.findfriends.procotol.ResponsePublicAccountsList;
import com.iyuba.iyubaclient.findfriends.procotol.ResponseSameAppFriendsList;
import com.iyuba.iyubaclient.location.procotol.RequestNearFriendsList;
import com.iyuba.iyubaclient.location.procotol.ResponseNearFriendsList;
import com.iyuba.iyubaclient.location.procotol.RequestSendLocation;
import com.iyuba.iyubaclient.location.procotol.ResponseSendLocation;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.sqlite.entity.GuessFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.NearFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.PublicAccountInfo;
import com.iyuba.iyubaclient.sqlite.entity.SameAppFriendInfo;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
//找朋友

public class FindFriendsActivity extends THSActivity implements OnScrollListener{

	private Button near, guess, sameapp, back,accounts;
	private Button near_press, guess_press, sameapp_press,accounts_press;
	private View nearFriendList, guessFriendList, sameAppFriendList,publicAccoutsList;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPageAdapter;
	private List<View> mListViews;
	private LayoutInflater mInflater;
	private CustomDialog waitingDialog;
	private String x, y;// 经纬度
	private TextView noData;
	public LocationClient mLocClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public Boolean isAccountsLastPage=false;
	public Boolean isSameAppLastPage=false;
	private int whichView=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findfriends);
		mLocClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocClient.registerLocationListener( myListener );    //注册监听函数
		mLocClient.start();
		waitingDialog = waitingDialog();
		initWidget();
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		back = (Button) findViewById(R.id.findfriend_back_btn);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		near = (Button) findViewById(R.id.findfriend_near);
		near_press = (Button) findViewById(R.id.findfriend_near_press);
		guess = (Button) findViewById(R.id.findfriend_guess);
		guess_press = (Button) findViewById(R.id.findfriend_guess_press);
		sameapp = (Button) findViewById(R.id.findfriend_sameapp);
		sameapp_press = (Button) findViewById(R.id.findfriend_sameapp_press);
		accounts=(Button)findViewById(R.id.findfriend_publicaccounts);
		accounts_press=(Button)findViewById(R.id.findfriend_publicaccounts_press);
		// 刚进入这一页，就返回“猜你认识”的人
		guess.setVisibility(View.GONE);
		guess_press.setVisibility(View.VISIBLE);
		near.setVisibility(View.VISIBLE);
		near_press.setVisibility(View.GONE);
		sameapp.setVisibility(View.VISIBLE);
		sameapp_press.setVisibility(View.GONE);
		accounts.setVisibility(View.VISIBLE);
		accounts_press.setVisibility(View.GONE);
		viewPageAdapter = new ViewPagerAdapter();
		viewPager = (ViewPager) findViewById(R.id.findfriend_viewpager);
		viewPager.setAdapter(viewPageAdapter);
		mListViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		nearFriendList = mInflater.inflate(R.layout.nearfriendlist, null);
		guessFriendList = mInflater.inflate(R.layout.guessfriendlist, null);
		sameAppFriendList = mInflater.inflate(R.layout.sameapplist, null);
		publicAccoutsList=mInflater.inflate(R.layout.pubicaccountslist, null);
		mListViews.add(guessFriendList);
		mListViews.add(nearFriendList);
		mListViews.add(sameAppFriendList);
		mListViews.add(publicAccoutsList);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 0) {// 猜你认识
					guessFriendList.setVisibility(View.VISIBLE);
					guess.setVisibility(View.GONE);
					guess_press.setVisibility(View.VISIBLE);
					near.setVisibility(View.VISIBLE);
					near_press.setVisibility(View.GONE);
					sameapp.setVisibility(View.VISIBLE);
					sameapp_press.setVisibility(View.GONE);
					accounts.setVisibility(View.VISIBLE);
					accounts_press.setVisibility(View.GONE);
				} else if (arg0 == 1) {// 周边的人
					nearFriendList.setVisibility(View.VISIBLE);
					guess.setVisibility(View.VISIBLE);
					guess_press.setVisibility(View.GONE);
					near_press.setVisibility(View.VISIBLE);
					near.setVisibility(View.GONE);
					sameapp_press.setVisibility(View.GONE);
					sameapp.setVisibility(View.VISIBLE);
					accounts.setVisibility(View.VISIBLE);
					accounts_press.setVisibility(View.GONE);
					initNearView();
				} else if (arg0 == 2) {// 同一应用的人
					sameAppFriendList.setVisibility(View.VISIBLE);
					guess.setVisibility(View.VISIBLE);
					guess_press.setVisibility(View.GONE);
					sameapp_press.setVisibility(View.VISIBLE);
					sameapp.setVisibility(View.GONE);
					near_press.setVisibility(View.GONE);
					near.setVisibility(View.VISIBLE);
					accounts.setVisibility(View.VISIBLE);
					accounts_press.setVisibility(View.GONE);
					initSameAppView();
				}else if(arg0 == 3) {// 公共账号
					publicAccoutsList.setVisibility(View.VISIBLE);
					guess.setVisibility(View.VISIBLE);
					guess_press.setVisibility(View.GONE);
					sameapp_press.setVisibility(View.GONE);
					sameapp.setVisibility(View.VISIBLE);
					near_press.setVisibility(View.GONE);
					near.setVisibility(View.VISIBLE);
					accounts.setVisibility(View.GONE);
					accounts_press.setVisibility(View.VISIBLE);
					initPublicAccountsList();
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
		initGuessView();
		initTab();
	}
	private ArrayList<GuessFriendInfo> guessList = new ArrayList<GuessFriendInfo>();
	private PullToRefreshListView guessListView;
	private GuessFriendsListAdapter adapterGuess;
	private TextView guessNoData;
	private void initGuessView() {
		// TODO Auto-generated method stub
		guessListView = (PullToRefreshListView) guessFriendList.findViewById(R.id.guesslist);
		guessNoData = (TextView) guessFriendList.findViewById(R.id.guess_nodata);
		whichView=1;
		adapterGuess = new GuessFriendsListAdapter(mContext);
		guessListView.setAdapter(adapterGuess);
		guessListView.setOnScrollListener(this);
		guessListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				adapterGuess.clearList();
				handler_guess.sendEmptyMessage(0);
			}
		});
		handler_guess.sendEmptyMessage(0);
	}
	Handler handler_guess = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				
				handler_guess.sendEmptyMessage(2);
				handler_guess.sendEmptyMessage(1);
				break;

			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(
						new RequestGuessFriendsList(
								AccountManager.Instace(mContext).userId), new IResponseReceiver() {
							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseGuessFriendsList res = (ResponseGuessFriendsList) response;
								if (res.result.equals("591")) {
									guessList.clear();
									guessList.addAll(res.list);
									adapterGuess.addList(res.list);	
									if(res.list.size()==0){
										guessNoData.setVisibility(View.VISIBLE);
										guessListView.setVisibility(View.GONE);
									}
								} 
								handler_guess.sendEmptyMessage(4);

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
				handler_guess.sendEmptyMessage(3);
				adapterGuess.notifyDataSetChanged();
				guessListView.onRefreshComplete();
				setGuessListener();
				break;

			case 7:
				handler_guess.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("fanuid",
						DataManager.Instace().guessFriendInfo.uid);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}

	};
	private void setGuessListener(){
		guessListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				DataManager.Instace().guessFriendInfo = guessList.get(position-1);
				handler_guess.sendEmptyMessage(7);// 进入个人空间

			}
		});
	}
	private ArrayList<NearFriendInfo> nearList = new ArrayList<NearFriendInfo>();
	private PullToRefreshListView nearListView;
	private NearFriendsListAdapter adapterNear;
	private String currPages = "1";
	private int curPage = 1;
	private Boolean isNearLastPage = false;

	private void initNearView() {
		// TODO Auto-generated method stub
		setOption();
		if (mLocClient != null && mLocClient.isStarted()){
			System.out.println(" mLocClient不为空");
			mLocClient.requestLocation();
			mLocClient.requestPoi();
		}	
		if(myListener!=null){
			nearListView = (PullToRefreshListView) nearFriendList.findViewById(R.id.nearlist);
			noData = (TextView) nearFriendList.findViewById(R.id.nodata);
			whichView=2;
			adapterNear = new NearFriendsListAdapter(mContext);
			nearListView.setAdapter(adapterNear);
			nearListView.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					adapterNear.clearList();
					handler.sendEmptyMessage(0);
				}
			});
			handler.sendEmptyMessage(0);
		}
		
	}
	private ArrayList<SameAppFriendInfo> sameAppList = new ArrayList<SameAppFriendInfo>();
	private PullToRefreshListView sameAppListView;
	private SameAppFriendListAdapter adapterSameApp;

	private void initSameAppView() {
		// TODO Auto-generated method stub
		sameAppListView = (PullToRefreshListView) sameAppFriendList.findViewById(R.id.sameapplist);
		noData = (TextView) nearFriendList.findViewById(R.id.nodata);	
		noData.setVisibility(View.GONE);
		whichView=3;
		adapterSameApp = new SameAppFriendListAdapter(mContext);
		sameAppListView.setAdapter(adapterSameApp);
		sameAppListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				adapterSameApp.clearList();
				handler_sameApp.sendEmptyMessage(0);
			}
		});
		handler_sameApp.sendEmptyMessage(0);
		sameAppListView.setOnScrollListener(this);
	}
	Handler handler_sameApp = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage = 1;
				currPages = String.valueOf(curPage);				
				handler_sameApp.sendEmptyMessage(2);
				handler_sameApp.sendEmptyMessage(1);
				break;

			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(
						new RequestSameAppFriendsList(
								AccountManager.Instace(mContext).userId,
								currPages), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseSameAppFriendsList res = (ResponseSameAppFriendsList) response;
								if (res.result.equals("261")) {	
									sameAppList.addAll(res.list);
									adapterSameApp.addList(res.list);
									if (res.friendCounts == nearList.size()
											|| res.friendCounts < nearList.size()) {
										isSameAppLastPage = true;
									} else {
										isSameAppLastPage = false;
									}
								} else if(res.result.equals("262")){
									handler_sameApp.sendEmptyMessage(5);
								}
								curPage += 1;
								currPages = String.valueOf(curPage);
								handler_sameApp.sendEmptyMessage(4);

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
				handler_sameApp.sendEmptyMessage(3);
				adapterSameApp.notifyDataSetChanged();
				sameAppListView.onRefreshComplete();
				setListenerSameApp();
				break;
			case 5:
				//noData.setVisibility(View.VISIBLE);
				//sameAppListView.setVisibility(View.GONE);
				break;

			case 7:
				handler_sameApp.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("fanuid",
						DataManager.Instace().sameAppFriendInfo.uid);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
				break;
			
			default:
				break;
			}
		}

	

	};
	private void setListenerSameApp() {
		// TODO Auto-generated method stub
		sameAppListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				DataManager.Instace().sameAppFriendInfo = sameAppList.get(position-1);
				handler_sameApp.sendEmptyMessage(7);// 进入私信聊天界面

			}
		});
	}
	private ArrayList<PublicAccountInfo> publicAccountArrayList = new ArrayList<PublicAccountInfo>();
	private PullToRefreshListView publicaccountsListView;
	private PublicAccountsListAdapter adapterPublicAccount;
	//设置公共账号页
	private void initPublicAccountsList() {
		// TODO Auto-generated method stub
		publicaccountsListView = (PullToRefreshListView) publicAccoutsList.findViewById(R.id.publicaccountslist);
		noData = (TextView) publicAccoutsList.findViewById(R.id.nodata);
		whichView=4;
		adapterPublicAccount = new PublicAccountsListAdapter(mContext);
		publicaccountsListView.setAdapter(adapterPublicAccount);
		publicaccountsListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				adapterPublicAccount.clearList();
				handler_publicAccounts.sendEmptyMessage(0);
			}
		});
		handler_publicAccounts.sendEmptyMessage(0);
		publicaccountsListView.setOnScrollListener(this);
	}
	Handler handler_publicAccounts = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage = 1;
				currPages = String.valueOf(curPage);				
				handler_publicAccounts.sendEmptyMessage(2);
				handler_publicAccounts.sendEmptyMessage(1);
				break;

			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(
						new RequestPublicAccountsList(
								AccountManager.Instace(mContext).userId,
								currPages), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponsePublicAccountsList res = (ResponsePublicAccountsList) response;
								if (res.result.equals("141")) {					
									publicAccountArrayList.addAll(res.list);
									adapterPublicAccount.addList(res.list);
									if (res.pageNumber .equals(res.totalPage)) {
										isAccountsLastPage = true;
									} else {
										isAccountsLastPage = false;
									}
								} else if(res.result.equals("142")){
									handler_publicAccounts.sendEmptyMessage(5);
								}
								curPage += 1;
								currPages = String.valueOf(curPage);
								handler_publicAccounts.sendEmptyMessage(4);

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
				handler_publicAccounts.sendEmptyMessage(3);
				adapterPublicAccount.notifyDataSetChanged();
				publicaccountsListView.onRefreshComplete();
				setListenerPublicAccount();
				break;
			case 5:
				noData.setVisibility(View.VISIBLE);
				publicaccountsListView.setVisibility(View.GONE);
				break;

			case 7:
				handler_publicAccounts.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("fanuid",
						DataManager.Instace().publicAccountInfo.uid);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
				break;
			
			default:
				break;
			}
		}
	};
	private void setListenerPublicAccount() {
		// TODO Auto-generated method stub
		publicaccountsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub		
				DataManager.Instace().publicAccountInfo=publicAccountArrayList.get(position-1);
				handler_publicAccounts.sendEmptyMessage(7);// 进入个人主页

			}
		});
	}
	private void setOption() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		System.out.println("option==="+option.getCoorType());
		System.out.println("mLocClient==="+mLocClient.getVersion());
		mLocClient.setLocOption(option);
	}
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;						
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");			
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
			System.out.println("*******"+sb);
			x=String.valueOf(location.getLongitude());
			y=String.valueOf(location.getLatitude());
			
		}
	public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Poi time : ");
				sb.append(poiLocation.getTime());
				sb.append("\nerror code : ");
				sb.append(poiLocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(poiLocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(poiLocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(poiLocation.getRadius());
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
				Log.e("qqqq", "" + sb);
				
			}
	}


	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage = 1;
				currPages = String.valueOf(curPage);
				
				handler.sendEmptyMessage(2);
				handler.sendEmptyMessage(1);
				break;

			case 1:
				//获取附近好友
				ClientNetwork.Instace().asynGetResponse(
						new RequestNearFriendsList(
								AccountManager.Instace(mContext).userId,
								currPages, x, y), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseNearFriendsList res = (ResponseNearFriendsList) response;
								if (res.result.equals("711")) {
									nearList.addAll(res.list);
									adapterNear.addList(res.list);
									if (res.total == nearList.size()
											|| res.total < nearList.size()) {
										isNearLastPage = true;
									} else {
										isNearLastPage = false;
									}
								} else if (res.result.equals("710")) {
									noData.setVisibility(View.VISIBLE);
									nearListView.setVisibility(View.GONE);
								}
								curPage += 1;
								currPages = String.valueOf(curPage);
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
				adapterNear.notifyDataSetChanged();
				nearListView.onRefreshComplete();
				setListener();
				break;

			case 7:
				handler.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("fanuid",
						DataManager.Instace().nearFriendInfo.uid);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
				break;
			case 10:
				// 上传当前位置
				ClientNetwork.Instace().asynGetResponse(
						new RequestSendLocation(
								AccountManager.Instace(mContext).userId, x, y),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseSendLocation res = (ResponseSendLocation) response;
								if (res.result.equals("701")) {
									System.out.println("位置上传成功");
								} else {
									System.out.println("位置上传失败");
								}

							}
						});
				break;
			default:
				break;
			}
		}

	};

	private void setListener() {
		handler.sendEmptyMessage(10);
		nearListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				DataManager.Instace().nearFriendInfo = nearList.get(position-1);
				handler.sendEmptyMessage(7);// 进入私信聊天界面

			}
		});
	}

	private void initTab() {
		// TODO Auto-generated method stub
		guess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(0);
			}
		});
		near.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(1);
			}
		});
		sameapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(2);
			}
		});
		accounts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(3);
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
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mLocClient.stop();
		super.onDestroy();
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
				if(whichView==2){
					if(!isNearLastPage){
						handler.sendEmptyMessage(1);
					}
				}else if(whichView==3){
					if(!isSameAppLastPage){
						handler_sameApp.sendEmptyMessage(1);
					}
				}else if(whichView==4){
					if(!isAccountsLastPage){
						handler_publicAccounts.sendEmptyMessage(1);
					}
				}
			}
			break;

		default:
			break;
		}
	}

}
