package hopper.library.object;

import java.util.ArrayList;

public class ProfileObject {
	private ArrayList<ProfileGroupObject> groupsArrayList = new ArrayList<ProfileGroupObject>();
	
	
	public ProfileObject() {		
	}	
	
	public void addGroup(ProfileGroupObject inGroup){
		groupsArrayList.add(inGroup);
	}
	public void addGroups(ArrayList<ProfileGroupObject> inGroupArrayList){
		groupsArrayList.addAll(inGroupArrayList);
	}
	public int getGroupCount(){
		return groupsArrayList.size();
	}
	public ProfileGroupObject getGroup(int inIndex){
		return groupsArrayList.get(inIndex);
	}

	public ArrayList<ProfileGroupObject> getgroups(){
		return groupsArrayList;
	}	
	

}
