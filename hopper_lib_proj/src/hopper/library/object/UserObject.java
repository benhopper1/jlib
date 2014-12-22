package hopper.library.object;


import java.util.HashMap;

public class UserObject {
	private HashMap<String, String> propertiesHash = new HashMap<String, String>();
	private int id = -1;
	public UserObject() {
		// TODO Auto-generated constructor stub
	}	
	public int getId(){return id;}
	public void setId(int inId){id = inId;}
	
	public void addProperty(String inKey, String inValue){
		propertiesHash.put(inKey, inValue);
	}
	public String getProperty(String inKey){
		return propertiesHash.get(inKey);
	}
}
