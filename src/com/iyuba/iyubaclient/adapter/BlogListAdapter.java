/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.BlogInfo;
import android.R.integer;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author yao
 * 日志列表Adapter
 */
public class BlogListAdapter extends BaseAdapter{

	private Context mContext;
	public  ArrayList<BlogContent> mList=new ArrayList<BlogContent>();
	private ViewHolder viewHolder;
	
	/**
	 * @param mContext
	 * @param mList
	 */
	public BlogListAdapter(Context mContext, ArrayList<BlogContent> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

	/**
	 * @param mContext
	 */
	public BlogListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	public void addList(ArrayList<BlogContent> blogList){
		mList.addAll(blogList);
		
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
	public void clearList(){
		mList.clear();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final BlogContent curBlog=mList.get(position);
	//	System.out.println("position------>"+position);
	//	System.out.println("curBlog------>"+curBlog.subject);
		if(convertView==null){
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=vi.inflate(R.layout.bloglist_item, null);
			viewHolder=new ViewHolder();
			viewHolder.replyNum=(TextView)convertView.findViewById(R.id.blogReplyNum);
			//viewHolder.shareNum=(TextView)convertView.findViewById(R.id.blogShareNum);
			viewHolder.subject=(TextView)convertView.findViewById(R.id.blogSubject);
			viewHolder.time=(TextView)convertView.findViewById(R.id.blogTime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.replyNum.setText(curBlog.replynum);
		//viewHolder.shareNum.setText(curBlog.sharetimes);
		viewHolder.subject.setText(curBlog.subject);
		long time=Long.parseLong(curBlog.dateline)*1000;
		DateFormat.format("yyyy-MM-dd hh:ss:mm", time);
		
		viewHolder.time.setText(DateFormat.format("yyyy-MM-dd hh:ss:mm", time));
		return convertView;
	}
	class ViewHolder{
		TextView subject;
		TextView time;
		TextView replyNum;
		//TextView shareNum;
	}
}
