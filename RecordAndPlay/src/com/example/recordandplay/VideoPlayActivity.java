package com.example.recordandplay;

import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VideoPlayActivity extends Activity implements OnInfoListener, OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback{
    private SurfaceView surfaceView;     
    private VideoCtlActivity ctlActivity;
	private View ctlMenuView;
	private Button btnPlay, btnPrev, btnNext;
	private SeekBar seekBar;
	private TextView tvstartPst,tvendPst;
        
    public Handler handler;    
    private String url="";
    private final String tag = "videoPlay";
    public final static int SeekMaxValue = 100;
    public final static int VIDEO_PLAY = 1;
    public final static int VIDEO_PAUSE = 2;
    public final static int VIDEO_STOP = 3;
    public final static int BUFFER_UPDATE = 4;
    public final static int PLAY_UPDATE = 5;
    public final static long delayMillis = 1000;//定时间隙
    
	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	public  static MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private static boolean mSeekTrackingFlag = false;
	private static long mDuration = 0;
	
    /** Called when the activity is first created. */  
    @SuppressLint("HandlerLeak") @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.video_play);
        initView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        Intent intent = getIntent();
        url = intent.getStringExtra("uri");                
        
        handler = new Handler(){
        	public void handleMessage(android.os.Message msg) 
        	{
        		switch (msg.what) {
        		case BUFFER_UPDATE:
        			seekBar.setSecondaryProgress((int)(msg.obj));
        			break;
        		case PLAY_UPDATE:
        			if (mMediaPlayer != null) {
						mMediaPlayer.seekTo((long)(msg.obj));
					}
        			break;
    			default:
    				break;
    			}
        	};
        };
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					handler.postDelayed(this, delayMillis);
					if (mMediaPlayer == null || mSeekTrackingFlag) {
						return;
					}
					if (mMediaPlayer.isPlaying() && ctlActivity.isShowing()) {
						Log.i(tag, String.valueOf(mMediaPlayer.getCurrentPosition()));
						if (mDuration == 0) {
							mDuration = mMediaPlayer.getDuration();
							tvendPst.setText(long2HMS(mDuration));
						}
				        double crtPosition = mMediaPlayer.getCurrentPosition();
				        double totalLenght = mDuration;
				        double tmp = 0.0f;
				        try {
				            tmp = crtPosition/totalLenght;
				            Log.i("tag", String.valueOf(tmp));			
						} catch (Exception e) {
							// TODO: handle exception
							Log.i("tag", e.getMessage());
						}
				        seekBar.setProgress((int)(SeekMaxValue*tmp));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}, delayMillis);
    
        //播放准备工作
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		mPreview = surfaceView;
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);  		
    }
    
    private void initView()
    {
		ctlActivity = new VideoCtlActivity(VideoPlayActivity.this,handler);
		ctlMenuView = ctlActivity.getContentView();
		tvendPst = (TextView)ctlMenuView.findViewById(R.id.tv_video_endPosition);
		seekBar = (SeekBar)ctlMenuView.findViewById(R.id.sb_video_during);
		seekBar.setMax(SeekMaxValue);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				VideoPlayActivity.mSeekTrackingFlag = false;
				float precent = (float)seekBar.getProgress()/seekBar.getMax();
				long playPostion = (long)(precent * VideoPlayActivity.mDuration);
				
				Message msg = Message.obtain();
				msg.what = PLAY_UPDATE;
				msg.obj  = playPostion;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				VideoPlayActivity.mSeekTrackingFlag = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		ctlClickListener listener = new ctlClickListener();
		btnPlay = (Button) ctlMenuView.findViewById(R.id.btn_video_play);
		btnPlay.setOnClickListener(listener);
		btnNext = (Button) ctlMenuView.findViewById(R.id.btn_video_fast);
		btnNext.setOnClickListener(listener);
		btnPrev = (Button) ctlMenuView.findViewById(R.id.btn_video_slow);
		btnPrev.setOnClickListener(listener);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);  
        surfaceView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctlActivity.showAtLocation(findViewById(R.id.fly_video_play), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});        
    }

    class ctlClickListener implements OnClickListener{    	
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		
    		if (VideoPlayActivity.mMediaPlayer == null) {
				return;
			}
    		
    		switch (v.getId()) {
			case R.id.btn_video_play:
				Log.i(tag, "play is working");
				if (VideoPlayActivity.mMediaPlayer.isPlaying()) {
					VideoPlayActivity.mMediaPlayer.pause();
				}
				else {
					VideoPlayActivity.mMediaPlayer.start();
				}
				break;
			case R.id.btn_video_fast:
				Log.i(tag, "fast is working");
				//ctlAct.dismiss();
				break;
			case R.id.btn_video_slow:
				Log.i(tag, "slow is working");
				//ctlAct.dismiss();
				break;
			default:
				break;
			}
    	}
    }    
    
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceCreated called");
		playVideo();		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int width, int height) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}		
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = BUFFER_UPDATE;
		msg.obj  = arg1;
		handler.sendMessage(msg);
		Log.i("buffer", String.valueOf(arg1));	
	}  

	private void playVideo()
	{
		mMediaPlayer = new MediaPlayer(this);
		mMediaPlayer.setBufferSize(1024*1024*50);
		try {
			mMediaPlayer.setDataSource(url);
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.setDisplay(holder);
		mMediaPlayer.prepareAsync();
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnVideoSizeChangedListener(this);
		mMediaPlayer.setOnInfoListener(this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);		
	}
		
	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}	
	
	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}

    private String long2HMS(long millSeconds)
    {
    	String tmp = "";
    	long hour = millSeconds/3600000;
    	long minute = (millSeconds%3600000)/60000;
    	long secord = ((millSeconds%3600000)%60000)/1000;
    	tmp = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(secord);
    	return tmp;
    }

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		//开始缓存，暂停直播
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mp.isPlaying()) {
				mp.stop();
			}
			break;
		//缓存完成，继续播放
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mp.start();
			break;
		//显示下载速度
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			Log.i(TAG, "download rate:" + extra);
			break;
		default:
			break;
		}
		return false;
	}
}
