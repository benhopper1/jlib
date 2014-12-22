package hopper.library.communication.v003;

import hopper.library.communication.v003.HopperFilter.OnFilterPassListener;
import hopper.library.communication.v003.WebSocketService.OnMessagedListener;
import hopper.library.time.MessureMe;

import java.util.HashMap;

import com.example.hopperlibrary.console;
// MULTI-TON object (many form of singlton using hash as a heap-keeper) 
public class FilterSetProcessor {
	static private HashMap<String, FilterSetProcessor> multitonHash = new HashMap<String, FilterSetProcessor>();
	private HashMap<String, HopperFilterSet> filterSetHash;
	public WebSocketService socketService;
	
	static public FilterSetProcessor getMaybeCreate(String inName,WebSocketService inService){		
		FilterSetProcessor instanceOfFilterSetProcessor = multitonHash.get(inName);
		if(instanceOfFilterSetProcessor == null){
			instanceOfFilterSetProcessor = new FilterSetProcessor(inService);
			multitonHash.put(inName, instanceOfFilterSetProcessor);
		}
		return instanceOfFilterSetProcessor;
	}
	
	static public FilterSetProcessor getInstance(String inName){
		return multitonHash.get(inName);
	}
	
	private FilterSetProcessor(WebSocketService inService){
		filterSetHash = new HashMap<String, HopperFilterSet>();
		this.socketService = inService;
	}
	
	public void addFilterSet(String inKey, HopperFilterSet inFilterSet){
		filterSetHash.put(inKey, inFilterSet);
	}
	
	public void removeFilterSet(String inKey){
		filterSetHash.remove(inKey);
	}
	
	public WebSocketService getSocketService(){
		return this.socketService;
	}
	

	private static int processorCount = 0;
	public void processorStart(){
		console.loge("-------> PROCESSOR NUMBER " + processorCount + " STARTED !!!!!!!!!!!! <-----------");
		socketService.setOnMessagedListener(new OnMessagedListener(){
			@Override
			public void onMessaged(Object... objects) {								
				String routingLayer_type = JsonQuickParser.quickParseFieldForLayer("routingLayer", "type", (String)objects[1]);
				if(routingLayer_type.equalsIgnoreCase("tokenToTokenUseFilter")){
					//assuming filtekey == filter, may need to change in future?? test filtervalue only for now, until exotic protocols				
					String fromServerFilterKey = "filter";
					String fromServerFilterValue = JsonQuickParser.quickParseFieldForLayer("routingLayer", "filterValue", (String)objects[1]).trim();
					
					for(HopperFilterSet filterSet : filterSetHash.values()){					
						HopperFilter filter = filterSet.getFilter(fromServerFilterKey,fromServerFilterValue);
						if(filter != null){							
							filter.execute(objects);
						}						
					}					
				}
			}
		});
	}
	
	public void processorStop(){
		
	}
	
	public FilterSetProcessor add(String inFilterSetName, String inFilterKey, String inFilterValue, OnFilterPassListener inOnFilterPassListener){
		HopperFilterSet filterSet = filterSetHash.get(inFilterSetName);
		if(filterSet == null){
			filterSet = new HopperFilterSet(new HopperFilter(inFilterKey, inFilterValue, inOnFilterPassListener));
		}else{
			filterSet.addFilter(new HopperFilter(inFilterKey, inFilterValue, inOnFilterPassListener));
		}
		
		this.addFilterSet(inFilterSetName, filterSet);		
		return this;
	}
	
	
	
	
}






























