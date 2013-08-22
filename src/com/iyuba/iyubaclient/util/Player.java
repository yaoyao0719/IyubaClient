package com.iyuba.iyubaclient.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.ths.frame.network.DefNetStateReceiverImpl;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener {
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Context mContext;
	private Timer mTimer = new Timer();
	private OnPlayStateChangedListener opscl;
	private String audioUrl;
	private DefNetStateReceiverImpl dialog=new DefNetStateReceiverImpl();

	public Player(Context context, OnPlayStateChangedListener opscl,
			SeekBar skbProgress) {
		this.skbProgress = skbProgress;
		this.mContext = context;
		this.opscl = opscl;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		// mTimer.schedule(mTimerTask, 0, 1000);
		// mTimer.cancel();
	}

	public void setSkbProgress(SeekBar skbProgress) {
		this.skbProgress = skbProgress;
	}
	
	public void setOpscl(OnPlayStateChangedListener opscl) {
		this.opscl = opscl;
	}
	
	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			// Log.e("dingshiqi", "!!!!!!!!!!!");
			try {
				if (mediaPlayer == null)
					return;
				
				if(skbProgress!=null){
					if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
						handleProgress.sendEmptyMessage(0);
					}
				}else{
					if (mediaPlayer.isPlaying()) {
						handleProgress.sendEmptyMessage(0);
					}
				}
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (mediaPlayer != null) {
				int position = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();
				if(skbProgress!=null){
					if (duration > 0) {
						long pos = skbProgress.getMax() * position / duration;
						skbProgress.setProgress((int) pos);
					}
				}
				if (opscl != null) {
					opscl.setPlayTime(getAudioCurrTime(), getAudioAllTime());
				}
				dialog.onCancelOrEnd();
			}
		};
	};

	// *****************************************************

	public void play() {
		mediaPlayer.start();
	}

	public void playUrl(final String videoUrl) {
		this.audioUrl = videoUrl;
		handler.sendEmptyMessage(1);
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		mTimer.cancel();
	}

	public boolean isPlaying() {
		if(mediaPlayer!=null){
			return mediaPlayer.isPlaying();
		}
		return false;
	}

	public int getDur() {
		if (mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	/**
	 * 通过onPrepared播放
	 */
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		// Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// Log.e("mediaPlayer", "onCompletion");
		if (skbProgress != null) {
			skbProgress.setProgress(0);
		}
		if (opscl != null) {
			opscl.playCompletion();
		}

		if (opscl != null) {
			opscl.setPlayTime("00:00", getAudioAllTime());
		}

	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		if (skbProgress != null) {
			skbProgress.setSecondaryProgress(bufferingProgress);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(mContext, "请检查网络是否可用！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				dialog.onStartConnect();
				try {
					mediaPlayer.reset();
					mediaPlayer.setDataSource(audioUrl);
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							try {
								mediaPlayer.prepare();
								// mediaPlayer.start();

								handler.sendEmptyMessage(2);
								// mTimer.schedule(mTimerTask, 0, 1000);
								// if (opscl != null) {
								// opscl.setPlayTime(getAudioCurrTime(),
								// getAudioAllTime());
								// }
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}// prepare之后自动播放
						}
					}.start();

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					if (opscl != null) {
						opscl.playFaild();
					}
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					if (opscl != null) {
						opscl.playFaild();
					}
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if (opscl != null) {
						opscl.playFaild();
					}
					e.printStackTrace();
				}
				break;
			case 2:
				mTimer.schedule(mTimerTask, 0, 1000);
				if (opscl != null) {
					opscl.setPlayTime(getAudioCurrTime(), getAudioAllTime());
				}
				break;
			}
		}
	};

	/**
	 * 获取音频总长
	 * 
	 * @return
	 */
	public String getAudioAllTime() {
		StringBuffer timeBuffer = new StringBuffer("");
		if (mediaPlayer != null) {
			int musicTime = mediaPlayer.getDuration() / 1000;

			String minu = String.valueOf(musicTime / 60);
			if (minu.length() == 1) {
				minu = "0" + minu;
			}
			String sec = String.valueOf(musicTime % 60);
			if (sec.length() == 1) {
				sec = "0" + sec;
			}

			timeBuffer.append(minu).append(":").append(sec);
		}
		return timeBuffer.toString();
	}

	/**
	 * 获取音频当前播放进度时间
	 * 
	 * @return
	 */
	public String getAudioCurrTime() {
		StringBuffer timeBuffer = new StringBuffer("");
		if (mediaPlayer != null) {
			int musicTime = mediaPlayer.getCurrentPosition() / 1000;

			String minu = String.valueOf(musicTime / 60);
			if (minu.length() == 1) {
				minu = "0" + minu;
			}
			String sec = String.valueOf(musicTime % 60);
			if (sec.length() == 1) {
				sec = "0" + sec;
			}

			timeBuffer.append(minu).append(":").append(sec);
		}
		return timeBuffer.toString();
	}

}
