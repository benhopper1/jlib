package hopper.dynamic.menu;

import java.util.ArrayList;

import com.example.hopperlibrary.HopperInstanceHash;
import com.example.hopperlibrary.console;

import hopper.exec.passable.MenuExecItem;
import hopper.global.tools.GlobalMessage;
import android.content.Intent;
import android.app.Activity;
public class MenuBaseClass {
	
	private ArrayList<MenuExecItem> arrayList;

	public MenuBaseClass() {
		GlobalMessage.putObject("MenuClassInstance", this);
		//######################################################################
		//----activity LOADER---------------------------------------------------		
		Intent intent = new Intent(HopperInstanceHash.getInstance("MainActivity"),Menu_activity.class);
		HopperInstanceHash.getInstance("MainActivity").startActivity(intent); 
		//###################################################################### 
	}
	
	public ArrayList<MenuExecItem> getArrayList(){
		ArrayList<MenuExecItem> tmpArrayList = onGetArrayList();
		if(arrayList != null){
			tmpArrayList.addAll(tmpArrayList);
		}		
		return tmpArrayList;
	}
	
	public void add(MenuExecItem inMenuExecItem){
		arrayList.add(inMenuExecItem);	
	}
	
	public void close(){
		Activity Menu_activity = (Activity) GlobalMessage.getAndRemoveObject("Menu_activity");
		if(Menu_activity != null){
			Menu_activity.finish();
		}
	}
	
	
	
	protected int layout(){return 0;}
	
	
	protected ArrayList<MenuExecItem> onGetArrayList(){
		ArrayList<MenuExecItem> tmpArrayList = new ArrayList<MenuExecItem>();		
	    return tmpArrayList;	   
	}
	
	public void show(){
		
	}


}