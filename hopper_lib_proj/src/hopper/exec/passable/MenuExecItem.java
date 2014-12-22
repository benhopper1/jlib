package hopper.exec.passable;

import com.example.hopperlibrary.console;

public class MenuExecItem {
	private String keyString;
	private String caption;
	private int id;
	
	public MenuExecItem(String inKeyString) {
		keyString = inKeyString;
	}
	public MenuExecItem(String inKeyString, int inId) {
		keyString = inKeyString;
		id = inId;
	}
	public MenuExecItem(String inKeyString, int inId, String inCaption) {
		keyString = inKeyString;
		id = inId;
		caption = inCaption;
	}
	
	
	public void exec(Object inObject){onExec(inObject);}
	protected void onExec(Object inObject){console.log("MenuExecItem.onExec not overriden!!!");}
	
	
	
	
	
	
	//---getter setters:
	public String getKeyString() {
		return keyString;
	}
	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
