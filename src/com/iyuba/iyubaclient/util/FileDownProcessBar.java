package com.iyuba.iyubaclient.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.ths.frame.components.ConfigManager;
import org.ths.frame.runtime.RuntimeManager;
import org.ths.frame.util.SDCardUtil;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FileDownProcessBar {
	private ProgressBar progressBar;
	private Context mContext;
	private int FileLength;
	private int DownedFileLength = 0;
	private InputStream inputStream;
	private URLConnection connection;
	private OutputStream outputStream;
	private String saveName;
	private String fileUrl;

	public static final String ROOT_DIR = SDCardUtil.getSDPath() + "/"
			+ RuntimeManager.getAppInfo().packageNames + "/";
	public static final String IMAGE_PATH = "image/";
	public static final String APP_PATH = "app/";
	public static final String AUDIO_PATH = "audio/";

	private DownLoadFailCallBack dlfcb; //

	private int downLoadType = 0; // 0=MP3,1=APK

	private String savePathString;

	public FileDownProcessBar(Context context) {
		this.mContext = context;
	}

	public FileDownProcessBar(Context context, ProgressBar progressBar) {
		this.mContext = context;
		this.progressBar = progressBar;
	}

	/**
	 * 
	 * @param filePath
	 */
	public void downLoadAudioFile(final String newWorkFileUrl,
			final String saveName, final DownLoadFailCallBack dlfcb) {
		downLoadType = 0;
		this.dlfcb = dlfcb;
		if (SDCardUtil.hasSDCard()) {
			DownedFileLength = 0; // 下载媒体
			// TODO Auto-generated method stub
			Thread thread = new Thread() {
				public void run() {
					try {
						DownFile(newWorkFileUrl, saveName);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
			thread.start();
		} else {
			Toast.makeText(mContext, "下载失败！请检查SD卡是否存在！", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 
	 * @param filePath
	 */
	public void downLoadApkFile(final String newWorkFileUrl,
			final String saveName, final DownLoadFailCallBack dlfcb) {
		downLoadType = 1; // 下载APK
		this.dlfcb = dlfcb;
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				dlfcb.downLoadFaild("");
			}
		};
		if (SDCardUtil.hasSDCard()) {
			DownedFileLength = 0;
			// TODO Auto-generated method stub
			Thread thread = new Thread() {
				public void run() {
					// Log.e("$$$$$$$$$$$$$$$", newWorkFileUrl+saveName);
					try {
						DownFile(newWorkFileUrl, saveName);
					} catch (Exception e) {
						// TODO: handle exception
						handler.sendEmptyMessage(0);
					}
				}
			};
			thread.start();
		} else {
			Toast.makeText(mContext, "下载失败！请检查SD卡是否存在！", Toast.LENGTH_LONG)
					.show();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					if (dlfcb != null) {
						dlfcb.downLoadBegin();
					}
					if (progressBar != null) {
						progressBar.setMax(FileLength);
					}
					// Log.i("文件长度----------->", progressBar.getMax() + "");
					break;
				case 1:
					if (dlfcb != null) {
						NumberFormat formatter = new DecimalFormat("0.00");
						Double x=new Double((double)DownedFileLength/(double)FileLength);
						dlfcb.downLoadCurrProcess(formatter.format(x));
					}
					if (progressBar != null) {
						progressBar.setProgress(DownedFileLength);
					}
					break;
				case 2:
					// Log.i("下载完成----------->", progressBar.getMax() + "");
					if (downLoadType == 0) {
						if (reNameFile(savePathString, savePathString + ".mp3")) {
							if (dlfcb != null) {
								dlfcb.downLoadSuccess(savePathString + ".mp3");
							}
						} else {
							dlfcb.downLoadFaild("error");
						}
					} else if (downLoadType == 1) {
						if (reNameFile(savePathString, savePathString + ".apk")) {
							if (dlfcb != null) {
								dlfcb.downLoadSuccess(savePathString + ".apk");
							}
						} else {
							dlfcb.downLoadFaild("error");
						}

					}

					break;
				case 3: // 下载失败
					ConfigManager.Instance().putInt(saveName, 0); // 保存进度，用于列表识别进度条
					break;
				default:
					break;
				}
			}
		}

	};

	private void DownFile(String urlString, String saveName) {
		this.saveName = saveName;

		if (downLoadType == 0) {
			this.fileUrl = "http://static.iyuba.com/sounds/voa" + urlString;
			savePathString = ROOT_DIR + AUDIO_PATH + "/" + this.saveName;
		} else if (downLoadType == 1) {
			this.fileUrl = urlString;
			savePathString = ROOT_DIR + APP_PATH + "/" + this.saveName;
			;
		}

		// Log.e("url=",fileUrl);
		File fileTemp = new File(savePathString + ".mp3");
		if (fileTemp.exists()) {
			// Log.e("下载音频", "缓存已存在！");
		} else {
			// Log.e("下载音频-网络地址", fileUrl);
			// Log.e("下载音频-本地名称", saveName);
			/*
			 * 连接到服务器
			 */
			try {
				URL url = new URL(fileUrl);
				connection = url.openConnection();
				if (connection.getReadTimeout() == 5) {
					// Log.i("---------->", "当前网络有问题");
					// return;
				}
				inputStream = connection.getInputStream();
				// Log.e("下载音频", "开始下载");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
			 */
			String savePAth = "";

			if (downLoadType == 0) {
				savePAth = ROOT_DIR + AUDIO_PATH + "/";
			} else if (downLoadType == 1) {
				savePAth = ROOT_DIR + APP_PATH + "/";
			}

			File file1 = new File(savePAth);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			// Log.e("下载音频-本地名称(完整)", savePathString);
			File file = new File(savePathString);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * 向SD卡中写入文件,用Handle传递线程
			 */
			Message message = new Message();
			try {
				outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024 * 4];
				FileLength = connection.getContentLength();
				message.what = 0;
				handler.sendMessage(message);
				while (DownedFileLength < FileLength) {
					int length = inputStream.read(buffer);
					DownedFileLength += length;
					outputStream.write(buffer, 0, length);
					// Log.i("-------->", DownedFileLength + "");
					Message message1 = new Message();
					message1.what = 1;
					handler.sendEmptyMessage(1);

					int x = DownedFileLength * 100 / FileLength;
					ConfigManager.Instance().putInt(saveName, x); // 保存进度，用于列表识别进度条

				}
				handler.sendEmptyMessage(2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				handler.sendEmptyMessage(3);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				handler.sendEmptyMessage(3);
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 文件更名，从缓冲状态转换到完成状态
	 * 
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 */
	public boolean reNameFile(String oldFilePath, String newFilePath) {
		File source = new File(oldFilePath);
		File dest = new File(newFilePath);
//		Log.e("文件更名", "完成更名");
		return source.renameTo(dest);
	}

}
