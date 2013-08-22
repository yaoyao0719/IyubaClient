package com.iyuba.iyubaclient.manager;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.helper.INormalCallBack;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.RequestLogin;
import com.iyuba.iyubaclient.procotol.RequestRegister;
import com.iyuba.iyubaclient.procotol.ResponseLogin;
import com.iyuba.iyubaclient.procotol.ResponseRegister;
import com.iyuba.iyubaclient.setting.SettingConfig;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class AccountManager {
	private static Context mContext;
	private static AccountManager instance;
	public String userId;
	public String userName;
	public String userImgSrc;
	public String realName;
	public String password;
	public String email;
	public String balance;
	public boolean isLoginStatus=false; // 登录状态
	public String errorInfo;
	public UserInfo userInfo=new UserInfo();
	
	private AccountManager() {
	};

	public static synchronized AccountManager Instace(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}
	
	/**
	 * �?��是否登录
	 * @return
	 */
	public boolean checkLogin(){
		return isLoginStatus;
	}
	
	/**
	 * 登录
	 * @param loginName
	 * @param password
	 * @param iNormalCallBack
	 */
	public void login(final String loginName,String password,final INormalCallBack iNormalCallBack){
		if(ConfigManager.Instance().loadBoolean("isLogin")){
			isLoginStatus=true;
			System.out.println("不联网登录"+ConfigManager.Instance().loadBoolean("isLogin"));
		}else{
			ClientNetwork.Instace().asynGetResponse(
					new RequestLogin(loginName,password), new IResponseReceiver() {

						@Override
						public void onResponse(BaseHttpRequest request,
								BaseHttpResponse response) {
							// TODO Auto-generated method stub
							ResponseLogin responseLogin = (ResponseLogin) response;					
							if(responseLogin.resultCode.equals("103")){
								errorInfo="密码错误，请重新输入";
								isLoginStatus=false;
								if(iNormalCallBack!=null){
									iNormalCallBack.failure();
								}
							}else if(responseLogin.resultCode.equals("102")){
								errorInfo="用户名不存在，请重新输入";
								isLoginStatus=false;
								if(iNormalCallBack!=null){
									iNormalCallBack.failure();
								}
							}else if(responseLogin.resultCode.equals("101")){
								userId=responseLogin.userId;
								userName=loginName;						
								email=responseLogin.email;	
								userImgSrc=responseLogin.imgSrc;
								userInfo.email=responseLogin.email;	
								userInfo.imgSrc=responseLogin.imgSrc;
								userInfo.Amount=responseLogin.Amount;
								userInfo.expireTime=responseLogin.expireTime;
								userInfo.vipStatus=responseLogin.vipStatus;
								userInfo.uid=responseLogin.userId;
								userInfo.username=responseLogin.userName;
								DataManager.Instace().userInfo=userInfo;
								AccountManager.Instace(mContext).userInfo.Amount=responseLogin.Amount;
								handler.sendEmptyMessage(1);
								isLoginStatus=true;
								ConfigManager.Instance().putBoolean("isLogin", true);
								if(iNormalCallBack!=null){
									iNormalCallBack.success();
								}
							}else{
								ConfigManager.Instance().putBoolean("isLogin", false);
								isLoginStatus=false;
								if(iNormalCallBack!=null){
									iNormalCallBack.failure();
								}
							}
						}
					},new DefNetStateReceiverImpl(),null);
		}

	}
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case 0:
				saveCacheUserNameAndPwd(userName,password);
				SettingConfig.Instance().setAutoLogin(true);
				break;
			case 1:
				DatabaseManager.Instace(mContext).getUserHelper().insertUserData(DataManager.Instace().userInfo);
				break;
				
			}
		}
		
	};
	/**
	 * 注册
	 * @param userName
	 * @param email
	 * @param password
	 * @param iNormalCallBack
	 */
	public void register(String userName,String email,String password,final INormalCallBack iNormalCallBack){
		ClientNetwork.Instace().asynGetResponse(
				new RequestRegister(userName,password ,email), new IResponseReceiver() {

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						// TODO Auto-generated method stub
						ResponseRegister responseRegister = (ResponseRegister) response;
						if(responseRegister.resultCode.equals("104")){
							errorInfo="邮箱格式错误";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}	
						if(responseRegister.resultCode.equals("105")){
							errorInfo="密码格式错误，禁止使用全部数字";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
						if(responseRegister.resultCode.equals("106")){
							errorInfo="非法邮箱";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
						if(responseRegister.resultCode.equals("110")){
							errorInfo="注册失败";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
						if(responseRegister.resultCode.equals("112")){
							errorInfo="用户名存在";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
						if(responseRegister.resultCode.equals("113")){
							errorInfo="邮箱存在";
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
					//	errorInfo=responseRegister.message;
						if(responseRegister.isRegSuccess){
							if(iNormalCallBack!=null){
								iNormalCallBack.success();
							}
						}else{
							if(iNormalCallBack!=null){
								iNormalCallBack.failure();
							}
						}
					}
				},new DefNetStateReceiverImpl(),null);
	}
	
	public void loginOut(){
		isLoginStatus=false;
		ConfigManager.Instance().putString("userName", "");
		ConfigManager.Instance().putString("userPwd", "");
		userId=null;
		ConfigManager.Instance().putBoolean("isLogin", false);
	}
	
	/**
	 * 保存账户密码
	 * @param userName
	 * @param userPwd
	 */
	public void saveCacheUserNameAndPwd(String userName,String userPwd){
		ConfigManager.Instance().putString("userName", userName);
		ConfigManager.Instance().putString("userPwd", userPwd);
		ConfigManager.Instance().putString("userId", userId);
		ConfigManager.Instance().putString("userImgSrc", userImgSrc);
	}
	
	/**
	 * 获取用户名及密码
	 * @return string[2] [0]=userName,[1]=userPwd
	 */
	public String[] getCacheUserNameAndPwd(){
		String[] nameAndPwd=new String[]{ConfigManager.Instance().loadString("userName"),
				ConfigManager.Instance().loadString("userPwd")};
		return nameAndPwd;
	}
	
}
