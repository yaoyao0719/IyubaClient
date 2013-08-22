package com.iyuba.iyubaclient;

import java.io.File;
import java.util.ArrayList;
import org.ths.frame.components.ConfigManager;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;
import com.iyuba.iyubaclient.adapter.FansListAdapter;
import com.iyuba.iyubaclient.adapter.RecentVisitListAdapter;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestBasicUserInfo;
import com.iyuba.iyubaclient.procotol.RequestFansList;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseFansList;
import com.iyuba.iyubaclient.sqlite.entity.RecentVisitInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.util.FileDownProcessBar;
import com.iyuba.iyubaclient.util.RoundAngleImageView;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.iyuba.iyubaclient.findfriends.procotol.RequestRecentVisitsList;
import com.iyuba.iyubaclient.findfriends.procotol.ResponseRecentVisitsList;
/**
 * @author yao 好友界面
 */
public class FriendsActivity extends BaseActivity implements
		OnActivityGroupKeyDown, OnScrollListener {

	private Context mContext;
	private ImageView ivFindFriends, ivPortrait, ivLocationIcon;
	private TextView tvFindFriends, tvUserName;
	private TextView btn_state_info, btn_fans_info, btn_journal_info;
	private EditText etFriendsSearch;
	private ListView listView;
	private LinearLayout layout_userinfo,findLayout;
	private String stateNum, fansNum, journalNum;
	private String userId, userName, userGender, stateInfo;
	private Bitmap userBitmap;
	private CustomDialog waitingDialog;
	private View headView;
	private FrameLayout flstate,flfans,flbolgs;
	private LayoutInflater layoutInflater;
	private RecentVisitListAdapter adapter;
	private ArrayList<RecentVisitInfo> list = new ArrayList<RecentVisitInfo>();
	private String currPages = "1";
	private int curPage = 1;
    private Boolean isLastPage=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		waitingDialog = waitingDialog();
		mContext = this;
		userId = ConfigManager.Instance().loadString("userId");
		initWidget();
		adapter = new RecentVisitListAdapter(mContext);
		listView.addHeaderView(headView);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		handler.sendEmptyMessage(0);// 加载用户基本信息
		
		setListener();
		DownLoadUserImg();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				ClientNetwork.Instace().asynGetResponse(
						new RequestBasicUserInfo(
								AccountManager.Instace(mContext).userId,AccountManager.Instace(mContext).userId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseBasicUserInfo responseBasicUserInfo = (ResponseBasicUserInfo) response;
								if (responseBasicUserInfo.result.equals("201")) {
									userName = responseBasicUserInfo.username;
									stateNum = responseBasicUserInfo.doings;// 状态数
									fansNum = responseBasicUserInfo.friends;
									journalNum = responseBasicUserInfo.blogs;
									userGender = responseBasicUserInfo.gender;
									stateInfo = responseBasicUserInfo.text;
									UserBasicInfoManager.Instace().responseBasicUserInfo = responseBasicUserInfo;
								}
								
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
				tvUserName.setText(userName);
				btn_state_info.setText(stateNum);
				btn_fans_info.setText(fansNum);
				btn_journal_info.setText(journalNum);
				handler.sendEmptyMessage(9);// 加载用户好友或者最近联系人
				//handler.sendEmptyMessage(3);
				break;
			case 5:
				if(userBitmap==null){
					ivPortrait.setBackgroundResource(R.drawable.defaultavatar);
				}else{
					ivPortrait.setImageDrawable(new BitmapDrawable(userBitmap));
				}			
				break;
			case 9:
				curPage=1;
				currPages=String.valueOf(curPage);
				handler.sendEmptyMessage(10);
				break;
			case 10:
				ClientNetwork.Instace().asynGetResponse(
						new RequestRecentVisitsList(
								AccountManager.Instace(mContext).userId,
								currPages), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseRecentVisitsList res = (ResponseRecentVisitsList) response;
								if (res.result.equals("591")) {
									list.addAll(res.list);
									adapter.addList(res.list);
									if(res.nextPage.equals(res.lastPage)){
										isLastPage=true;
									}
								} else {
								}
								curPage += 1;
								currPages = String.valueOf(curPage);
								handler.sendEmptyMessage(11);
								setListClick();
							}

						

						});
				break;
			case 11:
				handler.sendEmptyMessage(3);
				adapter.notifyDataSetChanged();
				break;

			}

		}
	};
	private void setListClick() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("fanuid", list.get(arg2-1).vuid);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
			}
		});
	}
	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					File file = new File(Constant.APP_DATA_PATH
							+ Constant.SDCARD_PORTEAIT_PATH + userId + ".jpeg");
					if (file.exists()) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						userBitmap = BitmapFactory.decodeFile(
								Constant.APP_DATA_PATH
										+ Constant.SDCARD_PORTEAIT_PATH
										+ userId + ".jpeg", options);
						handler.sendEmptyMessage(5);
					} else {
						DownLoadManager.Instace(mContext).downLoadImage(
								Constant.IMAGE_DOWN_PATH + userId, userId,
								new DownLoadCallBack() {
									@Override
									public void downLoadSuccess(Bitmap image) {
										// TODO Auto-generated method stub
										userBitmap = image;
										handler.sendEmptyMessage(5);
									}

									@Override
									public void downLoadFaild(String error) {
										// TODO Auto-generated method stub
									}
								}, true);
					}

				}
			}
		};
		t.start();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headView = layoutInflater.inflate(R.layout.friend_headview, null);	
		etFriendsSearch = (EditText) headView
				.findViewById(R.id.etFriendsSearch);
		findLayout=(LinearLayout)headView.findViewById(R.id.findlayout);
		ivFindFriends = (ImageView) headView.findViewById(R.id.ivFindFriends);
		ivPortrait = (RoundAngleImageView) headView.findViewById(R.id.ivPortrait);
		ivLocationIcon = (ImageView) headView.findViewById(R.id.ivLocationIcon);
		tvFindFriends = (TextView) headView.findViewById(R.id.tvFindFriends);
		tvUserName = (TextView) headView.findViewById(R.id.tvUserName);
		btn_fans_info = (TextView) headView.findViewById(R.id.btn_fans_info);
		btn_journal_info = (TextView) headView
				.findViewById(R.id.btn_journal_info);
		btn_state_info = (TextView) headView.findViewById(R.id.btn_state_info);
		layout_userinfo = (LinearLayout) headView
				.findViewById(R.id.layout_userinfo);
		flstate=(FrameLayout)headView.findViewById(R.id.flstate);
		flfans=(FrameLayout)headView.findViewById(R.id.flstate);
		flbolgs=(FrameLayout)headView.findViewById(R.id.flblogs);
		listView = (ListView) findViewById(R.id.lvRecentContacts);
		etFriendsSearch.setFocusable(true);   
		etFriendsSearch.setFocusableInTouchMode(true);   
		etFriendsSearch.requestFocus(); 
	}

	private void setListener() {
		// TODO Auto-generated method stub
		etFriendsSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("setOnClickListener   #####");
				etFriendsSearch.setInputType(InputType.TYPE_NULL);
				Intent intent = new Intent();
				intent.setClass(mContext,SearchResultActivity.class);
				startActivity(intent);
			}
		});
		ivFindFriends.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		layout_userinfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statenum", stateNum);
				intent.putExtra("fansnum", fansNum);
				intent.putExtra("username11", userName);
				intent.putExtra("journalnum", journalNum);
				intent.putExtra("gender", userGender);
				intent.putExtra("stateinfo", stateInfo);
				intent.putExtra("imgsrc", FileDownProcessBar.ROOT_DIR
						+ FileDownProcessBar.IMAGE_PATH + userId + ".jpeg");
				intent.putExtra("fanuid",
						AccountManager.Instace(mContext).userId);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
			}
		});
		ivPortrait.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statenum", stateNum);
				intent.putExtra("fansnum", fansNum);
				intent.putExtra("username11", userName);
				intent.putExtra("journalnum", journalNum);
				intent.putExtra("gender", userGender);
				intent.putExtra("stateinfo", stateInfo);
				intent.putExtra("imgsrc", FileDownProcessBar.ROOT_DIR
						+ FileDownProcessBar.IMAGE_PATH + userId + ".jpeg");
				intent.putExtra("fanuid",
						AccountManager.Instace(mContext).userId);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
			}
		});
		ivLocationIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		tvFindFriends.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		tvUserName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statenum", stateNum);
				intent.putExtra("fansnum", fansNum);
				intent.putExtra("username11", userName);
				intent.putExtra("journalnum", journalNum);
				intent.putExtra("gender", userGender);
				intent.putExtra("stateinfo", stateInfo);
				intent.putExtra("imgsrc", FileDownProcessBar.ROOT_DIR
						+ FileDownProcessBar.IMAGE_PATH + userId + ".jpeg");
				intent.putExtra("fanuid",
						AccountManager.Instace(mContext).userId);
				intent.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent);
			}
		});
		btn_fans_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("currentuid", userId);
				intent.setClass(mContext, FansListActivity.class);
				startActivity(intent);
			}
		});
		btn_journal_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("currentuid", userId);
				intent.putExtra("currentusername", AccountManager.Instace(mContext).userName);
				System.out.println("AccountManager.Instace(mContext).userInfo.username==="+AccountManager.Instace(mContext).userName);
				intent.setClass(mContext, BlogListActivity.class);
				startActivity(intent);
			}
		});
		btn_state_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, DoingsListActivity.class);
				startActivity(intent);
			}
		});
		flfans.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("currentuid", userId);
				intent.setClass(mContext, FansListActivity.class);
				startActivity(intent);
			}
		});
		flbolgs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("currentuid", userId);
				intent.putExtra("currentusername", AccountManager.Instace(mContext).userName);
				System.out.println("AccountManager.Instace(mContext).userInfo.username==="+AccountManager.Instace(mContext).userName);
				intent.setClass(mContext, BlogListActivity.class);
				startActivity(intent);
			}
		});
		flstate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, DoingsListActivity.class);
				startActivity(intent);
			}
		});
		findLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, FindFriendsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onSubActivityKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
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
		if(view.getLastVisiblePosition()==(view.getCount() - 1)){
			if(!isLastPage){
				handler.sendEmptyMessage(10);
				System.out.println("滑动到底部！！！！！");
			}
			
		}
	}

}
