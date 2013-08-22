package com.iyuba.iyubaclient.util;

import android.os.Environment;

/**
 * 
 * @author yaoyao
 * 
 * 功能：程序中用到的常量
 *
 */
public class Constant
{

  //程序存放在sd卡上地址
  public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory() + "/com.iyuba.iyubaclient/";
  //assets目录下表情存放文件夹
  public static final String ASSETS_IMAGE_PATH = "comcom";
  //表情转存到sd卡上
  public static final String SDCARD_IMAGE_PATH = "comcom";
  //头像转存到sd卡上
  public static final String SDCARD_PORTEAIT_PATH = "image/";
  //mp3文件存放sd卡上
  public static final String SDCARD_AUDIO_PATH = "audio";
  //软件更新apk地址
  public static final String SDCARD_APPUPDATE_PATH = APP_DATA_PATH ;
  //服务器地地址
  public static final String SERVER_PATH = "http://static2.iyuba.com/sounds/toeic/";
  //爱语吧web地址
  public static final String IYUBA_WEBADD = "http://app.iyuba.com/android/";
  //爱语吧语币充值地址
  public static final String IYUBA_CHARGE = "http://app.iyuba.com/wap/index.jsp?uid=";
  //头像下载地址
  public static final String IMAGE_DOWN_PATH = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&size=big&uid=";
  //Api地址 没用
  public static final String API= "http://api.iyuba.com.cn/v2/api.iyuba";

  
}
