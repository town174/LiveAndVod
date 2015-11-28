package Model;

//点播中的录像文件信息
public class VodVedio{
	private int Number;
	private String Name;
	private String LowUrl;
	private String HighUrl;
    private float FileSize;
	private String FileDuration;
	private String StartTime;
	private String StopTime;
	private int ChannelID;
	
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getLowUrl() {
		return LowUrl;
	}
	public void setLowUrl(String lowUrl) {
		LowUrl = lowUrl;
	}
	public String getHighUrl() {
		return HighUrl;
	}
	public void setHighUrl(String highUrl) {
		HighUrl = highUrl;
	}
	public float getFileSize() {
		return FileSize;
	}
	public String getFileDuration() {
		return FileDuration;
	}
	public String getStartTime() {
		return StartTime;
	}
	public String getStopTime() {
		return StopTime;
	}
	public int getChannelID() {
		return ChannelID;
	}
	public void setFileSize(float fileSize) {
		FileSize = fileSize;
	}
	public void setFileDuration(String fileDuration) {
		FileDuration = fileDuration;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public void setStopTime(String stopTime) {
		StopTime = stopTime;
	}
	public void setChannelID(int channelID) {
		ChannelID = channelID;
	}

}
