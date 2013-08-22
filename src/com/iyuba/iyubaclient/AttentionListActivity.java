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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.iyuba.iyubaclient.adapter.AttentionListAdapter;
import com.iyuba.iyubaclient.adapter.FansListAdapter;
import com.iyuba.iyubaclient.entity.Attention;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestAttentionList;
import com.iyuba.iyubaclient.procotol.RequestFansList;
import com.iyuba.iyubaclient.procotol.ResponseAttentionList;
import com.iyuba.iyubaclient.procotol.ResponseFansList;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

/**
 * @author yao
 *
 */
public class AttentionListActivity extends THSActivity implements OnScrollListener{

	private Button backButton,home;
	private ListView fansListView;
	private AttentionListAdapter adapter;
	private CustomDialog waitingDialog;
	private ArrayList<Attention> attentionlist=new ArrayList<Attention>();
	private String currPages="1";
	private int curPage=1;
	private String currentuid;
	private View footer,nodata;
	private String id;
	private Boolean isLastPage=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attentionlist);
		waitingDialog=waitingDialog();
		Intent intent=getIntent();
		currentuid=intent.getStringExtra("currentuid");
		initWidget();
		adapter=new AttentionListAdapter(mContext);
		fansListView.setAdapter(adapter);
		fansListView.setOnScrollListener(this);
		handler.sendEmptyMessage(0);
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		
		fansListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
			
				DataManager.Instace().attention=attentionlist.get(position);
				id=attentionlist.get(position).followuid;
				handler.sendEmptyMessage(7);
			}
		});
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		backButton=(Button)findViewById(R.id.attentionlist_back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AttentionListActivity.this.finish();
			}
		});
		fansListView=(ListView)findViewById(R.id.attentionlist);
		home=(Button)findViewById(R.id.home);
		nodata=(LinearLayout)findViewById(R.id.attention_nodata);
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
				ClientNetwork.Instace().asynGetResponse(new RequestAttentionList(currentuid, currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseAttentionList responseFansList=(ResponseAttentionList)response;
						if(responseFansList.result.equals("550")){					
							attentionlist.addAll(responseFansList.fansList);
							adapter.addList(responseFansList.fansList);	
							if(attentionlist.size()>responseFansList.num||attentionlist.size()==responseFansList.num){
								isLastPage=true;
							}
						}else if(responseFansList.result.equals("551")){		
							handler.sendEmptyMessage(5);
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
			case 5:
				setNodata();
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
	private void setNodata(){
		nodata.setVisibility(View.VISIBLE);
		fansListView.setVisibility(View.GONE);
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
				fansListView.addFooterView(footer);
				if(!isLastPage){
					handler.sendEmptyMessage(1);	
				}	
				
			}
			break;

		}
	}


	
}

