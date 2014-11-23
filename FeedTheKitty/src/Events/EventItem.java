package Events;

public class EventItem {

	private String eventName;
	private String eventDesc;
	private String eventHashTag;
	
	public EventItem(String eventName, String eventDesc, String eventHashTag){
		this.eventName = eventName;
		this.eventDesc = eventDesc;
		this.eventHashTag = eventHashTag;
	}
	
	public String getEventName(){
		return this.eventName;
	}
	
	public String getEventDesc(){
		return this.eventDesc;
	}
	
	public String getEventHashTag(){
		return this.eventHashTag;
	}
	
}
