package com.example.recordandplay;

import io.vov.vitamio.R.string;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import Data.CfgData;
import Data.XmlDataParse;
import Model.VodClass;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends TabActivity {

	private TabHost tabHost;
	private RadioGroup mainGroup;
	private RadioButton BIRadioButton,VodRadioButton,liveRadioButton,setRadioButton;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CfgData.setContext(this);
        //CfgData.GetCfgInfo();
        
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, BaseInfoActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, VodActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, LiveActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, SettingActivity.class)));
        
        mainGroup = (RadioGroup)findViewById(R.id.tab_BtnGroup);
        
        BIRadioButton = (RadioButton)findViewById(R.id.tab_icon_baseInfo);
        VodRadioButton = (RadioButton)findViewById(R.id.tab_icon_vod);
        liveRadioButton = (RadioButton)findViewById(R.id.tab_icon_live);
        setRadioButton = (RadioButton)findViewById(R.id.tab_icon_set);
        
        CheckListener listener = new CheckListener();
        mainGroup.setOnCheckedChangeListener(listener);
        
        /*try {
			String url = "http://192.168.100.142:8080/app/dbr.xml";
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.get(url, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(int stateCode, Header[] arg1, byte[] resopnes) {
					// TODO Auto-generated method stub
					//super.onSuccess(arg0, arg1, arg2);
					
					InputStream isStream = new ByteArrayInputStream(resopnes);
					@SuppressWarnings("unchecked")
					ArrayList<VodClass> total= (ArrayList<VodClass>)XmlDataParse.Parser(isStream);
					System.out.println(total.get(0).getProgramList().get(0).getVedioList().get(0).getName());
					try {
						isStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				@Deprecated
				public void onFailure(int statusCode, Header[] headers,
						Throwable error, String content) {
					// TODO Auto-generated method stub
					//super.onFailure(statusCode, headers, error, content);
					Toast.makeText(getApplicationContext(), "网络连接失败: " + statusCode, Toast.LENGTH_SHORT).show();
					System.out.println("错误代码： " + statusCode);
				}
			});
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}*/
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	//CfgData.SetCfgInfo();
    }
    
    public class CheckListener implements OnCheckedChangeListener
    {
    	@Override
    	public void onCheckedChanged(RadioGroup group, int checkedId) {
    		// TODO Auto-generated method stub
    		//setCurrentTab 通过标签索引设置当前显示的内容
    		//setCurrentTabByTag 通过标签名设置当前显示的内容
    		switch (checkedId) {
			case R.id.tab_icon_baseInfo:
				tabHost.setCurrentTab(0);
				break;
			case R.id.tab_icon_vod:
				tabHost.setCurrentTab(1);
				break;
			case R.id.tab_icon_live:
				tabHost.setCurrentTab(2);
				break;
			case R.id.tab_icon_set:
				tabHost.setCurrentTab(3);
				break;
			default:
				break;
			}
    	}
    }
}
