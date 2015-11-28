package com.example.recordandplay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.geniuseoe.spiner.demo.widget.AbstractSpinerAdapter;
import com.geniuseoe.spiner.demo.widget.SpinerPopWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import Data.CfgData;
import Data.XmlDataParse;
import Model.VodClass;
import Model.VodProgram;
import Model.VodVedio;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

public class VodActivity extends Activity{
	
	private Handler hangdler;
	private ArrayList<VodClass> VodTotalInfo = new ArrayList<>();
	private VideoAdapt videoAdapt;
	private int ClassPosition;//分类位置
	private int ProgramPosition;//程序集位置
	private final static int VOD_MSG_FLAG = 1;
	
	private ListView videoListView;
	
	private SpinerPopWindow ClspopWindow;
	private TextView ClassValueTv;
	private ImageButton ClassDropDown;
	private List<String> ClassNameList = new ArrayList<String>();
	
	private SpinerPopWindow PrgPopWindow;
	private TextView PrgValueTv;
	private ImageButton PrgDropDown;
	private List<String> PrgNameList = new ArrayList<String>();
	
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod);
		
		hangdler = new Handler(){
			@SuppressLint("ShowToast") @SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				//跟新UI
				case VOD_MSG_FLAG:
					Toast.makeText(getApplicationContext(), "收到点播信息!", 1).show();
					VodTotalInfo = (ArrayList<VodClass>)msg.obj;
					for (VodClass cls : VodTotalInfo) {
						ClassNameList.add(cls.getName());
					}
					
					SetViews();
					
					ClspopWindow = new SpinerPopWindow(getApplicationContext());
					ClspopWindow.refreshData(ClassNameList, 0);
					ClspopWindow.setItemListener(new ClsAdapt());
					
					PrgPopWindow = new SpinerPopWindow(getApplicationContext());
					PrgPopWindow.refreshData(PrgNameList, 0);
					PrgPopWindow.setItemListener(new PrgAdapt());
					//SetData2Spinner((ArrayList<VodClass>)msg.obj);
					break;
				default:
					break;
				}
			};
		};
		
		ConnectServer();
	}
	
	void ConnectServer()
	{
		CfgData.GetCfgInfo(this);
		String ipString = CfgData.getServerIp();
		String portString = CfgData.getServerPort();
		
		try {
		String url = "http://" + ipString + ":" + portString + "/app/dbr.xml";
				//"http://192.168.100.142:8080/app/dbr.xml";
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(url, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int stateCode, Header[] arg1, byte[] resopnes) {
				// TODO Auto-generated method stub
				
				InputStream isStream = new ByteArrayInputStream(resopnes);
				@SuppressWarnings("unchecked")
				ArrayList<VodClass> total= (ArrayList<VodClass>)XmlDataParse.Parser(isStream);
				Message msg = Message.obtain();
				msg.what = VOD_MSG_FLAG;
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
				//super.onFailure(statusCode, headers, error, content);
				Toast.makeText(getApplicationContext(), "网络连接失败: " + statusCode, Toast.LENGTH_SHORT).show();
				System.out.println("错误代码： " + statusCode);
			}
		});
		
		} catch (Exception e) {
		// TODO: handle exception
		} finally {
		
		}
	}
	
	void SetData2Spinner(ArrayList<VodClass> total)
	{
		ArrayList<String> strList = new ArrayList<>();
		for (VodClass cls : total) {
			strList.add(cls.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strList);
		//vodClassSpinner.setAdapter(adapter);
		
		ArrayList<String> strList2 = new ArrayList<>();
		for (VodProgram prg : total.get(0).getProgramList()) {
			strList2.add(prg.getName());
		}
		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strList2);
		//vodListsSpinner.setAdapter(adapter2);
	}
	
	void SetViews()
	{
		DropDownClickLitener listener = new DropDownClickLitener();
		ClassValueTv = (TextView)findViewById(R.id.vodClassValue);
		ClassDropDown = (ImageButton)findViewById(R.id.vodClassDropDown);
		ClassDropDown.setOnClickListener(listener);
		PrgValueTv = (TextView)findViewById(R.id.vodPrgValue);
		PrgDropDown = (ImageButton)findViewById(R.id.vodPrgDropDown);
		PrgDropDown.setOnClickListener(listener);
		

		VideoClickListener itemListener = new VideoClickListener();
		videoListView = (ListView)findViewById(R.id.vodVideoList);
		videoListView.setOnItemClickListener(itemListener);
	}
	
	private class DropDownClickLitener implements OnClickListener
	{
		@Override
		//public  SpinerPopWindow mSpinerPopWindow
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.vodClassDropDown:
				ClspopWindow.setWidth(ClassValueTv.getWidth());
				ClspopWindow.showAsDropDown(ClassValueTv);
				break;
			case R.id.vodPrgDropDown:
				PrgPopWindow.setWidth(PrgValueTv.getWidth());
				PrgPopWindow.showAsDropDown(PrgValueTv);
				break;
			default:
				break;
			}
		}
	}
	
	private class ClsAdapt implements AbstractSpinerAdapter.IOnItemSelectListener
	{
		@Override
		public void onItemClick(int pos) {
			// TODO Auto-generated method stub
			if (pos >= 0 && pos <= ClassNameList.size()) {
				String clsName = ClassNameList.get(pos);
				ClassValueTv.setText(clsName);
				ClassPosition = pos;
				
				PrgNameList.clear();
				PrgValueTv.setText("");
				
				for (VodProgram prg : VodTotalInfo.get(pos).getProgramList()) {
					PrgNameList.add(prg.getName());
				}
			}
		}
	}
	
	private class PrgAdapt implements AbstractSpinerAdapter.IOnItemSelectListener
	{
		@Override
		public void onItemClick(int pos) {
			// TODO Auto-generated method stub
			if (pos >= 0 && pos <= PrgNameList.size()) {
				PrgValueTv.setText(PrgNameList.get(pos));
				ProgramPosition = pos;
				
				videoAdapt = new VideoAdapt(getApplicationContext(),
						VodTotalInfo.get(ClassPosition).getProgramList().get(pos).getVedioList());
				videoListView.setAdapter(videoAdapt);
				//videoAdapt.ReFreshData(
						//VodTotalInfo.get(ClassPosition).getProgramList().get(pos).getVedioList());
			}
		}
	}

	private class VideoAdapt extends BaseAdapter{
		
		private Context context;
		private ArrayList<VodVedio> vedioList;
		private LayoutInflater layoutInflater;//视图容器

		public class listItemView {
			public ImageView ViDeoIamge;
			public TextView VideoDuration;
			public TextView ViDeoName;
		}
		
		public VideoAdapt(Context ct, ArrayList<VodVedio> list) {
			context = ct;
			vedioList = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vedioList.size();
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

		@SuppressLint("InflateParams") @SuppressWarnings("null")
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
				liv.ViDeoIamge = (ImageView)convertView.findViewById(R.id.VideoBreviaryIV);
				liv.VideoDuration = (TextView)convertView.findViewById(R.id.VideoDurationTV);
				liv.ViDeoName = (TextView)convertView.findViewById(R.id.VideoNameTV);
				//设置控件到convertView
				convertView.setTag(liv);
			}
			else
			{
				liv = (listItemView)convertView.getTag();
			}
			//从vedioList设置文字和图片到liv
			liv.VideoDuration.setText(vedioList.get(position).getFileDuration());
			liv.ViDeoName.setText(vedioList.get(position).getName());
			//convertView设置监听事件
			
			return convertView;
		}
		
		public void ReFreshData(ArrayList<VodVedio> tmp)
		{
			vedioList = tmp;
		}
	} 

	private class VideoClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), 
					VodTotalInfo.get(ClassPosition).getProgramList().get(ProgramPosition).getVedioList().get(position).getName(), 
					Toast.LENGTH_LONG).show();
			String urlString = 
					VodTotalInfo.get(ClassPosition).getProgramList().get(ProgramPosition).getVedioList().get(position).getHighUrl();
			
			Intent intent = new Intent(getApplicationContext(),VideoPlayV2Activity.class);//VideoPlayActivity.class);
			intent.putExtra("uri", urlString);
			intent.putExtra("isLive", false);
			startActivity(intent);
		}
		
	}
}
