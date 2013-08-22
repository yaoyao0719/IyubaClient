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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.iyuba.iyubaclient.entity.Emotion;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestPublishMood;
import com.iyuba.iyubaclient.procotol.ResponsePublishMood;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

/**
 * @author yao
 *
 */
public class ReportActivity extends THSActivity{
	private Button backButton,publishMood_Button;
	private EditText mood_content;
	private ImageView emotion,cancelLoc;
	private GridView emotion_GridView;
	private String publishString="";
	private CustomDialog waitingDialog;
	private int[] imageIds = new int[30];
	private TextView currLocation,wordsCount;
	private Boolean isLoc=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		waitingDialog=waitingDialog();
		publishString="报到\n";
		initWidget();
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub	
		backButton=(Button)findViewById(R.id.report_back_btn);
		emotion_GridView=(GridView)findViewById(R.id.grid_emotion);
		currLocation=(TextView)findViewById(R.id.currlocation);
		currLocation.setText(DataManager.Instace().poiInfo.name);
		wordsCount=(TextView)findViewById(R.id.wordscount);
		cancelLoc=(ImageView)findViewById(R.id.report_cancelloc);
		cancelLoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLoc=false;
			}
		});
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReportActivity.this.finish();
			}
		});
		publishMood_Button=(Button)findViewById(R.id.report_senddoing_btn);
		publishMood_Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mood_content.getText().toString().length()>200){
					handler.sendEmptyMessage(5);
				}else if(mood_content.getText().toString().length()<1){
					handler.sendEmptyMessage(6);
				}else{
					handler.sendEmptyMessage(0);
				}
				
			}
		});
		mood_content=(EditText)findViewById(R.id.report_content);
		mood_content.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emotion_GridView.getVisibility()==View.VISIBLE){
					emotion_GridView.setVisibility(View.GONE);
				}
			}
		});
		mood_content.setFocusableInTouchMode(true);
		mood_content.requestFocus();
		mood_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				wordsCount.setText(mood_content.getText().toString().length()+"");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		emotion=(ImageView)findViewById(R.id.report_emotion);
		emotion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
				hideSoftInputFromWindow(ReportActivity.this.getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);  	
				initEmotion();
				emotion_GridView.setVisibility(View.VISIBLE);			
				//emotion_GridView.setAdapter(adapter);
			}
		});
		
		emotion_GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(), imageIds[position % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(ReportActivity.this, bitmap);
				String str = null;
				str = "image"+position;
				String str1=null;
				str1=Emotion.express[position];
				SpannableString spannableString = new SpannableString(str);
				SpannableString spannableString1 = new SpannableString(str1);
				if(str.length()==6){
					spannableString.setSpan(imageSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else if(str.length()==7){
					spannableString.setSpan(imageSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else{
					spannableString.setSpan(imageSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}	
				System.out.println("spannableString=="+spannableString);
				mood_content.append(spannableString1);		
	
			}
		});
		
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
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				//发表状态
				if(isLoc){
					publishString=publishString+mood_content.getText().toString()+"\n"+DataManager.Instace().poiInfo.name;
				}else{
					publishString=mood_content.getText().toString();
				}

				ClientNetwork.Instace().asynGetResponse(new RequestPublishMood(AccountManager.Instace(mContext).userId,
						AccountManager.Instace(mContext).userName,publishString) , new IResponseReceiver(){

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponsePublishMood res=(ResponsePublishMood)response;
						if(res.result.equals("351")){					
							handler.sendEmptyMessage(8);												
						}else{							
						}
						
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
				break;
			case 5:
				Toast.makeText(mContext, "您最多能输入200个字符", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(mContext, "您不能输入空状态", Toast.LENGTH_SHORT).show();
				break;	
			case 7:

				break;
			case 8:
				Toast.makeText(mContext, "发表成功", Toast.LENGTH_SHORT).show();
				finish();
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

