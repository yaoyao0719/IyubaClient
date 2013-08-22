/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ths.frame.THSActivity;

import com.iyuba.iyubaclient.adapter.WordsListAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.util.Player;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * @author yao
 * 生词本删除历史
 */
public class HistoryWordsActivity extends THSActivity {
	private View tb_back;
	private ImageButton tb_edit,tb_synchronous,tb_history;
	private TextView tb_title;

	private ListView wordsListView;
	private WordsListAdapter wordsListAdapter;
	private List<Words> words;

	private boolean isEditMode = false;

	private View controlPanel;
	private Button delBtn,restBtn;
	private View listNoDataView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordlist);
		initTitleBar();
		initWidget();
	}

	public void initTitleBar() {
		tb_title = (TextView) findViewById(R.id.wordlist_text);
		tb_title.setText("生词-回收站");
		tb_back = findViewById(R.id.wordlist_back_btn);
		tb_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tb_edit = (ImageButton) findViewById(R.id.tb_edit);
		tb_edit.setVisibility(View.VISIBLE);
		tb_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isEditMode) {
					isEditMode = false;
				} else {
					isEditMode = true;
				}
				handler.sendEmptyMessage(1);
			}
		});
		tb_synchronous = (ImageButton) findViewById(R.id.tb_synchronous);
		tb_synchronous.setVisibility(View.GONE);
		tb_history=(ImageButton) findViewById(R.id.tb_history);
		tb_history.setVisibility(View.GONE);
	}

	public void initWidget() {
		handler.sendEmptyMessage(1);
		wordsListView = (ListView) findViewById(R.id.wordsListView);
		words = new ArrayList<Words>();
		wordsListAdapter = new WordsListAdapter(mContext,words,true);
		wordsListView.setAdapter(wordsListAdapter);
		handler.sendEmptyMessage(0);
		wordsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (isEditMode) {
					if (wordsListAdapter != null
							&& wordsListAdapter.getwords() != null
							&& wordsListAdapter.getwords().size() != 0) {
						if (wordsListAdapter.getwords().get(arg2).status == 999) {
							wordsListAdapter.getwords().get(arg2).status = 2;
						} else {
							wordsListAdapter.getwords().get(arg2).status = 999;
						}
						wordsListAdapter.notifyDataSetChanged();
					}
				}else{
					Player player = new Player(mContext, null, null);
					String url = wordsListAdapter.getwords().get(arg2).audio;
					player.playUrl(url);
				}
			}
		});
		controlPanel=findViewById(R.id.words_controlPanel);
		delBtn = (Button) findViewById(R.id.words_delBtn);
		delBtn.setText("彻底删除");
		delBtn.setOnClickListener(onClickListener);
		delBtn.setVisibility(View.GONE);
		restBtn=(Button) findViewById(R.id.words_restBtn);
		restBtn.setVisibility(View.VISIBLE);
		restBtn.setOnClickListener(onClickListener);
		listNoDataView=findViewById(R.id.wordsList_noList);
		listNoDataView.setVisibility(View.GONE);
	}

	private OnClickListener onClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) { 
			int mode=999;
			if(v==delBtn){
				mode=999;
			}else if(v==restBtn){
				mode=1;
			}
			// TODO Auto-generated method stub
			if (wordsListAdapter != null
					&& wordsListAdapter.getwords() != null
					&& wordsListAdapter.getwords().size() != 0) {
				List<Words> delWords = new ArrayList<Words>();
				Iterator<Words> iterator = wordsListAdapter
						.getwords().iterator();
				while (iterator.hasNext()) {
					Words delWord = (Words) iterator.next();
					if (delWord.status == 999) { // 999=操作
						delWord.status=mode;
						delWords.add(delWord);
						iterator.remove();
					}
				}
				if(delWords.size()!=0){ 
					if(v==delBtn){
						// 彻底删除
						if (DatabaseManager.Instace(mContext).getWordsHelper()
								.deleteWord(delWords)) {
							wordsListAdapter.notifyDataSetChanged();
							Toast.makeText(mContext,"删除成功",
									Toast.LENGTH_SHORT).show();
							isEditMode=false;
							handler.sendEmptyMessage(1);
						}
					}else if(v==restBtn){
						// 恢复到生词本
						if (DatabaseManager.Instace(mContext).getWordsHelper()
								.updataWordsData(delWords)) {
							wordsListAdapter.notifyDataSetChanged();
							Toast.makeText(mContext,"恢复成功",
									Toast.LENGTH_SHORT).show();
							isEditMode=false;
							handler.sendEmptyMessage(1);
						}
					}
					
				}else{
					Toast.makeText(mContext, "当前未选中任何单词！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext,  "生词本无数据",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: // 查询被删除的数据
				StringBuffer selection=new StringBuffer();;
				selection.append("status =? and forUser =?");
				
				words = DatabaseManager.Instace(mContext)
						.getWordsHelper().getWordss(selection.toString(),new String[]{"2",AccountManager.Instace(mContext).userId},"key");
				if (wordsListAdapter != null) {
					wordsListAdapter.setwords(words);
					wordsListAdapter.notifyDataSetChanged();
				}
				break;
			case 1: // 改变删除按钮状态
				if (isEditMode) {
					controlPanel.setVisibility(View.VISIBLE);
				} else {
					controlPanel.setVisibility(View.GONE);
				}
				wordsListAdapter.setEditMode(isEditMode);
				if (wordsListAdapter != null) {
					wordsListAdapter.notifyDataSetChanged();
				}
				break;
			}
		}

	};

	
}
