package Model;

import java.util.ArrayList;

//Â¼ÏñÄ¿Â¼
public class VodProgram {
	private int ID;
	private String Name;
	private int ChannelID;
	private ArrayList<VodVedio> vedioList;
	
	public VodProgram()
	{
		vedioList = new ArrayList<>();
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
	public ArrayList<VodVedio> getVedioList() {
		return vedioList;
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
	public void setVedioList(VodVedio vedio) {
		this.vedioList.add(vedio);
	}
}
