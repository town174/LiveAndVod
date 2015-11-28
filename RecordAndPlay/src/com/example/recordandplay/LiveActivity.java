package com.example.recordandplay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.geniuseoe.spiner.demo.widget.AbstractSpinerAdapter.IOnItemSelectListener;
import com.geniuseoe.spiner.demo.widget.SpinerPopWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import Data.CfgData;
import Data.XmlDataParse;
import Model.LiveChannel;
import Model.LiveClass;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LiveActivity extends Activity {
	
	private Handler hangdler;
	private ArrayList<LiveClass> LiveTotalInfo = new ArrayList<>();
	private ChannelAdapt channelAdapt;
	private int clsPosition;//分类位置
	
	private ListView ChannelListView;	
	private SpinerPopWindow ClspopWindow;
	private TextView ClassValueTv;
	private ImageButton ClassDropDown;
	private List<String> ClassNameList = new ArrayList<String>();	
	private static final int LIVE_MSG_FLAG = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live);
		
		hangdler = new Handler(){
			@SuppressLint("ShowToast") @SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				//刷新UI
				case LIVE_MSG_FLAG:
					Toast.makeText(getApplicationContext(), "收到直播信息!", 1).show();
					LiveTotalInfo = (ArrayList<LiveClass>)msg.obj;
					for (LiveClass cls : LiveTotalInfo) {
						ClassNameList.add(cls.getName());
					}
					
					SetViews();
					
					ClspopWindow = new SpinerPopWindow(getApplicationContext());
					ClspopWindow.refreshData(ClassNameList, 0);
					ClspopWindow.setItemListener(new ClassAdapt());
					
					break;
				default:
					break;
				}
			};
		};
		
		ConnectServer();		
	}
	
	private void ConnectServer(){
		CfgData.GetCfgInfo(this);
		String ipString = CfgData.getServerIp();
		String portString = CfgData.getServerPort();
		
		try {
		String url = "http://" + ipString + ":" + portString + "/app/ddr.xml";
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(url, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int stateCode, Header[] arg1, byte[] resopnes) {
				// TODO Auto-generated method stub				
				InputStream isStream = new ByteArrayInputStream(resopnes);
				@SuppressWarnings("unchecked")
				ArrayList<LiveClass> total= (ArrayList<LiveClass>)XmlDataParse.Parser(isStream);
				Message msg = Message.obtain();
				msg.what = LIVE_MSG_FLAG;
				msg.obj = total;
				hangdler.sendMessage(msg);

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
				Toast.makeText(getApplicationContext(), "网络连接失败: " + statusCode, Toast.LENGTH_SHORT).show();
				System.out.println("错误代码： " + statusCode);
			}
		});
		
		} catch (Exception e) {
		// TODO: handle exception
		} finally {
		
		}		
	}
	
	private void SetViews()
	{
		DropDownClickLitener listener = new DropDownClickLitener();
		ClassValueTv = (TextView)findViewById(R.id.liveClassValue);
		ClassDropDown = (ImageButton)findViewById(R.id.liveClassDropDown);
		ClassDropDown.setOnClickListener(listener);		
		
		ChannelClickListen itemListener = new ChannelClickListen();
		ChannelListView = (ListView)findViewById(R.id.liveChannelList);
		ChannelListView.setOnItemClickListener(itemListener);
	}
	
	private class ClassAdapt implements IOnItemSelectListener{

		@Override
		public void onItemClick(int pos) {
			// TODO Auto-generated method stub
			if (pos >= 0 && pos <= ClassNameList.size()) {
				String clsName = ClassNameList.get(pos);
				ClassValueTv.setText(clsName);
				clsPosition = pos;
				
				ChannelAdapt adapt = new ChannelAdapt(getApplicationContext(), 
						LiveTotalInfo.get(pos).getChannelList());
				ChannelListView.setAdapter(adapt);
			}			
		}
		
	}

	private class DropDownClickLitener implements OnClickListener
	{
		@Override
		//public  SpinerPopWindow mSpinerPopWindow
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.liveClassDropDown:
				ClspopWindow.setWidth(ClassValueTv.getWidth());
				ClspopWindow.showAsDropDown(ClassValueTv);				
				break;
			default:
				break;
			}
		}
	}	
	
	private class ChannelAdapt extends BaseAdapter
	{
		private Context context;
		private ArrayList<LiveChannel> channelList;
		private LayoutInflater layoutInflater;//视图容器
		public class listItemView
		{
			public ImageView ChannelIamge;
			public TextView ChannelDuration;
			public TextView ChannelName;
		}
		
		public ChannelAdapt(Context ct, ArrayList<LiveChannel> list)
		{
			context = ct;
			channelList = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return channelList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//自定义视图
			listItemView liv;
			//设置返回View
			if (convertView == null) 
			{
				liv = new listItemView();
				//获取list_item布局文件的视图
				convertView = (View)layoutInflater.from(context).inflate(R.layout.listview_item_layout, null);
				//获取控件对象
				liv.ChannelIamge = (ImageView)convertView.findViewById(R.id.VideoBreviaryIV);
				liv.ChannelDuration = (TextView)convertView.findViewById(R.id.VideoDurationTV);
				liv.ChannelName = (TextView)convertView.findViewById(R.id.VideoNameTV);
				//设置控件到convertView
				convertView.setTag(liv);
			}
			else
			{
				liv = (listItemView)convertView.getTag();
			}
			//从vedioList设置文字和图片到liv
			//liv.ChannelDuration.setText(channelList.get(position));
			liv.ChannelName.setText(channelList.get(position).getName());
			
			return convertView;
		}
		
	}

	private class ChannelClickListen implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), 
					//VodTotalInfo.get(ClassPosition).getProgramList().get(ProgramPosition).getVedioList().get(position).getName(), 
					//Toast.LENGTH_LONG).show();
			String urlString =
					LiveTotalInfo.get(clsPosition).getChannelList().get(position).getHighUrl();
					//VodTotalInfo.get(ClassPosition).getProgramList().get(ProgramPosition).getVedioList().get(position).getHighUrl();
			
			Intent intent = new Intent(getApplicationContext(),VideoPlayV2Activity.class);//VideoPlayActivity.class);
			intent.putExtra("uri", urlString);
			intent.putExtra("isLive", true);
			startActivity(intent);			
		}
		
	}
}
