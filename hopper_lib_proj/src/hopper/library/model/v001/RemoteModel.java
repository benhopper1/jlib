package hopper.library.model.v001;

public abstract class RemoteModel{
	protected Object data;
	

	public RemoteModel() {
		// TODO Auto-generated constructor stub
	}
	
	// using cache
	public Object getData(){
		return onGetData();		
	}
	
	public void setData(Object inData){
		data = inData;		
	}
	
	// reads from db and generates new data
	public void refresh(){
		onRefresh();		
	}
	
	
	
	// should be over Override
	protected abstract void onRefresh();
	
	protected Object onGetData(){
		return data;		
	}

}
