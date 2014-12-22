package hopper.library.object;

import java.util.ArrayList;

public class ProfileGroupObject {
	
	private int id;
	private int userId;
	private String subjectCaption;
	private String subjectFilePath;
	private String groupCaption;
	private String groupFilePath;
	private String topicImageFilePath;
	private int topicImageId = 0;
	public int getTopicImageId() {
		return topicImageId;
	}
	


	private int sortOrder;
	private ArrayList<ProfileItemObject> itemsArrayList = new ArrayList<ProfileItemObject>();
	

	public ProfileGroupObject() {
		// TODO Auto-generated constructor stub
	}
	public void setSortOrder(int inVal){
		sortOrder = inVal;
	}
	public int getSortOrder(){
		return sortOrder;
	}
	
	public void addItem(ProfileItemObject inItem){
		itemsArrayList.add(inItem);
	}
	public void addItems(ArrayList<ProfileItemObject> inItemArrayList){
		itemsArrayList.addAll(inItemArrayList);
	}
	public int getItemsCount(){
		return itemsArrayList.size();
	}
	public ProfileItemObject getItem(int inIndex){
		return itemsArrayList.get(inIndex);
	}

	public ArrayList<ProfileItemObject> getItems(){
		return itemsArrayList;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getSubjectCaption() {
		return subjectCaption;
	}


	public void setSubjectCaption(String subjectCaption) {
		this.subjectCaption = subjectCaption;
	}


	public String getSubjectFilePath() {
		return subjectFilePath;
	}


	public void setSubjectFilePath(String subjectFilePath) {
		this.subjectFilePath = subjectFilePath;
	}


	public String getGroupCaption() {
		return groupCaption;
	}


	public void setGroupCaption(String groupCaption) {
		this.groupCaption = groupCaption;
	}


	public String getGroupFilePath() {
		return groupFilePath;
	}


	public void setGroupFilePath(String groupFilePath) {
		this.groupFilePath = groupFilePath;
	}


	public String getTopicImageFilePath() {
		return topicImageFilePath;
	}


	public void setTopicImageFilePath(String topicImageFilePath) {
		this.topicImageFilePath = topicImageFilePath;
		this.topicImageId = Math.abs(topicImageFilePath.hashCode())+this.getId();
	}
	

}
