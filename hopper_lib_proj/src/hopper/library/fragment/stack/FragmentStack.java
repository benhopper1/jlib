package hopper.library.fragment.stack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.example.hopperlibrary.console;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

public class FragmentStack {
	private Context context;
	private int mainFragmentContainerId;
	private ArrayList<Fragment> arrayList = new ArrayList<Fragment>();
	//private Fragment baseFragment = null;
	
	//=================================================================================================================
	// STATIC AREA ----- MULTI-TON PATTERN
	//=================================================================================================================
	static private HashMap<String, FragmentStack> fragmentStackGroupHashMap = new  HashMap<String, FragmentStack>();
	static public FragmentStack getMaybeCreate(String inFragmentStackGroupName, Context inContext, int inMainFragmentContainerId){
		FragmentStack theInstance = fragmentStackGroupHashMap.get(inFragmentStackGroupName);
		if(theInstance == null){
			theInstance = new FragmentStack(inContext, inMainFragmentContainerId);
			fragmentStackGroupHashMap.put(inFragmentStackGroupName, theInstance);
		}
		return theInstance;
	}
	static public FragmentStack getInstance(String inFragmentStackGroupName){
		return fragmentStackGroupHashMap.get(inFragmentStackGroupName);
	}
	
	
	
	//static public FragmentStack testInstance;
	/*static public FragmentStack getTestInstaqnce(){
		return testInstance;
	}*/
	
	private FragmentStack(Context inContext, int inMainFragmentContainerId){
		context = inContext;
		mainFragmentContainerId = inMainFragmentContainerId;
		
		/*FragmentTransaction fragmentTransaction = ((Activity)context).getFragmentManager().beginTransaction();
		fragmentTransaction.add(inMainFragmentContainerId, mainFragment);*/
	}
	
	
	public void showThis(Fragment inFragment){
		FragmentTransaction fragmentTransaction = ((Activity)context).getFragmentManager().beginTransaction();
		fragmentTransaction.replace(mainFragmentContainerId, inFragment);
		fragmentTransaction.commit();
	}
		
	
	//reset
	public void showBase(){
		if(arrayList.size() < 2){return;}
		Fragment tmpFragment = arrayList.get(0);
		arrayList.clear();
		arrayList.add(tmpFragment);
	}
	
	public void showTop(){
		if(arrayList.isEmpty()){return;}		
		showThis(arrayList.get(arrayList.size()-1));		
	}
	
	public void back(){
		console.log("==============================================================================================");
		console.log("------back fragment-----------------------------");
		if(arrayList.size() < 2){return;}
		arrayList.remove(arrayList.size()-1);
		showTop();
	}
	
	public void add(Fragment inFragment){
		console.log("==============================================================================================");
		console.log("------add fragment-----------------------------");
		arrayList.add(inFragment);
		showTop();
	}
	
	
	
	
	
	
	
	
	
	
}




/*android.app.FragmentManager fm = getFragmentManager();
FragmentTransaction fragmentTransaction = fm.beginTransaction();		
fragmentTransaction.replace(R.id.fragment1, inFragmentInstance);
fragmentTransaction.addToBackStack(null);
fragmentTransaction.commit();*/