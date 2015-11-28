package Model;

import java.util.ArrayList;

public class VodClass {
	private int ID;
	private String Name;
	private ArrayList<VodProgram> programList;
	
	public VodClass()
	{
		programList = new ArrayList<>();
	}
	
	public int getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public ArrayList<VodProgram> getProgramList() {
		return programList;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setProgramList(VodProgram program) {
		this.programList.add(program);
	}
}
