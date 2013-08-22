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

import com.iyuba.iyubaclient.adapter.BlogListAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestBlogInfo;
import com.iyuba.iyubaclient.procotol.ResponseBlogInfo;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author yao
 * 日志列表类
 */
public class BlogListActivity extends THSActivity implements OnScrollListener{

	
	private Button backButton,writeblog;
	private ListView blogList;
	private BlogListAdapter blogListAdapter;
	private String currPages="1";
	private int curPage=1;
	private CustomDialog waitingDialog;
	private View footer,nodata;
	private ArrayList<BlogContent> blogArrayList =new ArrayList<BlogContent>();
	private String currentuid,currentusername;
	private LayoutInflater layoutInflater;
	private LinearLayout moreLayout;
	private Boolean isLastPage=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloglist);
		waitingDialog=waitingDialog();
		Intent intent=getIntent();
		currentuid=intent.getStringExtra("currentuid");
		currentusername=intent.getStringExtra("currentusername");
		backButton=(Button)findViewById(R.id.bloglist_back_btn);
		writeblog=(Button)findViewById(R.id.writeblog);
		blogList=(ListView)findViewById(R.id.bloglist);
		blogListAdapter=new BlogListAdapter(mContext);
		layoutInflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    footer = layoutInflater.inflate(R.layout.loadmore, null);
	    moreLayout=(LinearLayout)footer.findViewById(R.id.morelayout);
	    nodata=(LinearLayout)findViewById(R.id.blog_nodata);
		blogList.addFooterView(footer);
		blogArrayList.removeAll(blogArrayList);
		blogList.setAdapter(blogListAdapter);
		blogList.setOnScrollListener(this);
		handler.sendEmptyMessage(0);//
		setListener();
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
				blogListAdapter.clearList();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(new RequestBlogInfo(currentuid, currPages) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseBlogInfo responseBlogInfo=(ResponseBlogInfo)response;
						if(responseBlogInfo.result.equals("251")){
							/*blogArrayList=responseBlogInfo.blogList;*/
							blogArrayList.addAll(responseBlogInfo.blogList);
							blogListAdapter.addList(responseBlogInfo.blogList);					
						}else if(responseBlogInfo.result.equals("252")){
							handler.sendEmptyMessage(5);
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						if(responseBlogInfo.isLastPage){
							handler.sendEmptyMessage(8);
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
				handler.sendEmptyMessage(3);
				blogListAdapter.notifyDataSetChanged();
				break;
			case 5:
				nodata.setVisibility(View.VISIBLE);
				blogList.setVisibility(View.GONE);
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.setClass(mContext,BlogActivity.class);
				startActivity(intent);
				break;
			case 8:
				blogList.removeFooterView(footer);
				break;
			default:
				break;
			}
		}
		
	};
	
	private void setListener() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlogListActivity.this.finish();
			}
		});		
		writeblog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(mContext, PublishBlogActivity.class);
				startActivity(intent);
			}
		});
		blogList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				DataManager.Instace().blogContent=blogArrayList.get(position);
				DataManager.Instace().blogContent.username=currentusername;
				System.out.println("currentusername==="+currentusername);
				handler.sendEmptyMessage(7);
			}
		});
		moreLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("更多！！！");
				handler.sendEmptyMessage(1);
			}
		});
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
				//blogList.addFooterView(footer);
				//handler.sendEmptyMessage(1);
			}
			break;

		}
	}
}
