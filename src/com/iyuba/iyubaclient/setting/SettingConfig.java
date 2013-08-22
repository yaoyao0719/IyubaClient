package com.iyuba.iyubaclient.setting;

import org.ths.frame.components.ConfigManager;

import android.util.Log;

public class SettingConfig {
	
	private volatile static SettingConfig instance;
	
	private boolean automaticDownload;
	private boolean screenLit;
	private boolean rememberPwd;
	private boolean autoLogin;
	private boolean firstUserApp;
	private int textHighlightColor;
	private int textSize;
	
	private final static String AUTO_DOWN_TAG="autoDown";
	private final static String SCREEN_LIT_TAG="screenLit";
	private final static String REMEMBER_PWD_TAG="rememberPwd";
	private final static String AUTO_LOGIN_TAG="autoLogin";
	private final static String FIRST_USER_APP="firstUserApp";
	private final static String TEXT_HIGHLIGTH_COLOR="textHighlightColor";
	private final static String TEXT_SIZE="textSize";
	
	private SettingConfig() {

	}
	
	public static SettingConfig Instance() {

		if (instance == null) {
			synchronized (SettingConfig.class) {
				if (instance == null) {
					instance = new SettingConfig();
				}
			}

		}
		return instance;
	}
	
	/**
	 * 获取是否自动下载
	 * @return
	 */
	public boolean isAutomaticDownload() {
		try {
			automaticDownload=ConfigManager.Instance().loadBoolean(AUTO_DOWN_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			automaticDownload=false;
		}
		return automaticDownload;
	}

	// 设置是否自动下载
	public void setAutomaticDownload(boolean automaticDownload) {
		ConfigManager.Instance().putBoolean(AUTO_DOWN_TAG, automaticDownload);
	}
	
	
	/**
	 * 获取是否播放时屏幕常�?
	 * @return
	 */
	public boolean isScreenLit() {
		try {
			screenLit=ConfigManager.Instance().loadBoolean(SCREEN_LIT_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			screenLit=false;
		}
		return screenLit;
	}

	/**
	 * 设置屏幕常亮
	 * @param automaticDownload
	 */
	public void setScreenLit(boolean screenLit) {
		ConfigManager.Instance().putBoolean(SCREEN_LIT_TAG, screenLit);
	}
	
	/**
	 * 获取是否记住密码
	 * @return
	 */
	public boolean isRememberPwd() {
		try {
			rememberPwd=ConfigManager.Instance().loadBoolean(REMEMBER_PWD_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rememberPwd=false;
		}
		return rememberPwd;
	}

	/**
	 * 设置是否记住密码
	 * @param automaticDownload
	 */
	public void setRememberPwd(boolean RememberPwd) {
		ConfigManager.Instance().putBoolean(REMEMBER_PWD_TAG, RememberPwd);
	}
	
	/**
	 * 获取是否自动登录
	 * @return
	 */
	public boolean isAutoLogin() {
		try {
			autoLogin=ConfigManager.Instance().loadBoolean(AUTO_LOGIN_TAG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			autoLogin=false;
		}
		return autoLogin;
	}

	/**
	 * 设置是否自动登录
	 * @param automaticDownload
	 */
	public void setAutoLogin(boolean AutoLogin) {
		ConfigManager.Instance().putBoolean(AUTO_LOGIN_TAG, AutoLogin);
	}
	
	
	
	
	
	/**
	 * 获取是否自动登录
	 * @return
	 */
	public boolean isFirstUserApp() {
		try {
			firstUserApp=ConfigManager.Instance().loadBoolean(FIRST_USER_APP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			firstUserApp=false;
		}
		return firstUserApp;
	}

	/**
	 * 设置是否自动登录
	 * @param automaticDownload
	 */
	public void setFirstUserApp(boolean firstUserApp) {
		ConfigManager.Instance().putBoolean(FIRST_USER_APP, firstUserApp);
	}
	
	/**
	 * 获取高亮颜色�?
	 * @return
	 */
	public int getTextHighlightColor() {
		this.textHighlightColor=ConfigManager.Instance().loadInt(TEXT_HIGHLIGTH_COLOR);
		return this.textHighlightColor;
	}

	/**
	 * 设置高亮颜色�?
	 * @param textHighlightColor
	 */
	public void setTextHighlightColor(int textHighlightColor) {
		this.textHighlightColor = textHighlightColor;
		ConfigManager.Instance().putInt(TEXT_HIGHLIGTH_COLOR, textHighlightColor);
	}
	
	/**
	 * 获取文字大小
	 * @return
	 */
	public int getTextSize() {
		this.textSize=ConfigManager.Instance().loadInt(TEXT_SIZE);
		return this.textSize;
	}

	/**
	 * 设置文字大小
	 * @param textSize
	 */
	public void setTextSize(int textSize) {
		this.textSize = textSize;
		ConfigManager.Instance().putInt(TEXT_SIZE, textSize);
	}
	
}
