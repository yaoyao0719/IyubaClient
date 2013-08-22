/**
 * 
 */
package com.iyuba.iyubaclient;

import java.io.IOException;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.sqlite.helper.LocalWordsHelper;
import com.iyuba.iyubaclient.widget.TextPage.TextPageSelectTextCallBack;
import com.iyuba.iyubaclient.word.procotol.RequestTranslate;
import com.iyuba.iyubaclient.word.procotol.ResponseTranslate;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yao
 * 
 */
public class WordActivity extends THSActivity {

	private String key;
	private ImageView speak,close;
	private TextView keyText,pronText,defText;
	private Button   addWordsBtn;
	private Words currSelectWord;
	LocalWordsHelper localWordsHelper;
	private ProgressBar loadingBar;

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		Intent intent = getIntent();
		key = intent.getStringExtra("selectedText");
		localWordsHelper = new LocalWordsHelper(mContext);
		initWidget();
		handler.sendEmptyMessage(7);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		close = (ImageView) findViewById(R.id.word_close);
		speak = (ImageView) findViewById(R.id.wordSpeaker);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		keyText = (TextView) findViewById(R.id.wordKey);
		pronText = (TextView) findViewById(R.id.wordPron);
		defText = (TextView) findViewById(R.id.wordDef);
		addWordsBtn = (Button) findViewById(R.id.add_word);
		addWordsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (currSelectWord != null) {
					currSelectWord.forUser = AccountManager.Instace(mContext).userId;
					if (DatabaseManager.Instace(mContext).getWordsHelper()
							.insertWordsDataForSingle(currSelectWord) > 0) {
						Toast.makeText(mContext, "成功加入生词本！", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext, "生词本中已存在该单词！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
		loadingBar = (ProgressBar) findViewById(R.id.progressBar_get_Interperatatior);
	

	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			
		
			case 4:
				WordActivity.this.finish();
				break;
			case 5: // 填充单词释义
				keyText.setText(currSelectWord.key);
				pronText.setText("[" + currSelectWord.pron + "]");
				defText.setText(currSelectWord.def);
				loadingBar.setVisibility(View.GONE);
				addWordsBtn.setEnabled(true);
				break;
			case 6: // 单词无释义提示
				Toast.makeText(mContext, "没有这个单词", Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(4);
				
				break;
			case 7:
				// 从网络获取单词释义
				Log.e("key===", key);
				loadingBar.setVisibility(View.VISIBLE);
				addWordsBtn.setEnabled(false);
				if (localWordsHelper.isLocalWord(key)) {
					currSelectWord = localWordsHelper.SearchWord(key);
					handler.sendEmptyMessage(8);
					if (currSelectWord != null) {
						currSelectWord.forUser = AccountManager
								.Instace(mContext).userId;
						currSelectWord.fromApp = mContext.getResources()
								.getString(R.string.app_name);
						currSelectWord.status = 1;
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(6);
					}

				} else {
					ClientNetwork.Instace().asynGetResponse(
							new RequestTranslate(key),
							new IResponseReceiver() {

								@Override
								public void onResponse(BaseHttpRequest request,
										BaseHttpResponse response) {
									// TODO Auto-generated method stub
									ResponseTranslate responseTranslate = (ResponseTranslate) response;
									currSelectWord = responseTranslate.word;
									if (currSelectWord != null) {
										currSelectWord.forUser = AccountManager
												.Instace(mContext).userId;
										currSelectWord.fromApp = mContext
												.getResources().getString(
														R.string.app_name);
										currSelectWord.status = 1;
										handler.sendEmptyMessage(5);
									} else {
										handler.sendEmptyMessage(6);
									}
								}
							}, null, null);
				}

				break;
			case 8:
				if (currSelectWord.audio != null && currSelectWord.audio.length() != 0) {
					speak.setVisibility(View.VISIBLE);
				} else {
					speak.setVisibility(View.GONE);
				}
				speak.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.e("speak","speak");
						final MediaPlayer player = new MediaPlayer();
						String url = currSelectWord.audio;
						try {
							player.setDataSource(url);
							new Thread(){
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									super.run();
									try {
										player.prepare();
										player.start();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}// prepare之后自动播放
								}}.start();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				break;
			default:
				break;
			}
		}

	};
	

}
