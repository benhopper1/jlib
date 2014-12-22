package hopper.exec.passable;



import com.example.hopperlibrary.console;
//implements Serializable
public class ExecObject {
	public String keyString;
	public ExecObject(String inKeyString) {
		keyString = inKeyString;
	}
	
	
	public void exec(Object inObject){onExec(inObject);}
	protected void onExec(Object inObject){console.log("execObject.onExec not overriden!!!");}

}
