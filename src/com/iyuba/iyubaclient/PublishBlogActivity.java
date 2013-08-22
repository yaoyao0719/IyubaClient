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
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.adapter.EmotionAdapter;
import com.iyuba.iyubaclient.entity.SendBlog;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.procotol.RequestSendBlog;
import com.iyuba.iyubaclient.procotol.ResponseSendBlog;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * @author yao
 *
 */
public class PublishBlogActivity extends THSActivity{

	private Button backButton,publishblog,blogemotion,blogkeyboard;
	private EditText blog_subject,blog_message;//标题、正文
	private String subject,message;
	private SendBlog sendBlog;
	private GridView emotion_GridView;
	private EmotionAdapter adapter;
	private int emotionPosition;
	private String publishString=null;
	private int[] imageIds = new int[30];
	private CustomDialog waitingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writeblog);
		waitingDialog=waitingDialog();
		initWidget();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		adapter=new EmotionAdapter(mContext);
		backButton=(Button)findViewById(R.id.write_back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		publishblog=(Button)findViewById(R.id.publishblog);
		publishblog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(blog_subject.getText().toString()==null||blog_subject.getText().toString().equals("")){
					Toast.makeText(mContext, "您还未输入标题，请输入后重试", Toast.LENGTH_SHORT).show();
				}else if(blog_message.getText().toString()==null||blog_message.getText().toString().equals("")){
					Toast.makeText(mContext, "您还未输入内容，请输入后重试", Toast.LENGTH_SHORT).show();
				}else{
					handler.sendEmptyMessage(0);
				}
			}
		});
		blog_subject=(EditText)findViewById(R.id.blog_subject);
		blog_subject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emotion_GridView.getVisibility()==View.VISIBLE){
					emotion_GridView.setVisibility(View.GONE);
				}
			}
		});
		blog_message=(EditText)findViewById(R.id.blog_message);
		blog_message.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emotion_GridView.getVisibility()==View.VISIBLE){
					emotion_GridView.setVisibility(View.GONE);
				}
			}
		});
		blogemotion=(Button)findViewById(R.id.blogemotion);
		blogemotion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emotion_GridView.setVisibility(View.VISIBLE);	
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				//隐藏键盘
				imm.hideSoftInputFromWindow(PublishBlogActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				initEmotion();
				blogemotion.setVisibility(View.GONE);
				blogkeyboard.setVisibility(View.VISIBLE);
			}
		});
		blogkeyboard=(Button)findViewById(R.id.blogkeyboard);
		blogkeyboard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emotion_GridView.setVisibility(View.GONE);	
				blogkeyboard.setVisibility(View.GONE);
				blogemotion.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				//显示键盘
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
				emotion_GridView.setAdapter(adapter);
			}
		});
		emotion_GridView=(GridView)findViewById(R.id.grid_emotion);
		emotion_GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				emotionPosition=position;
				publishString+="[em:"+emotionPosition+":]";
			}
		});
		blog_message.setText(publishString);
	}
	private void initEmotion(){
		List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
		//生成107个表情的id，封装
		for(int i = 0; i < 30; i++){
			try {
				Field field = R.drawable.class.getDeclaredField("image" + (i+1));
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
		emotion_GridView.setNumColumns(5);
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				//发日志
				sendBlog=new SendBlog(blog_subject.getText().toString(),blog_message.getText().toString());
				ClientNetwork.Instace().asynGetResponse(
						new RequestSendBlog(
								AccountManager.Instace(mContext).userId,AccountManager.Instace(mContext).userName,
								sendBlog), new IResponseReceiver() {
							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseSendBlog responseSendBlog = (ResponseSendBlog) response;
								System.out.println("response---"+responseSendBlog.result);
								if (responseSendBlog.result.equals("331")) {
									
									handler.sendEmptyMessage(1);
								}
							}
						}, new DefNetStateReceiverImpl(), null);
				break;

			case 1:
				finish();
				Toast.makeText(mContext, "发表成功", Toast.LENGTH_SHORT).show();
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
		CustomDialog cDialog = customBuilder
				.setContentView(layout).create();
		return cDialog;
	}
}
