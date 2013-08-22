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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.iyuba.iyubaclient.adapter.DoingsListAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestBlogInfo;
import com.iyuba.iyubaclient.procotol.RequestDoingsInfo;
import com.iyuba.iyubaclient.procotol.ResponseBlogInfo;
import com.iyuba.iyubaclient.procotol.ResponseDoingsInfo;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.util.FileDownProcessBar;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

public class DoingsListActivity extends THSActivity implements OnScrollListener {
	private Button backButton;
	private ListView doingsList;
	private DoingsListAdapter doingsListAdapter;
	private String currPages = "1";
	private int curPage = 1;
	private CustomDialog waitingDialog;
	private View footer, nodata;
	private ArrayList<DoingsInfo> doingsArrayList = new ArrayList<DoingsInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doingslist);
		waitingDialog = waitingDialog();
		backButton = (Button) findViewById(R.id.doingslist_back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DoingsListActivity.this.finish();
			}
		});
		doingsList = (ListView) findViewById(R.id.doingslist);
		nodata = (LinearLayout) findViewById(R.id.doing_nodata);
		doingsListAdapter = new DoingsListAdapter(mContext);
		footer = getLayoutInflater().inflate(R.layout.loadmore, null);
		doingsList.addFooterView(footer);
		doingsList.removeFooterView(footer);
		doingsList.setAdapter(doingsListAdapter);
		doingsList.setOnScrollListener(this);
		handler.sendEmptyMessage(0);//
		setListener();
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
				doingsListAdapter.clearList();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(
						new RequestDoingsInfo(
								AccountManager.Instace(mContext).userId,
								currPages), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseDoingsInfo responseDoingsInfo = (ResponseDoingsInfo) response;
								if (responseDoingsInfo.result.equals("301")) {
									if (responseDoingsInfo.counts.equals("0")) {
										//handler.sendEmptyMessage(5);
									} else {
										doingsArrayList
												.addAll(responseDoingsInfo.doingslist);
										doingsListAdapter
												.addList(responseDoingsInfo.doingslist);
									}

								} else {

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
				doingsListAdapter.notifyDataSetChanged();
				break;
			case 5:
				nodata.setVisibility(View.VISIBLE);
				doingsList.setVisibility(View.GONE);
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("doingsimgsrc", FileDownProcessBar.ROOT_DIR
						+ FileDownProcessBar.IMAGE_PATH);
				intent.setClass(mContext, DoingsActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

	};

	private void setListener() {
		// TODO Auto-generated method stub

		doingsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				DataManager.Instace().doingsInfo = doingsArrayList
						.get(position);
				final String message = doingsArrayList.get(position).message;
				final String uid = doingsArrayList.get(position).uid;
				handler.sendEmptyMessage(7);
			}
		});
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
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				doingsList.addFooterView(footer);
				handler.sendEmptyMessage(1);
				System.out.println("滑动到底部！！！！！");
			}
			break;

		}
	}
}
