package history.Events;

import android.content.Intent;

public class EventItem {

	private String eventName;
	private String eventLocation;
	private String eventDescription;
	private String eventDate;
	private String eventTime;
	private String eventHashtagDescription;
	private String eventHashtag;
	
	public EventItem(String eventName, String eventLocation, String eventDescription, String eventDate, String eventTime, String eventHashtagDescription, String eventHashtag){
		this.eventName = eventName;
		this.eventLocation = eventLocation;
		this.eventDescription = eventDescription;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.eventHashtagDescription = eventHashtagDescription;
		this.eventHashtag = eventHashtag;
	}
	
	public EventItem(Intent intent){
		this.eventName = intent.getStringExtra("EventName");
		this.eventLocation = intent.getStringExtra("EventLocation");
		this.eventDescription = intent.getStringExtra("EventDescription");
		this.eventDate = intent.getStringExtra("EventDate");
		this.eventTime = intent.getStringExtra("EventTime");
		this.eventHashtagDescription = intent.getStringExtra("EventHashtagDescription");
		this.eventHashtag = intent.getStringExtra("EventHashtag");
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
	
	public String getEventHashtagDescription(){
		return this.eventHashtagDescription;
	}
	
	public String getEventHashtag(){
		return this.eventHashtag;
	}
	
	public Intent packageToIntent(){
		Intent intent = new Intent();
		intent.putExtra("EventName", eventName);
		intent.putExtra("EventDate", eventDate);
		intent.putExtra("EventTime", eventTime);
		intent.putExtra("EventLocation", eventLocation);
		intent.putExtra("EventDescription", eventDescription);
		intent.putExtra("EventHashtagDescription", eventHashtagDescription);
		intent.putExtra("EventHashtag", eventHashtag);
		return intent;
	}
	
	@Override
	public String toString() {
		return "Name: " + eventName + " Details: " + eventDescription;

	}
}
