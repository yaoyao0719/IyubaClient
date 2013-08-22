/**
 * 
 */
package com.iyuba.iyubaclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.procotol.RequestFeedBack;
import com.iyuba.iyubaclient.procotol.ResponseFeedback;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author yao
 *
 */
public class FeedBackActivity extends THSActivity{

	private Button back,send;
	private EditText context,email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initWidget();
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		back=(Button)findViewById(R.id.feedback_back_btn);
		send=(Button)findViewById(R.id.sendfeedback);
		context=(EditText)findViewById(R.id.editText_info);
		email=(EditText)findViewById(R.id.editText_Contact);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FeedBackActivity.this.finish();
			}
		});
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(verification()){
					ClientNetwork.Instace().asynGetResponse(
							new RequestFeedBack(AccountManager.Instace(mContext).userId,email.getText().toString(),context.getText().toString()), new IResponseReceiver() {
								
								@Override
								public void onResponse(BaseHttpRequest request, BaseHttpResponse response) {
									// TODO Auto-generated method stub
									ResponseFeedback res = (ResponseFeedback) response;
									if(res.resultCode.equals("911")){
										handler.sendEmptyMessage(0);
										finish();
									}
								
								}
							},new DefNetStateReceiverImpl(),null);
				}
			}
		});
		
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "意见反馈提交成功" ,Toast.LENGTH_SHORT).show();
		}
		
	};
	/**
	 * 验证
	 */
	public boolean verification() {
		String contextString = context.getText().toString();
		String emailString = email.getText().toString();

		if (contextString.length() == 0) {
			context.setError("反馈信息不能为空！");
			return false;
		}
		
		if (emailString.length() != 0) {
			if (!emailCheck(emailString)) {
				email.setError("请填写有效的邮箱地址！");
				return false;
			}
		}else{
			if(!AccountManager.Instace(mContext).checkLogin()){
				email.setError("请填写邮箱地址！");
				return false;
			}
		}

		return true;
	}
	/**
	 * email格式匹配
	 * 
	 * @param email
	 * @return
	 */
	public boolean emailCheck(String email) {
		String check = "^([a-z0-ArrayA-Z]+[-|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}
}
