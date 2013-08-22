/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.adapter.WordsListAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.util.Player;
import com.iyuba.iyubaclient.word.procotol.RequestSynWords;
import com.iyuba.iyubaclient.word.procotol.ResponseSynWords;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




/**
 * @author yao
 * 生词本
 */
public class WordListActivity extends THSActivity{

	private View tb_back;
	private ImageView tb_back_icon;
	private ImageButton tb_synchronous,tb_edit, tb_history;
	private ListView wordsListView;
	private WordsListAdapter wordsListAdapter;
	private List<Words> words;

	private boolean isEditMode = false;
	private View controlPanel;
	private Button delBtn, restBtn;
	
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
		tb_back = findViewById(R.id.wordlist_back_btn);
		tb_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tb_synchronous = (ImageButton) findViewById(R.id.tb_synchronous);
		tb_synchronous.setVisibility(View.VISIBLE);
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
		tb_synchronous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // 同步单词
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(2);
			}
		});
		tb_history = (ImageButton) findViewById(R.id.tb_history);
		tb_history.setVisibility(View.VISIBLE);
		tb_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, HistoryWordsActivity.class);
				mContext.startActivity(intent);
			}
		});
	}

	public void initWidget() {
		handler.sendEmptyMessage(1);
		wordsListView = (ListView) findViewById(R.id.wordsListView);
		words = new ArrayList<Words>();
		wordsListAdapter = new WordsListAdapter(mContext, words);
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
						if (wordsListAdapter.getwords().get(arg2).status == 2) {
							wordsListAdapter.getwords().get(arg2).status = 1;
						} else {
							wordsListAdapter.getwords().get(arg2).status = 2;
						}
						wordsListAdapter.notifyDataSetChanged();
					}
				} else {
					Player player = new Player(mContext, null, null);
					String url = wordsListAdapter.getwords().get(arg2).audio;
					player.playUrl(url);
				}
			}
		});
		controlPanel = findViewById(R.id.words_controlPanel);
		delBtn = (Button) findViewById(R.id.words_delBtn);
		delBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (wordsListAdapter != null
						&& wordsListAdapter.getwords() != null
						&& wordsListAdapter.getwords().size() != 0) {
					List<Words> delWords = new ArrayList<Words>();
					Iterator<Words> iterator = wordsListAdapter.getwords()
							.iterator();
					while (iterator.hasNext()) {
						Words delWord = (Words) iterator.next();
						if (delWord.status == 2) { // 2=删除状态
							delWords.add(delWord);
							iterator.remove();
						}
					}
					if (delWords.size() != 0) { // 更改数据库状态
						if (DatabaseManager.Instace(mContext).getWordsHelper()
								.updataWordsData(delWords)) {
							wordsListAdapter.notifyDataSetChanged();
							Toast.makeText(mContext,
									"删除成功",
									Toast.LENGTH_SHORT).show();
							isEditMode = false;
							handler.sendEmptyMessage(1);
						}
					} else {
						Toast.makeText(mContext, "当前未选中任何单词！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, "生词本无数据",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		listNoDataView=findViewById(R.id.wordsList_noList);
		listNoDataView.setVisibility(View.GONE);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: // 查询被收藏的数据
				StringBuffer selection = new StringBuffer();
				selection.append("status !=? and forUser =?");
				words = DatabaseManager
						.Instace(mContext)
						.getWordsHelper()
						.getWordss(
								selection.toString(),
								new String[] { "2",
										AccountManager.Instace(mContext).userId },
								"key");
				if (wordsListAdapter != null) {
					wordsListAdapter.setwords(words);
					wordsListAdapter.notifyDataSetChanged();
				}
				handler.sendEmptyMessage(4);
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
				handler.sendEmptyMessage(4);
				break;
			case 2:// 同步生词本操作
				StringBuffer selectionSyn = new StringBuffer();
				selectionSyn.append("forUser =?");
				List<Words> wordsTemp = DatabaseManager
						.Instace(mContext)
						.getWordsHelper()
						.getWordss(
								selectionSyn.toString(),
								new String[] { AccountManager.Instace(mContext).userId },
								"key");

				ClientNetwork.Instace().asynGetResponse(
						new RequestSynWords(
								AccountManager.Instace(mContext).userId,
								wordsTemp), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseSynWords responseSynWords = (ResponseSynWords) response;
								boolean isDelete = DatabaseManager
										.Instace(mContext)
										.getWordsHelper()
										.deleteWord(
												"forUser",
												AccountManager
														.Instace(mContext).userId);

								if (responseSynWords.words != null
										&& responseSynWords.words.size() != 0) {
									long count = DatabaseManager
											.Instace(mContext)
											.getWordsHelper()
											.insertWordssData(
													responseSynWords.words);
									handler.sendEmptyMessage(0);
								}
							}
						}, new DefNetStateReceiverImpl(), null);
				break;
			case 3:
				wordsListAdapter.notifyDataSetChanged();
				handler.sendEmptyMessage(4);
				break;
			case 4:
				if(words!=null&&words.size()!=0){
					listNoDataView.setVisibility(View.GONE);
				}else{
					listNoDataView.setVisibility(View.VISIBLE);
				}
				break;
			}
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (wordsListAdapter != null) {
			handler.sendEmptyMessage(0);
		}
		
		super.onResume();
	}

}

