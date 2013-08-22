/**
 * 
 */
package com.iyuba.iyubaclient.manager;

import java.util.ArrayList;
import java.util.List;

import com.iyuba.iyubaclient.entity.Attention;
import com.iyuba.iyubaclient.entity.Fans;
import com.iyuba.iyubaclient.entity.MutualAttention;
import com.iyuba.iyubaclient.sqlite.entity.BlogContent;
import com.iyuba.iyubaclient.sqlite.entity.BlogInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsCommentInfo;
import com.iyuba.iyubaclient.sqlite.entity.DoingsInfo;
import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.GuessFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.MessageLetter;
import com.iyuba.iyubaclient.sqlite.entity.NearFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.NotificationInfo;
import com.iyuba.iyubaclient.sqlite.entity.POIInfo;
import com.iyuba.iyubaclient.sqlite.entity.PublicAccountInfo;
import com.iyuba.iyubaclient.sqlite.entity.ReplyInfo;
import com.iyuba.iyubaclient.sqlite.entity.SameAppFriendInfo;
import com.iyuba.iyubaclient.sqlite.entity.SearchItem;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;


/**
 * @author yao
 * 日志管理类
 */
public class DataManager {
	
	private static DataManager instance;
	public DataManager() {
		// TODO Auto-generated constructor stub
	}
	public static synchronized DataManager Instace(){
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	public BlogInfo blogInfo;
	public BlogContent blogContent;
	public DoingsInfo doingsInfo;
	public DoingsCommentInfo doingsCommentInfo;
	public Fans fans;
	public Attention attention;
	public MutualAttention mutualAttention;
	public List<BlogInfo> blogList=new ArrayList<BlogInfo>();
	public List<DoingsInfo> doingsList=new ArrayList<DoingsInfo>();
	public List<DoingsCommentInfo> doingsCommentInfoList=new ArrayList<DoingsCommentInfo>();
	public MessageLetter letter=new MessageLetter();
	public List<MessageLetter> letterlist=new ArrayList<MessageLetter>();
	public NotificationInfo replyInfo=new NotificationInfo();
	public List<NotificationInfo> nofiList=new ArrayList<NotificationInfo>();
	public FeedInfo feed=new FeedInfo();
	public List<FeedInfo> feedList=new ArrayList<FeedInfo>();
	public UserInfo userInfo=new UserInfo();
	public NearFriendInfo nearFriendInfo=new NearFriendInfo();
	public GuessFriendInfo guessFriendInfo=new GuessFriendInfo();
	public SameAppFriendInfo sameAppFriendInfo=new SameAppFriendInfo();
	public PublicAccountInfo publicAccountInfo=new PublicAccountInfo();
	public SearchItem searchItem=new SearchItem();
	public POIInfo poiInfo=new POIInfo();
}
