/**
 * 
 */
package com.iyuba.iyubaclient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;



import com.iyuba.iyubaclient.adapter.ChatMessage;
import com.iyuba.iyubaclient.adapter.ChattingAdapter;
import com.iyuba.iyubaclient.adapter.EmotionAdapter;
import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.message.procotol.RequestMessageLetterContentList;
import com.iyuba.iyubaclient.message.procotol.RequestMessageLetterList;
import com.iyuba.iyubaclient.message.procotol.ResponseMessageLetterContentList;
import com.iyuba.iyubaclient.message.procotol.ResponseMessageLetterList;
import com.iyuba.iyubaclient.message.procotol.ResponseSendMessageLetter;
import com.iyuba.iyubaclient.message.procotol.RequestSendMessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetterContent;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author yao
 *
 */
public class MessageLetterContentActivity extends THSActivity{

	private ChattingAdapter adapter;
	private ArrayList<MessageLetterContent> list=new ArrayList<MessageLetterContent>();
	private ListView chatHistoryLv;
	private Button sendBtn,back;
	private EditText textEditor;
	private Button showBtn;
	private RelativeLayout rlShow;
	private CustomDialog waitingDialog;
	private String currPages="1";
	private int curPage=1;
	private TextView friendName;
	private GridView emotion_GridView;
	private EmotionAdapter emoAdapter;
	private String sendStr;
	private String friendid,search,mutualAttention;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting);
		chatHistoryLv = (ListView) findViewById(R.id.chatting_history_lv);
		Intent intent=getIntent();
		friendid=intent.getStringExtra("friendid");
		search=intent.getStringExtra("search");
		mutualAttention=intent.getStringExtra("mutualAttention");
		initWidget();
		waitingDialog=waitingDialog();
		initMessages();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		sendBtn = (Button) findViewById(R.id.send_button);
		showBtn = (Button) findViewById(R.id.show);
		back=(Button)findViewById(R.id.messageletterContent_back_btn);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rlShow = (RelativeLayout)findViewById(R.id.rl_show);
		textEditor = (EditText) findViewById(R.id.text_editor);
		emotion_GridView=(GridView)rlShow.findViewById(R.id.blog_sendmsg_gvemotion);
	    //emotion_GridView=createGridView();
		emoAdapter=new EmotionAdapter(mContext);
		friendName=(TextView)findViewById(R.id.messagelettercontent_friendname);
		if(friendid!=null){
			if(search!=null&&search.equals("search")){
				friendName.setText(DataManager.Instace().searchItem.username);
			}else{
				friendName.setText(DataManager.Instace().mutualAttention.fusername);
			}
			
		}else{
			friendName.setText(DataManager.Instace().letter.name);
		}	
		sendBtn.setOnClickListener(l);
		showBtn.setOnClickListener(l);
		textEditor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(rlShow.getVisibility() == View.VISIBLE){
					rlShow.setVisibility(View.GONE);
				}
			}
		});
		
	}
	// 设置adapter
	private void setAdapterForThis() {		
		adapter = new ChattingAdapter(this,AccountManager.Instace(mContext).userId);
		chatHistoryLv.setAdapter(adapter);
	}

	// 为listView添加数据
	private void initMessages() {
		setAdapterForThis();
		handler.sendEmptyMessage(0);		
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage=1;
				currPages=String.valueOf(curPage);
				adapter.clearList();
				list.clear();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;

			case 1:
				//联网获取日志列表，滑到底部点击更多进行加载
				if(friendid!=null){
					ClientNetwork.Instace().asynGetResponse(new RequestMessageLetterContentList(AccountManager.Instace(mContext).userId,friendid) , new IResponseReceiver(){

						@Override
						public void onResponse(BaseHttpRequest request,
								BaseHttpResponse response) {
							// TODO Auto-generated method stub
							ResponseMessageLetterContentList res=(ResponseMessageLetterContentList)response;
							if(res.result.equals("631")){
								list.addAll(res.list);
								adapter.addList(res.list);	
						
							}else{						
							}
							curPage+=1;
							currPages=String.valueOf(curPage);
							handler.sendEmptyMessage(4);
						}
						
					});
				}else{
				ClientNetwork.Instace().asynGetResponse(new RequestMessageLetterContentList(AccountManager.Instace(mContext).userId,DataManager.Instace().letter.friendid) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseMessageLetterContentList res=(ResponseMessageLetterContentList)response;
						if(res.result.equals("631")){
							list.addAll(res.list);
							adapter.addList(res.list);	
					
						}else{						
						}
						curPage+=1;
						currPages=String.valueOf(curPage);
						handler.sendEmptyMessage(4);
					}
					
				});
				}
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler.sendEmptyMessage(3);
				adapter.notifyDataSetChanged();
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent=new Intent();
				intent.setClass(mContext,MessageLetterContentActivity.class);
				startActivity(intent);
				break;
			case 10:
				//发送私信
				if(search!=null&&search.equals("search")){
					ClientNetwork.Instace().asynGetResponse(new RequestSendMessageLetter(AccountManager.Instace(mContext).userId,DataManager.Instace().searchItem.username,sendStr) , new IResponseReceiver(){

						@Override
						public void onResponse(BaseHttpRequest request,
								BaseHttpResponse response) {
							// TODO Auto-generated method stub
							ResponseSendMessageLetter res=(ResponseSendMessageLetter)response;
							if(res.result.equals("611")){
						        System.out.println("fasongchenggong 111");
							}else{						
							}			
							handler.sendEmptyMessage(4);
						}
						
					});
				}else if(mutualAttention!=null&&mutualAttention.equals("mutualAttention")){
					ClientNetwork.Instace().asynGetResponse(new RequestSendMessageLetter(AccountManager.Instace(mContext).userId,DataManager.Instace().mutualAttention.fusername,sendStr) , new IResponseReceiver(){

						@Override
						public void onResponse(BaseHttpRequest request,
								BaseHttpResponse response) {
							// TODO Auto-generated method stub
							ResponseSendMessageLetter res=(ResponseSendMessageLetter)response;
							if(res.result.equals("611")){
						        System.out.println("fasongchenggong 111");
							}else{						
							}			
							handler.sendEmptyMessage(4);
						}
						
					});
				}else{
					ClientNetwork.Instace().asynGetResponse(new RequestSendMessageLetter(AccountManager.Instace(mContext).userId,DataManager.Instace().letter.name,sendStr) , new IResponseReceiver(){

						@Override
						public void onResponse(BaseHttpRequest request,
								BaseHttpResponse response) {
							// TODO Auto-generated method stub
							ResponseSendMessageLetter res=(ResponseSendMessageLetter)response;
							if(res.result.equals("611")){
						        System.out.println("fasongchenggong 111");
							}else{						
							}			
							handler.sendEmptyMessage(4);
						}
						
					});
				}
				
				break;
			default:
				break;
			}
		}
		
	};
	/**
	 * 按键时监听
	 */
	private int[] imageIds = new int[30];
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == sendBtn.getId()) {
				String str = textEditor.getText().toString();
				//String sendStr;
				if (str != null
						&& (sendStr = str.trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")
								.replaceAll("\f", "")) != "") {
					sendMessage(sendStr);
				}
				textEditor.setText("");
			}
			if(v.getId() == showBtn.getId()){
				if(rlShow.getVisibility() == View.GONE){
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
					hideSoftInputFromWindow(MessageLetterContentActivity.this.getCurrentFocus().getWindowToken(), 
							InputMethodManager.HIDE_NOT_ALWAYS);  
					rlShow.setVisibility(View.VISIBLE);
					initEmotion();
				    emotion_GridView.setVisibility(View.VISIBLE);
					emotion_GridView.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeResource(getResources(), imageIds[arg2 % imageIds.length]);
							ImageSpan imageSpan = new ImageSpan(MessageLetterContentActivity.this, bitmap);
							String str = null;
							str = "image"+arg2;
							SpannableString spannableString = new SpannableString(str);
							String str1=null;
							str1=Emotion.express[arg2];
							System.out.println("spannableString===="+spannableString);
							SpannableString spannableString1 = new SpannableString(str1);
							if(str.length()==6){
								spannableString.setSpan(imageSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}else if(str.length()==7){
								spannableString.setSpan(imageSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}else{
								spannableString.setSpan(imageSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}	
							System.out.println("1111   spannableString===="+spannableString);
														
							textEditor.append(spannableString1);		
							String string=textEditor.getText().toString();
							System.out.println("1111   ===="+string);
						}
					});
				}else{
					rlShow.setVisibility(View.GONE);
				}
			}
		}

		// 模拟发送消息
		private void sendMessage(String sendStr) {
			handler.sendEmptyMessage(10);
			MessageLetterContent letterContent=new MessageLetterContent(sendStr);
			letterContent.setDirection(1);
			letterContent.setAuthorid(AccountManager.Instace(mContext).userId);
			letterContent.setDateline(String.valueOf(System.currentTimeMillis()/1000));
			list.add(letterContent);
			System.out.println("listdaxiao  "+list.size());
			System.out.println("listdaxiao  "+list.get(list.size()-1).message);
			adapter.clearList();
			adapter.addList(list);
		}

	};
	private void initEmotion(){
		List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
		//生成107个表情的id，封装
		for(int i = 0; i < 30; i++){
			try {
				Field field = R.drawable.class.getDeclaredField("image" +( i+1));
				int resourceId = Integer.parseInt(field.get(null).toString());
				System.out.println("resourceId=="+resourceId);
				System.out.println("field=="+field);
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
	        Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.team_layout_single_expression_cell, new String[]{"image"}, new int[]{R.id.image});
		emotion_GridView.setAdapter(simpleAdapter);
		emotion_GridView.setNumColumns(7);
	}
	/**
	 * 生成一个表情对话框中的gridview
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(this);
		List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
		//生成107个表情的id，封装
		for(int i = 0; i < 30; i++){
			try {
				Field field = R.drawable.class.getDeclaredField("image" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				System.out.println("resourceId=="+resourceId);
				System.out.println("field=="+field);
				imageIds[i] = resourceId;
				/*if(i<10){
					Field field = R.drawable.class.getDeclaredField("image" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					System.out.println("resourceId=="+resourceId);
					imageIds[i] = resourceId;
				}else{
					Field field = R.drawable.class.getDeclaredField("image" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					imageIds[i] = resourceId;
				}*/
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
	        Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.team_layout_single_expression_cell, new String[]{"image"}, new int[]{R.id.image});
		view.setAdapter(simpleAdapter);
		view.setNumColumns(5);
		//view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		System.out.println("listItem==="+listItems.get(5));
		System.out.println("listItem==="+listItems.size());
		System.out.println("view==="+view);
		return view;
	}
	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder
				.setContentView(layout).create();
		return cDialog;
	}
}
