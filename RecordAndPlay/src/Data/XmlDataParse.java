package Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import Model.LiveChannel;
import Model.LiveClass;
import Model.VodClass;
import Model.VodProgram;
import Model.VodVedio;

import android.util.Xml;

//关于直播和点播的返回文档解析,返回directList

/*写代码的时候觉得中间解析部分最有可能出问题,反复检查。
     写完后测试，反而是开头忘记设置输入导致无输出，循环结束忘记给type赋值导致死循环，
     测试xml文档的编码不正确导致乱码，到是这几个看似简单的问题影响了结果。
     以后写完代码要通读一遍再调试，避免上述类似问题影响效率*/
public class XmlDataParse{
	public static Object Parser(InputStream is)
	{
		//初始化pull解析器,liveDirector,vodDirector
		XmlPullParser xpParser = Xml.newPullParser();
		
		ArrayList<LiveClass> liveTotalClasses = null;//直播类集合
		LiveClass liveClass = null;//直播类
		LiveChannel liveChannel;//直播频道
		
		ArrayList<VodClass>  vodTotalClasses = null;//点播类集合
		VodClass vodClass = null;//点播类
		VodProgram vodProgram = null;//点播程序
		VodVedio vodVedio;//点播节目
		
		Boolean isLive = false;//直播标志
		
		try {
			
			//忘记设置输入，要注意！
			xpParser.setInput(is, "UTF-8");
			
			//获得初始节点的事件类型
			int type = xpParser.getEventType();
			//当节点不是结束节点，一直解析
			while(type != XmlPullParser.END_DOCUMENT)
			{
				//获得节点名称
				String nodeName = xpParser.getName();
				//判断节点事件类型
				switch (type) {
				case XmlPullParser.START_TAG:
					//获得节点名称
					if ("Asset".equals(nodeName)) {
						if ("LIVE".equals(xpParser.getAttributeValue(null, "Type"))) {
							liveTotalClasses = new ArrayList<>();
							isLive = true;
						}
						else if ("VOD".equals(xpParser.getAttributeValue(null, "Type"))) {
							vodTotalClasses = new ArrayList<>();
							isLive = false;
						}
					} 
					else if ("Class".equals(nodeName)) {
						if (isLive){
							liveClass = new LiveClass();
							liveClass.setID(Integer.parseInt( xpParser.getAttributeValue(null, "ID")));
							liveClass.setName(xpParser.getAttributeValue(null, "Name"));
						}
						else if (isLive == false) {
							vodClass = new VodClass();
							vodClass.setID(Integer.parseInt( xpParser.getAttributeValue(null, "ID")));
							vodClass.setName(xpParser.getAttributeValue(null, "Name"));
						}
					} 
					else if ("Program".equals(nodeName)) {
						vodProgram = new VodProgram();
						vodProgram.setID(Integer.parseInt( xpParser.getAttributeValue(null, "ID")));
						vodProgram.setName(xpParser.getAttributeValue(null, "Name"));
						vodProgram.setChannelID(Integer.parseInt( xpParser.getAttributeValue(null, "ChanID")));
					} 
					//录制文件，点播基本单位
					else if ("Serial".equals(nodeName)) {
						vodVedio = new VodVedio();
						vodVedio.setNumber(Integer.parseInt( xpParser.getAttributeValue(null, "No")));
						vodVedio.setName(xpParser.getAttributeValue(null, "Name"));
						vodVedio.setFileDuration(xpParser.getAttributeValue(null, "Duration"));
						vodVedio.setLowUrl(xpParser.getAttributeValue(null, "LowURL"));
						vodVedio.setHighUrl(xpParser.getAttributeValue(null, "HiURL"));
						vodProgram.setVedioList(vodVedio);
					} 
					//通道，直播基本单位
					else if ("Channel".equals(nodeName)) {
						liveChannel = new LiveChannel();
						liveChannel.setId(Integer.parseInt( xpParser.getAttributeValue(null, "No")));
						liveChannel.setName(xpParser.getAttributeValue(null, "Name"));
						liveChannel.setLowUrl(xpParser.getAttributeValue(null, "LowURL"));
						liveChannel.setHighUrl(xpParser.getAttributeValue(null, "HiURL"));
						liveClass.setChannelList(liveChannel);
					}
					break;
				case XmlPullParser.END_TAG:
					if ("Program".equals(nodeName)) {
						vodClass.setProgramList(vodProgram);
					}
					else if ("Class".equals(nodeName)) {
						if (!isLive) {
							vodTotalClasses.add(vodClass);
						}
						else {
							liveTotalClasses.add(liveClass);
						}
					}
					break;
				default:
					break;
				}
				
				try {
					//忘记赋值给type，导致死循环，小心
					type = xpParser.next();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (isLive) {
			return liveTotalClasses;
		}
		else {
			return vodTotalClasses;
		}
		
	}
}
