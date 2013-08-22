/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.sqlite.entity.POIInfo;
import com.iyuba.iyubaclient.sqlite.entity.POIInfo;


/**
 * @author yao
 *
 */
public class POIListAdapter extends BaseAdapter{

	private Context mContext;
	public ArrayList<POIInfo> mList=new ArrayList<POIInfo>();
	private ViewHolder viewHolder;
	int i=0;
	
	/**
	 * @param mContext
	 * @param mList
	 */
	public POIListAdapter(Context mContext, ArrayList<POIInfo> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	/**
	 * @param mContext
	 */
	public POIListAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	public void addList(ArrayList<POIInfo> list){
		mList.addAll(list);
	}
    public void clearList(){
    	if(mList.size()==0||mList==null){
    		
    	}else{
    		mList.clear();
    	}
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=layoutInflater.inflate(R.layout.poilist_item, null);
			viewHolder=new ViewHolder();
			viewHolder.name=(TextView)convertView.findViewById(R.id.poilist_name);
			viewHolder.addr=(TextView)convertView.findViewById(R.id.poilist_addr);			
			convertView.setTag(viewHolder);		
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
		viewHolder.name.setText(mList.get(position).name);
		viewHolder.addr.setText(mList.get(position).addr);
		return convertView;
	}
	class ViewHolder{
		TextView name;
		TextView addr;
		
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
			case 1:
				
				break;
			default:
				break;
			}
		}		
	};
	
}
