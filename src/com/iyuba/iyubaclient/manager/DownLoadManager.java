package com.iyuba.iyubaclient.manager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ths.frame.runtime.RuntimeManager;
import org.ths.frame.util.ImageDownLoadUtil;
import org.ths.frame.util.SDCardUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.iyuba.iyubaclient.manager.DownLoadCallBack;


public class DownLoadManager {
	private static Context mContext;
	private static DownLoadManager instance;
	private ExecutorService pool;
	private static final String ROOT_DIR = SDCardUtil.getSDPath() + "/"
			+ RuntimeManager.getAppInfo().packageNames + "/";
	private static final String IMAGE_PATH = "image/";
	private static final String APP_PATH = "app/";
	private static final String AUDIO_PATH = "audio/";

	private DownLoadManager() {
		pool = Executors.newFixedThreadPool(1); // 创建线程池并设定池中线程数量
	};

	public static synchronized DownLoadManager Instace(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new DownLoadManager();
		}
		return instance;
	}

	/**
	 * 下载图片，独立线程
	 * 
	 * @param urlPath
	 * @param downloadCallBack
	 */
	public void downLoadImage(final String urlPath, final String reName,
			final DownLoadCallBack downloadCallBack, final boolean thumbnail) {
		if (SDCardUtil.hasSDCard()) {
			Thread downLoadT = new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					Bitmap image = new ImageDownLoadUtil().loadImage(urlPath,
							reName, ROOT_DIR + IMAGE_PATH, thumbnail);
					if (downloadCallBack != null) {
						downloadCallBack.downLoadSuccess(image);
					}
				}

			};
//			downLoadT.start();
			pool.execute(downLoadT);
		} else {
			Toast.makeText(mContext, "SD卡不存在！", Toast.LENGTH_SHORT).show();
		}
	}

/*	public void batchGetImage(List<VoaTitle> voaTitles,DownLoadCallBack downLoadCallBack) {
		for (VoaTitle voaTitle : voaTitles) {
			downLoadImage(voaTitle.pic, String.valueOf(voaTitle.voaId),
					downLoadCallBack, true);
		}
	}*/
	
}
