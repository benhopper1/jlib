package hopper.dynamic.menu;

import hopper.exec.passable.MenuExecItem;


import java.util.ArrayList;

import com.example.hopperlibrary.console;
import hopper.library.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class Menu_arrayAdapter extends ArrayAdapter<MenuExecItem>  {
	private Context context;
	private ArrayList<MenuExecItem> menuArrayList;
	private int item_layout_resourceId;

	public Menu_arrayAdapter(Context inContext, ArrayList<MenuExecItem> inMenuArrayList, int inResourceId) {
		super(inContext, inResourceId, inMenuArrayList);
		context = inContext;
		menuArrayList = inMenuArrayList;
		item_layout_resourceId = inResourceId;
	}
	
	@SuppressLint("NewApi")
	public void setUserArrayList(ArrayList<MenuExecItem> inUserArrayList){
		menuArrayList = inUserArrayList;
		clear();
		addAll(menuArrayList);
		console.log("set  menuArrayList--------------");
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		console.log("getView position:", position);
		console.log("position:"+String.valueOf(position)+"arraySize:"+String.valueOf(menuArrayList.size()));
		
		final MenuExecItem menuExecItem = menuArrayList.get(position);
		View v = convertView;
		
		if (v == null) {
			console.log("getView testpoint_1");
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			console.log("getView testpoint_1a");
			v = inflater.inflate(item_layout_resourceId, null);	
		}
		
		Button bt_menu_item = (Button) v.findViewById(R.id.bt_menu_item);
		
		bt_menu_item.setText(menuExecItem.getCaption());
		
		bt_menu_item.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				console.log("command button--click", position);				
				menuExecItem.exec(null);				
			}
		});
		
	
		return v;
	}
	
	
	
	
	

}
