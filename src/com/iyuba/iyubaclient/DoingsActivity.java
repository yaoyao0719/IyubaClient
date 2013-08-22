/**
 * 
 */
package com.iyuba.iyubaclient;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.adapter.DoingsCommentAdapter;
import com.iyuba.iyubaclient.adapter.DoingsListAdapter;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.entity.ExpressionUtil;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.procotol.ResponseDoingSendComments;
import com.iyuba.iyubaclient.procotol.RequestDoingSendComments;
import com.iyuba.iyubaclient.procotol.RequestBlogInfo;
import com.iyuba.iyubaclient.procotol.RequestDoingsCommentInfo;
import com.iyuba.iyubaclient.procotol.ResponseBlogInfo;
import com.iyuba.iyubaclient.procotol.ResponseDoingsCommentInfo;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.DoingsCommentInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetterContent;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.iyuba.iyubaclient.widget.listview.DynamicListView;
import com.iyuba.iyubaclient.widget.listview.DynamicListView.DynamicListViewListener;
import com.iyuba.iyubaclient.feed.procotol.RequestDoingById;
import com.iyuba.iyubaclient.feed.procotol.ResponseDoingById;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author yao 状态（心情）详细展示
 */
public class DoingsActivity extends THSActivity implements OnScrollListener {

	private Button backButton;
	private ImageView doings_userPortrait;
	private TextView doings_username, doings_time, doings_message,
			doings_replyNum, doings_shareNum;
	private ListView doings_commentlist;
	private DoingsCommentAdapter doingsCommentListAdapter;
	private CustomDialog waitingDialog;
	private String currPages = "1";
	private int curPage = 1;
	private ArrayList<DoingsCommentInfo> doingsCommentArrayList = new ArrayList<DoingsCommentInfo>();

	private DoingsInfo doingsInfo = new DoingsInfo();
	private String userImgSrc, doingsImgsrc;
	private Bitmap bm=null;
	private View footer;
	Drawable drawable = null;
	private Boolean isLastPage = false;
	private EditText editText;
	private Button emotionButton, sendButton;
	private GridView emotion_GridView;
	private RelativeLayout emotionShow;
	private String sendStr;
	DoingsCommentInfo item = new DoingsCommentInfo();
	private View headView;
	private LayoutInflater layoutInflater;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doings);
		waitingDialog = waitingDialog();
		doingsInfo = DataManager.Instace().doingsInfo;
		initWidget();
		doingsCommentListAdapter = new DoingsCommentAdapter(mContext);
		doings_commentlist.addHeaderView(headView);
		doings_commentlist.setAdapter(doingsCommentListAdapter);
		doings_commentlist.setOnScrollListener(this);
		handler.sendEmptyMessage(10);// 下载图片
		handler.sendEmptyMessage(0);
	//	handler.sendEmptyMessage(10);// 下载图片
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		doings_commentlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {

				} else {
					editText.findFocus();
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.showSoftInput(editText, 0);
					editText.setText("回复 "
							+ doingsCommentArrayList.get(arg2 - 1).username
							+ ":");
					Editable etext = editText.getText();
					int position = etext.length();
					Selection.setSelection(etext, position);
					item.grade = doingsCommentArrayList.get(arg2 - 1).grade;
				}
			}
		});
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		backButton = (Button) findViewById(R.id.doings_back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DoingsActivity.this.finish();
			}
		});
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headView = layoutInflater.inflate(R.layout.doing_headview, null);
		doings_userPortrait = (ImageView) headView
				.findViewById(R.id.doings_userPortrait);
		doings_username = (TextView) headView
				.findViewById(R.id.doings_username);
		doings_time = (TextView) headView.findViewById(R.id.doings_time);
		doings_message = (TextView) headView.findViewById(R.id.doings_message);
		doings_replyNum = (TextView) headView
				.findViewById(R.id.doings_replynum);

		String zhengze = "image[0-9]{2}|image[0-9]";
		Emotion emotion = new Emotion();
		doingsInfo.message = emotion.replace(doingsInfo.message);
		try {
			SpannableString spannableString = ExpressionUtil
					.getExpressionString(mContext, doingsInfo.message, zhengze);
			doings_message.setText(spannableString);
			handler.sendEmptyMessage(100);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		doings_username.setText(doingsInfo.username);	
		doings_replyNum.setText(doingsInfo.replynum);
		doings_time.setText(DateFormat.format("yyyy-MM-dd hh:ss:mm",
				Long.parseLong(doingsInfo.dateline) * 1000));
		handler.sendEmptyMessage(100);
		doings_commentlist = (ListView) findViewById(R.id.doings_commentlist);
		emotionButton = (Button) findViewById(R.id.doingcommment_emotion);
		sendButton = (Button) findViewById(R.id.doingcomment_send);
		editText = (EditText) findViewById(R.id.doingcomment_edit);
		emotionShow = (RelativeLayout) findViewById(R.id.emotion_show);
		emotion_GridView = (GridView) emotionShow
				.findViewById(R.id.blog_sendmsg_gvemotion);
		sendButton.setOnClickListener(l);
		emotionButton.setOnClickListener(l);
		editText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (emotionShow.getVisibility() == View.VISIBLE) {
					emotionShow.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 按键时监听
	 */
	private int[] imageIds = new int[30];
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == sendButton.getId()) {
				String str = editText.getText().toString();
				// String sendStr;
				if (str != null
						&& (sendStr = str.trim().replaceAll("\r", "")
								.replaceAll("\t", "").replaceAll("\n", "")
								.replaceAll("\f", "")) != "") {
					sendMessage(sendStr);
				}
				editText.setText("");
			}
			if (v.getId() == emotionButton.getId()) {
				if (emotionShow.getVisibility() == View.GONE) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(DoingsActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					emotionShow.setVisibility(View.VISIBLE);
					initEmotion();
					emotion_GridView.setVisibility(View.VISIBLE);
					emotion_GridView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									Bitmap bitmap = null;
									bitmap = BitmapFactory.decodeResource(
											getResources(), imageIds[arg2
													% imageIds.length]);
									ImageSpan imageSpan = new ImageSpan(
											DoingsActivity.this, bitmap);
									String str = null;
									str = "image" + arg2;
									SpannableString spannableString = new SpannableString(
											str);
									System.out.println("spannableString===="
											+ spannableString);
									if (str.length() == 6) {
										spannableString
												.setSpan(
														imageSpan,
														0,
														6,
														Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									} else if (str.length() == 7) {
										spannableString
												.setSpan(
														imageSpan,
														0,
														7,
														Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									} else {
										spannableString
												.setSpan(
														imageSpan,
														0,
														5,
														Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
									System.out
											.println("1111   spannableString===="
													+ spannableString);

									editText.append(spannableString);
									String string = editText.getText()
											.toString();
									System.out.println("1111   ====" + string);
								}
							});
				} else {
					emotionShow.setVisibility(View.GONE);
				}
			}
		}

		// 模拟发送消息
		private void sendMessage(String sendStr) {
			item.dateline = String.valueOf(System.currentTimeMillis());
			item.message = sendStr;
			item.uid = AccountManager.Instace(mContext).userId;
			item.username = AccountManager.Instace(mContext).userName;
			item.upid = "0";
			handler.sendEmptyMessage(20);
			doingsCommentArrayList.add(item);
			doingsCommentListAdapter.clearList();
			doingsCommentListAdapter.addList(doingsCommentArrayList);
			handler.sendEmptyMessage(100);
		}

	};

	private void initEmotion() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
		for (int i = 0; i < 30; i++) {
			try {
				Field field = R.drawable.class.getDeclaredField("image"
						+ (i + 1));
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.team_layout_single_expression_cell,
				new String[] { "image" }, new int[] { R.id.image });
		emotion_GridView.setAdapter(simpleAdapter);
		emotion_GridView.setNumColumns(7);
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
				doingsCommentListAdapter.clearList();
				handler.sendEmptyMessage(100);
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				ClientNetwork.Instace()
						.asynGetResponse(
								new RequestDoingsCommentInfo(doingsInfo.doid,
										currPages), new IResponseReceiver() {

									@Override
									public void onResponse(
											BaseHttpRequest request,
											BaseHttpResponse response) {
										// TODO Auto-generated method stub
										ResponseDoingsCommentInfo responseDoingsCommentInfo = (ResponseDoingsCommentInfo) response;
										if (responseDoingsCommentInfo.result
												.equals("311")) {
											doingsCommentArrayList
													.addAll(responseDoingsCommentInfo.doingsCommentlist);
											doingsCommentListAdapter
													.addList(responseDoingsCommentInfo.doingsCommentlist);
											handler.sendEmptyMessage(100);
											if (Integer
													.parseInt(responseDoingsCommentInfo.counts) < doingsCommentArrayList
													.size()
													|| Integer
															.parseInt(responseDoingsCommentInfo.counts) == doingsCommentArrayList
															.size()) {
												isLastPage = true;
											}
										} else {
										}
										curPage += 1;
										currPages = String.valueOf(curPage);
										handler.sendEmptyMessage(4);
									}

								});
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler.sendEmptyMessage(3);
				handler.sendEmptyMessage(100);
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.setClass(mContext, BlogActivity.class);
				startActivity(intent);
				break;
			case 10:
				DownLoadUserImg();
				handler.sendEmptyMessage(100);
				break;
			case 20:
				// 发送评论
				ClientNetwork.Instace().asynGetResponse(
						new RequestDoingSendComments(item, doingsInfo.doid),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseDoingSendComments res = (ResponseDoingSendComments) response;
								if (res.result.equals("361")) {
									System.out.println("评论发送成功！！！！");
								} else {
								}
								handler.sendEmptyMessage(4);
							}

						});
				break;
			case 22:
				Log.e("bm===",""+bm);
				if(bm==null){
					doings_userPortrait.setBackgroundResource(R.drawable.defaultavatar);
					handler.sendEmptyMessage(100);
				}else{
					doings_userPortrait.setImageBitmap(bm);			
					handler.sendEmptyMessage(100);
				}
				break;

			case 100:
				doingsCommentListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {

					File file = new File(Constant.APP_DATA_PATH
							+ Constant.SDCARD_PORTEAIT_PATH + doingsInfo.uid
							+ ".jpeg");
					if (file.exists()) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						bm = BitmapFactory.decodeFile(Constant.APP_DATA_PATH
								+ Constant.SDCARD_PORTEAIT_PATH
								+ doingsInfo.uid + ".jpeg", options);
						handler.sendEmptyMessage(22);
					} else {
						Log.e("bm","本地不存在！！！");
						DownLoadManager.Instace(mContext).downLoadImage(
								Constant.IMAGE_DOWN_PATH + doingsInfo.uid,
								doingsInfo.uid, new DownLoadCallBack() {
									@Override
									public void downLoadSuccess(Bitmap image) {
										// TODO Auto-generated method stub
										bm = image;
										handler.sendEmptyMessage(22);
									}

									@Override
									public void downLoadFaild(String error) {
										// TODO Auto-generated method stub
									}
								}, true);
					}
				}
			}

		};
		t.start();
	}

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}

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
				// doings_commentlist.addFooterView(footer);
				handler.sendEmptyMessage(1);
				System.out.println("滑动到底部！！！！！");
			}
			break;

		}
	}

}
