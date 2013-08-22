/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.THSActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iyuba.iyubaclient.adapter.POIListAdapter;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.POIInfo;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

/**
 * @author yao
 * 
 */
public class POIListActivity extends THSActivity {

	private PullToRefreshListView listView;
	private Button back, refresh;
	public LocationClient client = null;
	public BDLocationListener myListener = new LocationListener();
	private ArrayList<POIInfo> poiList = new ArrayList<POIInfo>();
	private String poiString = null;
	public JSONArray data;
	private POIListAdapter adapter;
	private String fromiyu="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poilist);
		client = new LocationClient(getApplicationContext()); // 声明LocationClient类
		client.registerLocationListener(myListener); // 注册监听函数
		setOption();
		client.start();
		Intent intent=getIntent();
		fromiyu=intent.getStringExtra("fromiyu");
		initWidget();
		setAdapter();
		setOnClick();
	}


	private void initWidget() {
		// TODO Auto-generated method stub
		back = (Button) findViewById(R.id.poilist_back_btn);
		refresh = (Button) findViewById(R.id.poi_refresh);
		listView = (PullToRefreshListView) findViewById(R.id.poiListView);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("点击按钮", "点击按钮");
				if (client != null && client.isStarted()) {
					client.requestPoi();
				}

			}
		});

	}

	private void setOnClick() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				DataManager.Instace().poiInfo=poiList.get(arg2-1);
				if(fromiyu.equals("fromiyu")){
					Intent intent =new Intent(mContext,PublishMoodActivity.class);
					intent.putExtra("haveloc", true);//已经定位
					startActivity(intent);
					finish();
				}else{
					Intent intent =new Intent(mContext,ReportActivity.class);
					startActivity(intent);
					finish();
				}			
			}
		});
	}
	private void setPoiList() {
		// TODO Auto-generated method stub
		if (myListener != null) {
			if (poiString != null) {
				try {
					JSONObject jsonObject = new JSONObject(poiString.substring(
							poiString.indexOf("{"),
							poiString.lastIndexOf("}") + 1));
					Log.e("poistring 2222 ", poiString);
					data = jsonObject.getJSONArray("p");
					if (data != null && data.length() != 0) {
						for (int i = 0; i < data.length(); i++) {
							try {
								POIInfo item = new POIInfo();
								JSONObject json = ((JSONObject) data.opt(i));
								item.addr = json.getString("addr");
								item.dis = json.getString("dis");
								item.y = json.getString("y");
								item.name = json.getString("name");
								item.tel = json.getString("tel");
								item.x = json.getString("x");
								poiList.add(item);
								
							} catch (JSONException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setAdapter();
			}
		}
	}

	private void setAdapter() {
		// TODO Auto-generated method stub
		adapter = new POIListAdapter(this);
		listView.setAdapter(adapter);
		listView.setRefresh(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				refresh.performClick();
			}
		});		
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				poiList.clear();
				adapter.clearList();
				if (client != null && client.isStarted()) {
					client.requestPoi();
				}
				break;
			case 1:
				setPoiList();
				break;
			case 2:
				adapter.addList(poiList);
				listView.onRefreshComplete();
				break;
			default:
				break;
			}
		}

	};

	private void setOption() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(20); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		client.setLocOption(option);
	}

	public class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
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

			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				
				return;
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
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
				poiString = poiLocation.getPoi();
				handler.sendEmptyMessage(1);
			} else {
				sb.append("noPoi information");
			}
			Log.e("222222222", ""+sb);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
	}
}
