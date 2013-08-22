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

import com.iyuba.iyubaclient.adapter.SearchResultAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.procotol.RequestFansList;
import com.iyuba.iyubaclient.procotol.ResponseFansList;
import com.iyuba.iyubaclient.search.procotol.RequestSearchList;
import com.iyuba.iyubaclient.search.procotol.ResponseSearchList;
import com.iyuba.iyubaclient.sqlite.entity.SearchItem;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author yao
 *
 */
public class SearchResultActivity extends THSActivity implements OnScrollListener{

	private Button cancel,inAll,inFans,inFollows,inMutual;
	private EditText editText;
	private String currPages="1";
	private int curPage=1;
	private SearchResultAdapter adapter;
	private ArrayList<SearchItem> list=new ArrayList<SearchItem>();
	private String search;
	private CustomDialog waitingDialog;
	private String id;
	private ListView listView;
	private int searchRange;//1 粉丝 2 我关注的 3 互相关注 0 全站用户
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		waitingDialog=waitingDialog();
		initWidget();
		adapter=new SearchResultAdapter(mContext);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		setListener();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		cancel=(Button)findViewById(R.id.findCancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchResultActivity.this.finish();
			}
		});
		inAll=(Button)findViewById(R.id.inAll);
		inFans=(Button)findViewById(R.id.inFans);
		inFollows=(Button)findViewById(R.id.inFollows);
		inMutual=(Button)findViewById(R.id.inMutual);
		editText=(EditText)findViewById(R.id.edittext);	
		listView=(ListView)findViewById(R.id.findresult);
	}
	private void setListener(){
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				String content=editText.getText().toString();
				System.out.println("content===="+content);
				if(EditorInfo.IME_ACTION_SEND==actionId){
					System.out.println("dahsidshdaahskadasjdsjdjfksjfk");
				}
				if(EditorInfo.IME_ACTION_DONE==actionId){
					System.out.println("IME_ACTION_DONE");
				}
				if(EditorInfo.IME_ACTION_GO==actionId){
					System.out.println("IME_ACTION_GO");
				}

				return true;
			}
		});
		inAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search=editText.getText().toString();
				System.out.println("search===="+search);
				searchRange=0;
				handler.sendEmptyMessage(12);
			}
		});
		inFans.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search=editText.getText().toString();
				System.out.println("search===="+search);
				searchRange=1;
				handler.sendEmptyMessage(0);
			}
		});
		inFollows.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search=editText.getText().toString();
				System.out.println("search===="+search);
				searchRange=2;
				handler.sendEmptyMessage(10);
			}
		});
		inMutual.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search=editText.getText().toString();
				System.out.println("search===="+search);
				searchRange=3;
				handler.sendEmptyMessage(11);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				id=list.get(arg2).uid;
				handler.sendEmptyMessage(7);
			}
		});
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
				list.clear();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;

			case 1:
				//在粉丝中搜索
				ClientNetwork.Instace().asynGetResponse(new RequestSearchList(AccountManager.Instace(mContext).userId,search, "1",currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSearchList res=(ResponseSearchList)response;					
						if(res.result.equals("591")){					
							list.addAll(res.list);
							adapter.addList(res.list);													
						}else{							
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(4);
					}
					
				});
				break;
			case 10:
				//在我关注中搜索
				adapter.clearList();
				//handler.sendEmptyMessage(2);
				ClientNetwork.Instace().asynGetResponse(new RequestSearchList(AccountManager.Instace(mContext).userId,search, "2",currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSearchList res=(ResponseSearchList)response;					
						if(res.result.equals("591")){					
							list.addAll(res.list);
							adapter.addList(res.list);													
						}else{							
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(4);
					}
					
				});
				break;
			case 11:
				//在互相关注中搜索
				adapter.clearList();
				//handler.sendEmptyMessage(2);
				ClientNetwork.Instace().asynGetResponse(new RequestSearchList(AccountManager.Instace(mContext).userId,search, "3",currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSearchList res=(ResponseSearchList)response;					
						if(res.result.equals("591")){					
							list.addAll(res.list);
							adapter.addList(res.list);													
						}else{							
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(4);
					}
					
				});
				break;
			case 12:
				//在全站用户中搜索
				adapter.clearList();
				//handler.sendEmptyMessage(2);
				ClientNetwork.Instace().asynGetResponse(new RequestSearchList(AccountManager.Instace(mContext).userId,search, "0",currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseSearchList res=(ResponseSearchList)response;					
						if(res.result.equals("591")){					
							list.addAll(res.list);
							adapter.addList(res.list);													
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
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.putExtra("fanuid",id);
				intent.setClass(mContext,PersonalHomeActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
		
	};
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
		case OnScrollListener.SCROLL_STATE_IDLE:
			if(view.getLastVisiblePosition()==(view.getCount()-1)){
				if(searchRange==0){
					handler.sendEmptyMessage(12);
				}else if(searchRange==1){
					handler.sendEmptyMessage(1);
				}else if(searchRange==2){
					handler.sendEmptyMessage(10);
				}else if(searchRange==3){
					handler.sendEmptyMessage(11);
				}
			}
			break;

		default:
			break;
		}
	}
	
}
