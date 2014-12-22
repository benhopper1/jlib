package hopper.library.object;

import android.R.bool;
import android.content.Context;

public class ConversationObject {
	
	private Context context;
	
	private int id;
	private bool hadRead;
	private String readTime;
	private bool hadDownload;
	private String downloadTime;
	private int dbId;
	private int tagId;
	private int arfId;
	private int arfBuffId;
	private int buffId;
	private int entryTypeId;
	private String caption;
	private int userId;
	private String entryTypeLabel;
	private String audioFilePath;
	private String entryTimeStamp;
	private String arfFileCaption;
	private String screenName;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String status;
	private String city;
	private String state;
	private String country;
	private String imageFilePath;
	private String stat;
	private String regEntryDate;
	private int imageId;
	private int privacyTypeId;
	private String mailTimeStamp;
	private int parentId; 							//if rsp then thier rq
	
	
	public int getParentId() {
		return parentId;
	}




	public void setParentId(int parentId) {
		this.parentId = parentId;
	}




	public ConversationObject(Context inContext){
		context = inContext;
	}
	
	
	
	
	//##################################################################
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public bool getHadRead() {
		return hadRead;
	}


	public void setHadRead(bool hadRead) {
		this.hadRead = hadRead;
	}


	public String getReadTime() {
		return readTime;
	}


	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}


	public bool getHadDownload() {
		return hadDownload;
	}


	public void setHadDownload(bool hadDownload) {
		this.hadDownload = hadDownload;
	}


	public String getDownloadTime() {
		return downloadTime;
	}


	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}


	public int getDbId() {
		return dbId;
	}


	public void setDbId(int dbId) {
		this.dbId = dbId;
	}


	public int getTagId() {
		return tagId;
	}


	public void setTagId(int tagId) {
		this.tagId = tagId;
	}


	public int getArfId() {
		return arfId;
	}


	public void setArfId(int arfId) {
		this.arfId = arfId;
	}


	public int getArfBuffId() {
		return arfBuffId;
	}


	public void setArfBuffId(int arfBuffId) {
		this.arfBuffId = arfBuffId;
	}


	public int getBuffId() {
		return buffId;
	}


	public void setBuffId(int buffId) {
		this.buffId = buffId;
	}


	public int getEntryTypeId() {
		return entryTypeId;
	}


	public void setEntryTypeId(int entryTypeId) {
		this.entryTypeId = entryTypeId;
	}


	public String getCaption() {
		return caption;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getEntryTypeLabel() {
		return entryTypeLabel;
	}


	public void setEntryTypeLabel(String entryTypeLabel) {
		this.entryTypeLabel = entryTypeLabel;
	}


	public String getAudioFilePath() {
		return audioFilePath;
	}


	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}


	public String getEntryTimeStamp() {
		return entryTimeStamp;
	}


	public void setEntryTimeStamp(String entryTimeStamp) {
		this.entryTimeStamp = entryTimeStamp;
	}


	public String getArfFileCaption() {
		return arfFileCaption;
	}


	public void setArfFileCaption(String arfFileCaption) {
		this.arfFileCaption = arfFileCaption;
	}


	public String getScreenName() {
		return screenName;
	}


	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getImageFilePath() {
		return imageFilePath;
	}


	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}


	public String getStat() {
		return stat;
	}


	public void setStat(String stat) {
		this.stat = stat;
	}


	public String getRegEntryDate() {
		return regEntryDate;
	}


	public void setRegEntryDate(String regEntryDate) {
		this.regEntryDate = regEntryDate;
	}


	public int getImageId() {
		return imageId;
	}


	public void setImageId(int imageId) {
		this.imageId = imageId;
	}


	public int getPrivacyTypeId() {
		return privacyTypeId;
	}


	public void setPrivacyTypeId(int privacyTypeId) {
		this.privacyTypeId = privacyTypeId;
	}


	public String getMailTimeStamp() {
		return mailTimeStamp;
	}


	public void setMailTimeStamp(String mailTimeStamp) {
		this.mailTimeStamp = mailTimeStamp;
	}
	
	
	
	
	
	
	

	

}
