package com.example.hopperlibrary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView; 
import android.widget.ArrayAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
//import android.widget.ListAdapter;

public class ListViewHelper {	
	public ListView listView;
	public Context context;
	public ArrayList<String> arrayList;
	public ArrayAdapter<String> arrayAdapter;
	public String[] stringArray;
	public Map<Long,String> tagMap;
	public int currentIndex;
	
	public ListViewHelper(){}
	public ListViewHelper(Context inContext, ListView inListView){
		tagMap = new HashMap<Long ,String>();
		arrayList = new ArrayList<String>();  
		this.arrayAdapter= new ArrayAdapter<String>(inContext, android.R.layout.simple_list_item_1, arrayList);
		this.listView=inListView;
		inListView.setAdapter(arrayAdapter);

		
		inListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View inView, int inPosition, long inId) {
				Log.v("MyActivity","CLICKED, pos:"+inPosition);
				Log.v("MyActivity","CLICKED, inId:"+inPosition);
				Log.v("MyActivity","CLICKED, str:"+ListViewHelper.this.arrayList.get(inPosition));
				Log.v("MyActivity","CLICKED, tagString:"+ListViewHelper.this.getTagString(inId));
				ListViewHelper.this.currentIndex=inPosition;
				ListViewHelper.this.onClick(ListViewHelper.this.arrayList.get(inPosition),inPosition,inId,ListViewHelper.this.getTagString(inId));
			}
		}); 
		
		
		
		
		
		
		
	}
	
	
	
	public void lateSetup(Context inContext, ListView inListView){
		tagMap = new HashMap<Long ,String>();
		arrayList = new ArrayList<String>();  
		this.arrayAdapter= new ArrayAdapter<String>(inContext, android.R.layout.simple_list_item_1, arrayList);
		this.listView=inListView;
		inListView.setAdapter(arrayAdapter);

		
		inListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View inView, int inPosition, long inId) {
				Log.v("MyActivity","CLICKED, pos:"+inPosition);
				Log.v("MyActivity","CLICKED, inId:"+inPosition);
				Log.v("MyActivity","CLICKED, str:"+ListViewHelper.this.arrayList.get(inPosition));
				Log.v("MyActivity","CLICKED, tagString:"+ListViewHelper.this.getTagString(inId));
				ListViewHelper.this.currentIndex=inPosition;
				ListViewHelper.this.onClick(ListViewHelper.this.arrayList.get(inPosition),inPosition,inId,ListViewHelper.this.getTagString(inId));
			}
		}); 
		
		
	}
	public void onClick(String inItemString,int inPosition, long inId, String inTagString){
		// TODO override this to get clicked data----
		
	}
	
	public void addItem(String inItemString){
		arrayAdapter.add(inItemString);	
	}
	public void addItem(String inItemString, String inTagString){
		arrayAdapter.add(inItemString);
		int lastInsertPosition = arrayAdapter.getPosition (inItemString);
		long idOfLastInsert = arrayAdapter.getItemId(lastInsertPosition);
		this.tagMap.put(idOfLastInsert, inTagString);
		Log.v("MyActivity","ListViewHelper AddItem str/id:"+inItemString+"  "+String.valueOf(idOfLastInsert));
	}
	public String getTagString(long inItemId){
		return this.tagMap.get(inItemId);
	}
	
	
	
	public void show(){
		Log.v("MyActivity","ListViewHelper Show enetered");
		this.arrayAdapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, stringArray);
		
		this.listView.setAdapter(arrayAdapter);
		
	}
	
	public void clear(){
		this.arrayAdapter.clear();
		tagMap.clear();		
	}
	
	public String getSelected(){
		return this.arrayList.get(this.currentIndex);
	}
	 

}
