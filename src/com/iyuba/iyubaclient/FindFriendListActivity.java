/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.ArrayList;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.adapter.FansListAdapter;
import com.iyuba.iyubaclient.adapter.MutualAttentionAdapter;
import com.iyuba.iyubaclient.adapter.SearchResultAdapter;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.entity.MutualAttention;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestBlogInfo;
import com.iyuba.iyubaclient.procotol.RequestFansList;
import com.iyuba.iyubaclient.procotol.RequestMutualAttentionList;
import com.iyuba.iyubaclient.procotol.ResponseBlogInfo;
import com.iyuba.iyubaclient.procotol.ResponseFansList;
import com.iyuba.iyubaclient.procotol.ResponseMutualAttentionList;
import com.iyuba.iyubaclient.search.procotol.RequestSearchList;
import com.iyuba.iyubaclient.search.procotol.ResponseSearchList;
import com.iyuba.iyubaclient.sqlite.entity.SearchItem;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author yao
 *
 */
public class FindFriendListActivity extends THSActivity implements OnScrollListener{
	private ListView friendList,searchListView;
	private char letter = 'A';
	private Button backButton,button;
	private EditText editText;
	private CustomDialog waitingDialog;
	private String currPages="1";
	private int curPage=1;
	private int curSearchPage=1;
	private String currSearchPages="1";
	private MutualAttentionAdapter adapter;
	private String id;
	private ArrayList<MutualAttention> list=new ArrayList<MutualAttention>();
	private ArrayList<SearchItem> searchList=new ArrayList<SearchItem>();
	private String editContent;//搜索内容
	private SearchResultAdapter adapterSearch;
	private LinearLayout layout_friend,layout_search,layout_nodata;
	private Boolean isSearchLastPage=false;
	private Boolean isFriendLastPage=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendlist);
		initWidget();
		waitingDialog=waitingDialog();
		adapter=new MutualAttentionAdapter(mContext);
		friendList.setAdapter(adapter);
		friendList.setOnScrollListener(this);
		handler.sendEmptyMessage(0);
		setListener();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		backButton=(Button)findViewById(R.id.friendlist_back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		editText=(EditText)findViewById(R.id.findfriend);
		friendList=(ListView)findViewById(R.id.friend_list);   
		button=(Button)findViewById(R.id.surebutton);//确定按钮
		layout_friend=(LinearLayout)findViewById(R.id.layout_friend);
		layout_search=(LinearLayout)findViewById(R.id.layout_search);
		layout_nodata=(LinearLayout)findViewById(R.id.layout_nodata);
		searchListView=(ListView)findViewById(R.id.friendsearch_list);
		layout_search.setVisibility(View.GONE);
		layout_nodata.setVisibility(View.GONE);
	}
	private void setListener() {
		// TODO Auto-generated method stub
		
		friendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub			
				DataManager.Instace().mutualAttention=list.get(position);
				id=list.get(position).followuid;
				handler.sendEmptyMessage(7);
			}
		});
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editContent=editText.getText().toString();
				search();
			}

			
		});
	}
	private void search() {
		// TODO Auto-generated method stub
		layout_friend.setVisibility(View.GONE);
		layout_search.setVisibility(View.VISIBLE);
		adapterSearch=new SearchResultAdapter(mContext);
		searchListView.setAdapter(adapterSearch);	
		searchListView.setOnScrollListener(this);
		handler.sendEmptyMessage(10);
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
				adapter.clearList();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(new RequestMutualAttentionList(AccountManager.Instace(mContext).userId, currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseMutualAttentionList responseFansList=(ResponseMutualAttentionList)response;					
						if(responseFansList.result.equals("570")){					
							list.addAll(responseFansList.list);
							adapter.addList(responseFansList.list);		
							if(list.size()==responseFansList.num||list.size()>responseFansList.num){
								isFriendLastPage=true;
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
				//setListViewHeightBasedOnChildren(doings_commentlist);
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.putExtra("friendid",id);
				intent.putExtra("mutualAttention","mutualAttention");
				intent.setClass(mContext,MessageLetterContentActivity.class);
				startActivity(intent);
				break;
			case 10:
				curSearchPage=1;
				currSearchPages=String.valueOf(curSearchPage);
				adapterSearch.clearList();
				handler.sendEmptyMessage(2);
				handler.sendEmptyMessage(11);
				break;
			case 11:
				//在互相关注中搜索
				adapterSearch.clearList();
				ClientNetwork.Instace().asynGetResponse(new RequestSearchList(AccountManager.Instace(mContext).userId,editText.getText().toString(), "3",currSearchPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSearchList res=(ResponseSearchList)response;					
						if(res.result.equals("591")){					
							searchList.addAll(res.list);
							adapterSearch.addList(res.list);		
							handler.sendEmptyMessage(14);
							if(res.nextPage==res.lastPage){
								isSearchLastPage=true;
							}
						}else if(res.result.equals("590")){		
							//无数据
							handler.sendEmptyMessage(12);
						}
						curSearchPage+=1;
						currSearchPages=String.valueOf(curSearchPage);
						handler.sendEmptyMessage(3);
						handler.sendEmptyMessage(13);
					}
					
				});
				break;
			case 13:
				adapterSearch.notifyDataSetChanged();
				break;
			case 12:
				layout_friend.setVisibility(View.GONE);
				layout_search.setVisibility(View.GONE);
				layout_nodata.setVisibility(View.VISIBLE);
				break;
			case 14:
				setOnClick();
				break;
			default:
				break;
			}
		}
		
	};
	private void setOnClick(){
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				DataManager.Instace().searchItem=searchList.get(arg2);
				id=searchList.get(arg2).uid;
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.putExtra("friendid",searchList.get(arg2).uid);
				intent.putExtra("search", "search");
				intent.setClass(mContext,MessageLetterContentActivity.class);
				startActivity(intent);
				//handler.sendEmptyMessage(7);
			}
		});
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
				if(layout_friend.getVisibility()==View.VISIBLE){
					if(!isFriendLastPage){
						handler.sendEmptyMessage(1);
					}				
				}if(layout_search.getVisibility()==View.VISIBLE){
					if(!isSearchLastPage){
						handler.sendEmptyMessage(11);
					}
					
				}
			}
			break;

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
}
