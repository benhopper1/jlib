package hopper.library.communication.v003;



import java.util.ArrayList;

public class HopperFilter {
	private String key;
	private String Value;	
	public HopperFilter(String inKey, String inValue) {
		if(inKey != null){
			this.key = inKey;
		}else {
			this.key = "";
		}
		if(inValue != null){
			this.Value = inValue;
		}else{
			this.Value = "";
		}		
	}
	public HopperFilter(String inKey, String inValue, OnFilterPassListener inOnFilterPassListener){
		if(inKey != null){
			this.key = inKey;
		}else {
			this.key = "";
		}
		if(inValue != null){
			this.Value = inValue;
		}else{
			this.Value = "";
		}		
		this.setOnFilterPassListener(inOnFilterPassListener);
	}
	
	
	//OnFilterPassListener
	
	
    //-------events----------------------------------------------------------------------------------------------------------------------
	private ArrayList<OnFilterPassListener> onFilterPassListenerArrayList = new ArrayList<OnFilterPassListener>();
	
	public interface OnFilterPassListener{
	    public void onFilterPass(Object...objects);
	}
    public void setOnFilterPassListener(OnFilterPassListener listener) {
    	onFilterPassListenerArrayList.add(listener);
    }
    private void reportOnFilterPass(Object...objects){
    	for(OnFilterPassListener listener : onFilterPassListenerArrayList){
    		listener.onFilterPass(objects);
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
	
	public void execute(Object...objects){		
		this.reportOnFilterPass(objects);
	}
	

}
