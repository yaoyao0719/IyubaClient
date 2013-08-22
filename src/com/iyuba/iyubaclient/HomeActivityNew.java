package com.iyuba.iyubaclient;

import java.util.ArrayList;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import android.widget.Toast;


import com.iyuba.iyubaclient.adapter.FeedListAdapter;
import com.iyuba.iyubaclient.feed.procotol.RequestFeedList;
import com.iyuba.iyubaclient.feed.procotol.ResponseFeedList;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.WordSentence;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.sqlite.helper.FeedInfoHelper;
import com.iyuba.iyubaclient.sqlite.helper.LocalWordsHelper;
import com.iyuba.iyubaclient.sqlite.helper.WordsHelper;
import com.iyuba.iyubaclient.util.AutoScrollTextView;
import com.iyuba.iyubaclient.util.CheckGrade;
import com.iyuba.iyubaclient.util.CheckNetWorkState;
import com.iyuba.iyubaclient.util.Player;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView;
import com.iyuba.iyubaclient.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.iyuba.iyubaclient.feed.procotol.RequestFindBlogById;
import com.iyuba.iyubaclient.feed.procotol.ResponseFindBlogById;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class HomeActivityNew extends BaseActivity implements OnScrollListener {

	private String userName, userId;
	private TextView tvuserName;
	private Button publish_mood_btn, refresh_btn;
	private PullToRefreshListView listView;
	private FeedListAdapter adapter;
	private String currPages = "1";
	private int curPage = 1;
	private Boolean isLastPage = false;
	private ArrayList<FeedInfo> list = new ArrayList<FeedInfo>();
	private Boolean isRerefreshBoolean = false;
	private TextView wordTextView,wordSentence,worddef;
	private ImageView wordsOpen,wordsUp,wordSpeaker,addword,hasadded;
	private View wordlayout,wordScroll;
	private Words words=new Words();
	private LocalWordsHelper localWordsHelper;
	private ArrayList<WordSentence> wordSentences=new ArrayList<WordSentence>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homenew);
		userId = AccountManager.Instace(mContext).userId;
		userName = AccountManager.Instace(mContext).userName;	
		localWordsHelper = new LocalWordsHelper(mContext);
		initWidget();
		setAdapter();
		CheckNetWorkState  checkNetWorkState=new CheckNetWorkState();
		if(checkNetWorkState.netWorkConnect(mContext)==3){
			//无网络，从本地读取
			Log.e("无网络", "无网络");
			Toast.makeText(mContext, "无法连接到网络，请检查网络配置！",
					Toast.LENGTH_SHORT).show();
		
			list=DatabaseManager.Instace(mContext).getFeedInfoHelper().getFeedInfoList(AccountManager.Instace(mContext).userId);
			adapter.addList(list);
			adapter.notifyDataSetChanged();
		}else{
			handler.sendEmptyMessage(0);
		}
		
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.e("点击listVIew", ""+arg2);
			
				if(list.size()>0&&list!=null){
					DataManager.Instace().feed = list.get(arg2 - 1);
					if (DataManager.Instace().feed.idtype.equals("blogid")) {
						// 进入日志
						handler.sendEmptyMessage(10);
					} else if (DataManager.Instace().feed.idtype.equals("doid")) {					
						handler.sendEmptyMessage(11);
					}else {
						handler.sendEmptyMessage(12);
					}
				}		
			}
		});
	}

	private void setAdapter() {
		// TODO Auto-generated method stub		
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
	}
	Handler wordHandler=new Handler();	
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            wordHandler.postDelayed(this, 1000 * 40);// 间隔120秒
        }
        void update() {
            //刷新msg的内容
        	words=SelectWord();
        	if(wordScroll.getVisibility()==View.VISIBLE){
        		
        	}else{      		
        		Words newWords=SelectWord();     		
        		if(words.key.equals("")&&words.key==null){
        			Log.e("word=====", "空空");
        			words=localWordsHelper.SearchWordRandom();
        			addword.setVisibility(View.VISIBLE);
        			hasadded.setVisibility(View.GONE);       			
        			if(words.key!=null&&words.def!=null){
        				if(words.pron==null||words.pron.equals("")){
                			setWord(words.key);
                		}else{
                			setWord(words.key+"   ["+words.pron+"]");
                		}
                	}else{
                		wordlayout.setVisibility(View.GONE);
                	}
        		}else{
        			if(words.key.equals(newWords.key)){
            			//在单词库中随机出现一个词
            			words=localWordsHelper.SearchWordRandom();
            			addword.setVisibility(View.VISIBLE);
            			hasadded.setVisibility(View.GONE);       			
            			if(words.key!=null&&words.def!=null){
            				if(words.pron==null||words.pron.equals("")){
                    			setWord(words.key);
                    		}else{
                    			setWord(words.key+"   ["+words.pron+"]");
                    		}
                    	}else{
                    		wordlayout.setVisibility(View.GONE);
                    	}
            		}else{
            			//在生词本中随机一个词
            			words=SelectWord();
            			addword.setVisibility(View.GONE);
            			hasadded.setVisibility(View.VISIBLE);
                    	if(words.key!=null&&words.def!=null){
                    		if(words.pron==null||words.pron.equals("")){
                    			setWord(words.key);
                    		}else{
                    			setWord(words.key+"   ["+words.pron+"]");
                    		}
                    	}else{
                    		wordlayout.setVisibility(View.GONE);
                    	}
            		}
        		}
        			
        	}    	      
        }
    }; 
	private void initWidget() {
		tvuserName = (TextView) findViewById(R.id.username);
		publish_mood_btn = (Button) findViewById(R.id.publish_mood_btn);	
		refresh_btn = (Button) findViewById(R.id.refresh_btn);
		listView = (PullToRefreshListView) findViewById(R.id.newsListView);
		wordlayout=(RelativeLayout)findViewById(R.id.wordlayout);
		wordScroll=(ScrollView)findViewById(R.id.wordScroll);
		adapter = new FeedListAdapter(mContext);
		wordTextView = (TextView) findViewById(R.id.autoscrolltextview);
		worddef = (TextView) findViewById(R.id.worddef);
		wordSentence= (TextView) findViewById(R.id.wordsentence);
		wordHandler.postDelayed(runnable, 1000 * 1);//刷新单词
		wordsOpen=(ImageView)findViewById(R.id.wordsopen);
		wordsUp=(ImageView)findViewById(R.id.wordsup);
		wordSpeaker=(ImageView)findViewById(R.id.wordspeaker);
		addword=(ImageView)findViewById(R.id.wordsadd);
		hasadded=(ImageView)findViewById(R.id.hasadded);
		if(words.audio!=null){
			wordSpeaker.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Player player = new Player(mContext, null, null);
					String url = words.audio;
					player.playUrl(url);
				}
			});
		}else{
			wordSpeaker.setEnabled(false);
		}
		addword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (words != null) {
					words.forUser = AccountManager.Instace(mContext).userId;
					words.fromApp = mContext.getResources()
							.getString(R.string.app_name);
					words.status = 1;
					if (DatabaseManager.Instace(mContext).getWordsHelper()
							.insertWordsDataForSingle(words) > 0) {
						Toast.makeText(mContext, "成功加入生词本！", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext, "生词本中已存在该单词！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		wordsOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wordsOpen.setVisibility(View.GONE);
				wordsUp.setVisibility(View.VISIBLE);
				wordScroll.setVisibility(View.VISIBLE);
				worddef.setText(words.def);
				wordSentences=localWordsHelper.SearchWordSentences(words.key);
				if(wordSentences!=null&&wordSentences.size()>0){
					if(wordSentences.size()==1){
						wordSentence.setText(Html.fromHtml(wordSentences.get(0).orig)+"\n"+Html.fromHtml(wordSentences.get(0).trans));
					}else if(wordSentences.size()==2){
						wordSentence.setText(Html.fromHtml(wordSentences.get(0).orig)+"\n"+Html.fromHtml(wordSentences.get(0).trans)
								+"\n"+Html.fromHtml(wordSentences.get(1).orig)+"\n"+Html.fromHtml(wordSentences.get(1).trans));
					}else if(wordSentences.size()==3){
						wordSentence.setText(Html.fromHtml(wordSentences.get(0).orig)+"\n"+Html.fromHtml(wordSentences.get(0).trans)
								+"\n"+Html.fromHtml(wordSentences.get(1).orig)+"\n"+Html.fromHtml(wordSentences.get(1).trans)+"\n"+
								Html.fromHtml(wordSentences.get(2).orig)+"\n"+Html.fromHtml(wordSentences.get(2).trans));
					}			
				}else{
					wordSentence.setText("暂无");
				}
				
			}
		});	
		wordsUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wordsUp.setVisibility(View.GONE);
				wordsOpen.setVisibility(View.VISIBLE);
				wordScroll.setVisibility(View.GONE);
			}
		});
		wordTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				intent.setClass(mContext, WordListActivity.class);
				startActivity(intent);
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				isRerefreshBoolean = true;
			}
		});
		publish_mood_btn.setOnClickListener(new View.OnClickListener() {
			// 发表新鲜事
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,MainTopLeftDailog.class);			
				startActivity(intent);
			}
		});
		refresh_btn.setOnClickListener(new View.OnClickListener() {
			// 刷新页面
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listView.setRefresh(new OnRefreshListener() {
					
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(0);
						isRerefreshBoolean = true;
					}
				});
				
			}
		});
	}

	private void setWord(String word) {
		// TODO Auto-generated method stub	
		wordTextView.setText(word);
	}
	private Words SelectWord(){
		Words words=DatabaseManager
		.Instace(mContext)
		.getWordsHelper().getWord(AccountManager.Instace(mContext).userId);
		return words;	
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
				handler.sendEmptyMessage(1);
				
				break;
			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace().asynGetResponse(
						new RequestFeedList(
								AccountManager.Instace(mContext).userId,
								currPages, "2"), new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseFeedList res = (ResponseFeedList) response;
								if (res.result.equals("391")) {
									list.clear();
									adapter.clearList();
									list.addAll(res.list);
									adapter.addList(res.list);
									handler.sendEmptyMessage(15);//加到本地数据库
								} else if (res.result.equals("392")) {
									isLastPage = true;
								}
								curPage += 1;
								currPages = String.valueOf(curPage);
								handler.sendEmptyMessage(4);
							}

						});
				break;
		
			case 4:
				
				adapter.notifyDataSetChanged();
				if (isRerefreshBoolean) {
					listView.onRefreshComplete();
				}
				break;
			case 10:
				// 跳到这个日志
				ClientNetwork.Instace().asynGetResponse(
						new RequestFindBlogById(DataManager.Instace().feed.id),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseFindBlogById res = (ResponseFindBlogById) response;
								if (res.result.equals("251")) {
									DataManager.Instace().blogContent = res.blogContent;
									DataManager.Instace().blogContent.username = DataManager
											.Instace().feed.username;
									Intent intent = new Intent();
									intent.setClass(mContext,
											BlogActivity.class);
									startActivity(intent);
								} else if (res.result.equals("250")) {
									handler.sendEmptyMessage(13);
								}
								

							}

						});

				break;
			case 11:
				// 跳到这个状态
				
				Intent intent1 = new Intent();
				intent1.putExtra("doid", DataManager.Instace().feed.id);
				intent1.setClass(mContext, DoingFromHomeActivity.class);
				startActivity(intent1);
				break;
			case 12:
				// 跳到这个人的个人主页
		
				Intent intent2 = new Intent();
				intent2.putExtra("fanuid", DataManager.Instace().feed.uid);
				intent2.setClass(mContext, PersonalHomeActivity.class);
				startActivity(intent2);
				break;
			case 13:
				Toast toast = Toast.makeText(getApplicationContext(), "日志不存在",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			case 15:
				//保存到本地
				long count=DatabaseManager.Instace(mContext).getFeedInfoHelper().insertFeedInfo(list);
				Log.e("Home  count",""+count);
				break;
			default:
				break;
			}
		}

	};


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
				// listView.addFooterView(footer);
				if (!isLastPage) {
					handler.sendEmptyMessage(1);
					isRerefreshBoolean = false;
				}
			}
			System.out.println("新鲜事滑动到底部");
			break;

		}
	}
}
