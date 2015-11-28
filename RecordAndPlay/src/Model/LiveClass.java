package Model;

import java.util.ArrayList;

public class LiveClass {
	private int ID;
	private String Name;
	private int ChannelID;
	private ArrayList<LiveChannel> ChannelList;
	
	public LiveClass()
	{
		ChannelList = new ArrayList<>();
	}
	
	public int getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public int getChannelID() {
		return ChannelID;
	}
	public ArrayList<LiveChannel> getChannelList() {
		return ChannelList;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setChannelID(int channelID) {
		ChannelID = channelID;
	}
	public void setChannelList(LiveChannel vedio) {
		this.ChannelList.add(vedio);
	}
}
