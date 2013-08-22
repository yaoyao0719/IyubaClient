/**
 * 
 */
package com.iyuba.iyubaclient.adapter;

import java.util.List;

import com.iyuba.iyubaclient.R;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.util.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * @author yao
 *生词本
 */
public class WordsListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Words> words;
	private ViewHolder viewHolder;

	private boolean isEditMode = false;
	private boolean isTrashMode = false; // 垃圾箱模式

	/**
	 * 设置是否为编辑模式
	 * 
	 * @param isEditMode
	 */
	public void setEditMode(boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public List<Words> getwords() {
		return words;
	}

	public void setwords(List<Words> words) {
		this.words = words;
	}

	public WordsListAdapter(Context context, List<Words> words) {
		this.mContext = context;
		this.words = words;
	}

	public WordsListAdapter(Context context, List<Words> words,
			boolean isTrashMode) {
		this.mContext = context;
		this.words = words;
		this.isTrashMode = isTrashMode;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return words.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return words.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater vInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vInflater.inflate(R.layout.wordlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.key = (TextView) convertView
					.findViewById(R.id.wordsItem_key);
			viewHolder.pron = (TextView) convertView
					.findViewById(R.id.wordsItem_pron);
			viewHolder.def = (TextView) convertView
					.findViewById(R.id.wordsItem_def);
			viewHolder.spaker = (ImageButton) convertView
					.findViewById(R.id.wordsItem_spaker);
			viewHolder.isSelect = (CheckBox) convertView
					.findViewById(R.id.isSelect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.key.setText(words.get(position).key);
		viewHolder.pron.setText("[ " + words.get(position).pron + " ]");
		viewHolder.def.setText(words.get(position).def);

		if (words.get(position).audio != null
				&& words.get(position).audio.length() != 0) {
			viewHolder.spaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Player player = new Player(mContext, null, null);
					String url = words.get(position).audio;
					// Log.e("单词发音路径", url);
					player.playUrl(url);
				}
			});
		} else {
			viewHolder.spaker.setVisibility(View.GONE);
		}
		if (isEditMode) {
			viewHolder.isSelect.setVisibility(View.VISIBLE);
			viewHolder.spaker.setVisibility(View.GONE);
			viewHolder.isSelect
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isTrashMode) {
								if (isChecked) {
									words.get(position).status = 999;// 执行操作状态
								} else {
									words.get(position).status = 2;
								}
							} else {
								if (isChecked) {
									words.get(position).status = 2;
								} else {
									words.get(position).status = 1;
								}
							}
						}
					});
			
			if (isTrashMode) {
				if (words.get(position).status == 999) {
					viewHolder.isSelect.setChecked(true);
				} else {
					viewHolder.isSelect.setChecked(false);
				}
			}else{
				if (words.get(position).status == 2) {
					viewHolder.isSelect.setChecked(true);
				} else {
					viewHolder.isSelect.setChecked(false);
				}
			}

		} else {
			viewHolder.isSelect.setVisibility(View.GONE);
			if (words.get(position).audio != null
					&& words.get(position).audio.length() != 0) {
				viewHolder.spaker.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView key;
		TextView pron;
		TextView def;
		ImageButton spaker;
		CheckBox isSelect;
	}

}
