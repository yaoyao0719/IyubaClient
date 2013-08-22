package com.iyuba.iyubaclient.manager;

import java.util.ArrayList;
import org.ths.frame.database.EntityBean;
import org.ths.frame.database.DatabaseService;

import com.iyuba.iyubaclient.sqlite.entity.FeedInfo;
import com.iyuba.iyubaclient.sqlite.entity.UserInfo;
import com.iyuba.iyubaclient.sqlite.entity.Words;
import com.iyuba.iyubaclient.sqlite.helper.FeedInfoHelper;
import com.iyuba.iyubaclient.sqlite.helper.UserHelper;
import com.iyuba.iyubaclient.sqlite.helper.WordsHelper;

import android.content.Context;
import android.util.Log;

/**
 * 数据库统一操作类
 * @author lijingwei
 *
 */
public class DatabaseManager {
	private static Context mContext;
	private String databaseName = "iyuba.db";
	private int databaseVersion = 1;
	private ArrayList<EntityBean> entitys;
	private static DatabaseManager instance;
	private DatabaseService databaseService;
	private WordsHelper wordsHelper;
	private UserHelper userHelper;
	private FeedInfoHelper feedInfoHelper;

	private DatabaseManager() {
		entitys = initEntitys();
		databaseService = new DatabaseService(mContext, databaseName, entitys,
				databaseVersion);
		wordsHelper=new WordsHelper(databaseService);
		userHelper=new UserHelper(databaseService);
		feedInfoHelper=new FeedInfoHelper(databaseService);
	};

	public static synchronized DatabaseManager Instace(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	private ArrayList<EntityBean> initEntitys() {
		entitys = new ArrayList<EntityBean>();
		entitys.clear();
		entitys.add(new Words());
		entitys.add(new UserInfo());
		entitys.add(new FeedInfo());
		return entitys;
	}

	public WordsHelper getWordsHelper() {
		return wordsHelper;
	}
	public UserHelper getUserHelper() {
		return userHelper;
	}
	public FeedInfoHelper getFeedInfoHelper() {
		return feedInfoHelper;
	}
}
