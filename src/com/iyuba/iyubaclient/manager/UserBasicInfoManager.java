package com.iyuba.iyubaclient.manager;

import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseUserDetailInfo;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;

public class UserBasicInfoManager {

	private static UserBasicInfoManager instance;
	private UserBasicInfoManager(){
	}
	public static synchronized UserBasicInfoManager Instace(){
		if(instance==null){
			instance=new UserBasicInfoManager();
		}
		return instance;
	}
	public UserInfo userInfo;
	public ResponseBasicUserInfo responseBasicUserInfo;
	public ResponseUserDetailInfo responseUserDetailInfo;
}
