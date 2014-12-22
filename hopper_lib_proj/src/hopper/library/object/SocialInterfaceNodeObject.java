package hopper.library.object;

import com.example.hopperCommServicelibrary.CommService;

public class SocialInterfaceNodeObject {
	//same === nodeId & id
	private int id;
	private int socialQueueId;
	private int nodeId;
	
	private int userId;
	
	private String text; //=== (timeStamp) userName
	
	private String screenName;
	private int screenImageId;
	private String screenImageUrl;
	
	private String timeStamp;
	private String audioUrl;
	private int audioId;
	

	private String responseType;
	private int groupId;
	private String action;
	private int parentNodeId;
	private int isRoot;
	
	private int fromDeviceId;
	
	private int imageHashId;
	
	private SocialInterfaceNodeObject parentNodeObject;
	
	
	
	
	public SocialInterfaceNodeObject getParentNodeObject() {
		return parentNodeObject;
	}


	public void setParentNodeObject(SocialInterfaceNodeObject inParentNodeObject) {
		parentNodeObject = inParentNodeObject;
	}


	public int getFromDeviceId() {
		return fromDeviceId;
	}


	public void setFromDeviceId(int inFromDeviceId) {
		fromDeviceId = inFromDeviceId;
	}


	public SocialInterfaceNodeObject(){
		// TODO rember to remove from listeners-----
		//CommService.get("COMMSERVICE").		
	}


	public int getImageHashId(){
		return Math.abs(this.getScreenImageUrl().hashCode())+this.getId();
	}


	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public int getSocialQueueId() {
		return socialQueueId;
	}





	public void setSocialQueueId(int socialQueueId) {
		this.socialQueueId = socialQueueId;
	}





	public int getNodeId() {
		return nodeId;
	}





	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}





	public int getUserId() {
		return userId;
	}





	public void setUserId(int userId) {
		this.userId = userId;
	}





	public String getText() {
		return text;
	}





	public void setText(String text) {
		this.text = text;
	}





	public String getScreenName() {
		return screenName;
	}





	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}





	public String getTimeStamp() {
		return timeStamp;
	}





	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}





	public String getAudioUrl() {
		return audioUrl;
	}





	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}





	public int getAudioId() {
		return audioId;
	}





	public void setAudioId(int audioId) {
		this.audioId = audioId;
	}





	public int getScreenImageId() {
		return screenImageId;
	}





	public void setScreenImageId(int screenImageId) {
		this.screenImageId = screenImageId;
	}





	public String getScreenImageUrl() {
		return screenImageUrl;
	}





	public void setScreenImageUrl(String screenImageUrl) {
		this.screenImageUrl = screenImageUrl;
	}





	public String getResponseType() {
		return responseType;
	}





	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}





	public int getGroupId() {
		return groupId;
	}





	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}





	public String getAction() {
		return action;
	}





	public void setAction(String action) {
		this.action = action;
	}





	public int getParentNodeId() {
		return parentNodeId;
	}





	public void setParentNodeId(int parentNodeId) {
		this.parentNodeId = parentNodeId;
	}





	public int getIsRoot() {
		return isRoot;
	}





	public void setIsRoot(int isRoot) {
		this.isRoot = isRoot;
	}

	
	
}
