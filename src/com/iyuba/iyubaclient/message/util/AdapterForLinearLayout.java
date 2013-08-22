/**
 * 
 */
package com.iyuba.iyubaclient.message.util;

import java.util.ArrayList;

import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author yao
 *
 */
public class AdapterForLinearLayout extends BaseAdapter{

	private LayoutInflater mInflater;
	private ArrayList<DoingsInfo> mList;
	private Context mContext;
	private int resource;
	private String[] from;
	private int[] to;
	/**
	 * @param mInflater
	 * @param mList
	 * @param mContext
	 * @param resource
	 * @param from
	 * @param to
	 */
	public AdapterForLinearLayout(ArrayList<DoingsInfo> mList, Context mContext, int resource,
			String[] from, int[] to) {
		super();
		this.mInflater =(LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mList = mList;
		this.mContext = mContext;
		this.resource = resource;
		this.from = from;
		this.to = to;
	}

	/**
	 * @param mInflater
	 * @param mList
	 * @param mContext
	 */
	public AdapterForLinearLayout(LayoutInflater mInflater,
			ArrayList<DoingsInfo> mList, Context mContext,int resource) {
		super();
		this.mInflater = mInflater;
		this.mList = mList;
		this.mContext = mContext;
		this.resource=resource;
	}

	/**
	 * @param mContext
	 */
	public AdapterForLinearLayout(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=mInflater.inflate(resource, null);
		DoingsInfo item=mList.get(position);
		int count=to.length;
		for(int i=0;i<count;i++){
			View view=convertView.findViewById(to[i]);
			bindView(view,item,from[i]);
		}
		convertView.setTag(position);
		return convertView;
	}
	private void bindView(View view,DoingsInfo item,String from){
		if(view instanceof TextView){
			
		}
	}

}
