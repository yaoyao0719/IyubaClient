package com.iyuba.iyubaclient;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestBasicUserInfo;
import com.iyuba.iyubaclient.procotol.RequestUserDetailInfo;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseUserDetailInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserDetailInfoActivity extends THSActivity{

	private TextView tvUserName,tvGender,tvResideLocation,tvBirthday,
	tvConstellation,tvZodiac,tvGraduatesSchool,tvCompany,
	tvAffectivestatus,tvLookingfor,tvBio,tvInterest,tvAddress;
	private ResponseUserDetailInfo userDetailInfo;
	private Button detailinfo_back_btn;
	private String currentuid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetailinfo);
		Intent intent=getIntent();
		currentuid=intent.getStringExtra("currentuid");
		initWidget();
		LoadInfo();
	}
	private void LoadInfo() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				//waitting.show();
				break;
			case 1:
				//waitting.show();
				ClientNetwork.Instace().asynGetResponse(
						new RequestUserDetailInfo(currentuid), new IResponseReceiver() {
							
							@Override
							public void onResponse(BaseHttpRequest request, BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseUserDetailInfo responseUserDetailInfo=(ResponseUserDetailInfo)response;
								if(responseUserDetailInfo.result.equals("211")){						
									UserBasicInfoManager.Instace().responseUserDetailInfo=responseUserDetailInfo;
									System.out.println("responseUserDetailInfo=="+responseUserDetailInfo.realname+responseUserDetailInfo.birthday);
									userDetailInfo=responseUserDetailInfo;
								}
								handler.sendEmptyMessage(2);
							}							
						});
				break;
			case 2:
				//waitting.dissmiss();
				setText();

			default:
				break;
			}
		}	
	};

	private void setText() {
		// TODO Auto-generated method stub
		tvUserName.setText(userDetailInfo.realname);
		if(userDetailInfo.gender.equals("1")){
			tvGender.setText("男");
		}else if(userDetailInfo.gender.equals("2")){
			tvGender.setText("女");
		}
		tvResideLocation.setText(userDetailInfo.resideLocation);
		tvBirthday.setText(userDetailInfo.birthday);
		tvConstellation.setText(userDetailInfo.constellation);
		tvZodiac.setText(userDetailInfo.zodiac);
		tvGraduatesSchool.setText(userDetailInfo.graduateschool);
		tvCompany.setText(userDetailInfo.company);
		tvAffectivestatus.setText(userDetailInfo.affectivestatus);
		tvLookingfor.setText(userDetailInfo.lookingfor);
		tvBio.setText(userDetailInfo.bio);
		tvInterest.setText(userDetailInfo.interest);
		tvAddress.setText(userDetailInfo.address);
	}
	private void initWidget() {
		// TODO Auto-generated method stub
		tvUserName=(TextView)findViewById(R.id.tvUserName);
		tvGender=(TextView)findViewById(R.id.tvGender);
		tvResideLocation=(TextView)findViewById(R.id.tvResideLocation);
		tvBirthday=(TextView)findViewById(R.id.tvBirthday);
		tvConstellation=(TextView)findViewById(R.id.tvConstellation);
		tvZodiac=(TextView)findViewById(R.id.tvZodiac);
		tvGraduatesSchool=(TextView)findViewById(R.id.tvGraduatesSchool);
		tvCompany=(TextView)findViewById(R.id.tvCompany);
		tvAffectivestatus=(TextView)findViewById(R.id.tvAffectivestatus);
		tvLookingfor=(TextView)findViewById(R.id.tvLookingfor);
		tvBio=(TextView)findViewById(R.id.tvBio);
		tvInterest=(TextView)findViewById(R.id.tvInterest);
		tvAddress=(TextView)findViewById(R.id.tvAddress);
		detailinfo_back_btn=(Button)findViewById(R.id.detailinfo_back_btn);
		detailinfo_back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserDetailInfoActivity.this.finish();
			}
		});
	}

}
