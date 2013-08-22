/**
 * 
 */
package com.iyuba.iyubaclient;

import java.io.InputStream;
import java.net.URL;
import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DatabaseManager;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.sqlite.helper.LocalWordsHelper;
import com.iyuba.iyubaclient.widget.TextPage.SubtitleSynView;
import com.iyuba.iyubaclient.widget.TextPage.TextPageSelectTextCallBack;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.iyuba.iyubaclient.word.procotol.RequestTranslate;
import com.iyuba.iyubaclient.word.procotol.ResponseTranslate;

/**
 * @author yao 显示日志内容
 */
public class BlogActivity extends THSActivity {

	private Button backButton, blogComments;
	private String message = "";
	private TextView blogSubject, blogTitle, blogTime;
	private SubtitleSynView test;
	private String subject = "";
	int i = 0;
	private TextPageSelectTextCallBack tpstcb;
	private CustomDialog waitingDialog;
	// ------------------------单词释义相关控件
	private View translationPanel; // 取词面板
	private TextView keyText, pronText, defText;
	private Button addWordsBtn;
	private ProgressBar loadingBar;
	private boolean isTranslationPanelShow = false;
	private Words currSelectWord;
	LocalWordsHelper localWordsHelper;
	Spanned sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtiy_blog);
		waitingDialog = waitingDialog();
		message = DataManager.Instace().blogContent.message;
		subject = DataManager.Instace().blogContent.subject;
		test = (SubtitleSynView) findViewById(R.id.test);
		localWordsHelper = new LocalWordsHelper(mContext);
		initTranslationPanel();
		initWidget();
		handler.sendEmptyMessage(2);
		Thread thread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				sp = Html.fromHtml(message, new Html.ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						InputStream is = null;
						try {
							is = (InputStream) new URL(source).getContent();
							Drawable d = Drawable.createFromStream(is, "src");
							d.setBounds(0, 0, d.getIntrinsicWidth(),
									d.getIntrinsicHeight());
							is.close();
							return d;
						} catch (Exception e) {
							return null;
						}
					}
				}, null);
				handler.sendEmptyMessage(1);
			}
		};
		thread.start();

	}

	private void initTranslationPanel() {
		// TODO Auto-generated method stub
		keyText = (TextView) findViewById(R.id.word_key);
		pronText = (TextView) findViewById(R.id.word_pron);
		defText = (TextView) findViewById(R.id.word_def);
		addWordsBtn = (Button) findViewById(R.id.add_word_translate);
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

	private void initWidget() {
		// TODO Auto-generated method stub
		backButton = (Button) findViewById(R.id.blog_back_btn);
		blogSubject = (TextView) findViewById(R.id.blog_subject);
		blogTitle = (TextView) findViewById(R.id.blog_title);
		blogTime = (TextView) findViewById(R.id.blog_time);
		blogComments = (Button) findViewById(R.id.blogcomments);
		translationPanel = (LinearLayout) findViewById(R.id.linearLayout_interpertatior);
		translationPanel.setVisibility(View.GONE);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlogActivity.this.finish();
			}
		});
		blogComments.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, BlogCommentsActivity.class);
				mContext.startActivity(intent);
			}
		});
		blogSubject.setText(subject);
		blogTime.setText(DateFormat.format(
				"yyyy-MM-dd hh:mm:ss",
				Long.parseLong(DataManager.Instace().blogContent.dateline) * 1000));
		if (DataManager.Instace().blogContent.username == null
				|| DataManager.Instace().blogContent.username.equals("")) {
			blogTitle.setText("日志");
		} else {
			blogTitle.setText(DataManager.Instace().blogContent.username
					+ "的日志");
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				test.setSubtitleSum(sp);
				handler.sendEmptyMessage(3);
				test.setTpstcb(new TextPageSelectTextCallBack() {

					@Override
					public void selectTextEvent(String selectText) {
						// TODO Auto-generated method stub
						
						Intent intent =new Intent (mContext,WordActivity.class);
						intent.putExtra("selectedText",selectText );
						startActivity(intent);
					}
				});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 33:
				translationPanel.setVisibility(View.VISIBLE);
				break;
			case 4:
				translationPanel.setVisibility(View.GONE);
				isTranslationPanelShow = false;
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
				if (isTranslationPanelShow) {
					handler.sendEmptyMessage(4);
				}
				break;
			case 7:
				// 从网络获取单词释义
				String selectText = String.valueOf(msg.obj);
				loadingBar.setVisibility(View.VISIBLE);
				addWordsBtn.setEnabled(false);
				if (localWordsHelper.isLocalWord(selectText)) {
					currSelectWord = localWordsHelper.SearchWord(selectText);
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
							new RequestTranslate(selectText),
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

			default:
				break;
			}
		}

	};

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}
}
