package history.Events;

import android.content.Intent;

public class EventItem {

	private String eventName;
	private String eventLocation;
	private String eventDescription;
	private String eventDate;
	private String eventTime;
	private String imageName;
	private String eventHashtag;
	private String hostName;
	
	public EventItem(String eventName, String eventLocation, String eventDescription, String eventDate, String eventTime, String eventHashtag, String imageName, String userName){
		this.eventName = eventName;
		this.eventLocation = eventLocation;
		this.eventDescription = eventDescription;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.imageName = imageName;
		this.eventHashtag = eventHashtag;
		this.hostName = userName;
	}
	
	public EventItem(Intent intent){
		this.eventName = intent.getStringExtra("EventName");
		this.eventLocation = intent.getStringExtra("EventLocation");
		this.eventDescription = intent.getStringExtra("EventDescription");
		this.eventDate = intent.getStringExtra("EventDate");
		this.eventTime = intent.getStringExtra("EventTime");
		this.imageName = intent.getStringExtra("ImageName");
		this.eventHashtag = intent.getStringExtra("EventHashtag");
		this.hostName = intent.getStringExtra("Username");
	}
	
	public String getEventName(){
		return this.eventName;
	}
	
	public String getEventLocation(){
		return this.eventLocation;
	}
	
	public String getEventDescription(){
		return this.eventDescription;
	}
	
	public String getEventDate(){
		return this.eventDate;
	}
	
	public String getEventTime(){
		return this.eventTime;
	}
	
	public String getImageName(){
		return this.imageName;
	}
	
	public String getEventHashtag(){
		return this.eventHashtag;
	}
	
	public String getUserName(){
		return this.hostName;
	}
	
	public Intent packageToIntent(){
		Intent intent = new Intent();
		intent.putExtra("EventName", eventName);
		intent.putExtra("EventDate", eventDate);
		intent.putExtra("EventTime", eventTime);
		intent.putExtra("EventLocation", eventLocation);
		intent.putExtra("EventDescription", eventDescription);
		intent.putExtra("ImageName", imageName);
		intent.putExtra("EventHashtag", eventHashtag);
		intent.putExtra("Username", hostName);
		return intent;
	}
	
	@Override
	public String toString() {
		return "Name: " + eventName + " Details: " + eventDescription;

	}
}
