package Model;

//直播通道
public class LiveChannel {
	private int Id;
	private String Name;
	private String LowUrl;
	private String HighUrl;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
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

	//直播和录像都是发送HttpUri给服务器,直播是录像的前提，然后获取直播url在本地播放
	public int BeginLive(){return 1;}
	public int EndLive(){return 1;}
	public int BeginRecord(){return 1;}
	public int EndRecord(){return 1;}
	//public int BeginPlay(){return 1;}
	//public int EndPlay(){return 1;}
}

