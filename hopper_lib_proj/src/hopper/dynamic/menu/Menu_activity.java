package hopper.dynamic.menu;



import hopper.exec.passable.MenuExecItem;
import hopper.global.tools.GlobalMessage;

import java.util.ArrayList;

import com.example.hopperlibrary.console;

import hopper.library.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ListView;


//hopper.dynamic.menu.Menu_activity
public class Menu_activity extends Activity {
	private Context context;
	private ListView listView;
	private Menu_arrayAdapter arrayAdapter;
	
	

	@Override
	protected void onDestroy() {
		GlobalMessage.removeObject("Menu_activity");
		super.onDestroy();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.menu_layout);
        this.context = getApplicationContext();
        GlobalMessage.putObject("Menu_activity", this);
        
        
        MenuBaseClass MenuClassInstance = (MenuBaseClass) GlobalMessage.getObject("MenuClassInstance");
        if(MenuClassInstance == null){console.log("MenuClassInstance NULL");}else{console.log("MenuClassInstance NOT NULL");}
        
        ArrayList<MenuExecItem> menuArrayList = MenuClassInstance.getArrayList(); //new ArrayList<MenuExecItem>();
        if(menuArrayList == null){console.log("menuArrayList NULL");}else{console.log("menuArrayList NOT NULL");}
        
        
        console.log("MENU TEST POINT",0);
        
        arrayAdapter = new Menu_arrayAdapter(context, menuArrayList, R.layout.menu_item_layout);       
        listView = (ListView) findViewById(R.id.lv_menu);
        listView.setAdapter(arrayAdapter);      
        listView.invalidateViews();
      
        
        
        

	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    // If we've received a touch notification that the user has touched
	    // outside the app, finish the activity.
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	      //finish();
	      return true;
	    }

	    // Delegate everything else to Activity.
	   // return super.onTouchEvent(event);
	    return true;
	  }
	

}
