package com.example.recordandplay;

import Data.CfgData;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends Activity {
	
	private EditText edIpText,edPortText,edBufferText;
	private Button btnSure;
	String ipString,portString,bufferString;
	//Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		
		//intent = getIntent();
		//ipString = intent.getStringExtra("IP");
		//portString = intent.getStringExtra("PORT");
		CfgData.GetCfgInfo(this);
		ipString = CfgData.getServerIp();
		portString = CfgData.getServerPort();
		bufferString = CfgData.getBufferSize();
		
		edIpText = (EditText)findViewById(R.id.et_content_ip);
		edIpText.setText(ipString);
		edPortText = (EditText)findViewById(R.id.et_content_port);
		edPortText.setText(portString);
		edBufferText = (EditText)findViewById(R.id.et_content_buffersize);
		edBufferText.setText(bufferString);
		btnSure = (Button)findViewById(R.id.btn_setting_sure);
		
		OnClickListener onClickListener = new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//intent.putExtra("IP", edIpText.getText());
				//intent.putExtra("PORT", edPortText.getText());
				//setResult(1, intent);
				//finish();
				CfgData.SetCfgInfo(getApplicationContext(), 
						edIpText.getText().toString(), 
						edPortText.getText().toString(),
						edBufferText.getText().toString());
			}
		};
		btnSure.setOnClickListener(onClickListener);
	}
}
