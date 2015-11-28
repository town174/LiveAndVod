package com.example.recordandplay;

import com.example.recordandplay.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class VideoCtlActivity extends PopupWindow {

	private View mMenuView;
	private Button btnPlay, btnPrev, btnNext;
	private SeekBar seekBar;
	private TextView tvstartPst,tvendPst;
	
    @SuppressWarnings("deprecation")
	public VideoCtlActivity(Activity context,Handler handler) {  
        super(context);               
        
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.video_control, null);  
        //ctlClickListener listener = new ctlClickListener(this,handler);
        //btnPlay = (Button) mMenuView.findViewById(R.id.btn_video_play);
        //btnPlay.setOnClickListener(listener);
        //btnNext = (Button) mMenuView.findViewById(R.id.btn_video_fast);
        //btnNext.setOnClickListener(listener);
        //btnPrev = (Button) mMenuView.findViewById(R.id.btn_video_slow);  
        //btnPrev.setOnClickListener(listener);
        //seekBar = (SeekBar)mMenuView.findViewById(R.id.sb_video_during);
        //seekBar.setMax(100);
        
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        //this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为透明  
        ColorDrawable dw = new ColorDrawable(0x00000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {                
            public boolean onTouch(View v, MotionEvent event) {                    
                int heightOfButtom = mMenuView.findViewById(R.id.rly_video_bottom).getTop();
                int heightOfTop = mMenuView.findViewById(R.id.rly_video_top).getBottom();
                int y=(int) event.getY();  
                
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y < heightOfButtom && y > heightOfTop){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
    }  

}
