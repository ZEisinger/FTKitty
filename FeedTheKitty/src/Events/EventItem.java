package Events;

public class EventItem {

	private String eventName;
	private String eventLoc;
	private String eventDesc;
	private String eventHashTag;
	private String eventDate;
	private String imageName;
	private String userName;
	
	public EventItem(String eventName, String eventLoc, String eventDesc, String eventHashTag, String eventDate,
			String imageName, String userName){
		this.eventName = eventName;
		this.eventLoc = eventLoc;
		this.eventDesc = eventDesc;
		this.eventHashTag = eventHashTag;
		this.eventDate = eventDate;
		this.imageName = imageName;
		this.userName = userName;
	}
	
	public String getEventName(){
		return this.eventName;
	}
	
	public String getEventLoc(){
		return this.eventLoc;
	}
	
	public String getEventDesc(){
		return this.eventDesc;
	}
	
	public String getEventHashTag(){
		return this.eventHashTag;
	}
	
	public String getEventDate(){
		return this.eventDate;
	}
	
	public String getImageName(){
		return this.imageName;
	}
	
	public String getUserName(){
		return this.userName;
	}
}
