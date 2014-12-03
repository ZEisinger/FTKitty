package Events;

import java.util.ArrayList;
import java.util.List;

import umd.cmsc.feedthekitty.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<EventItem>{
	private static String TAG = "EventAdapter";
	private List<EventItem> eventList = new ArrayList<EventItem>();
	
	static class EventViewHolder{
		TextView eventName;
		TextView eventLoc;
		TextView eventDesc;
		ImageView eventIcon;
		TextView eventDate;
	}
	
	public EventAdapter(Context context, int textViewResourceId){
		super(context, textViewResourceId);
	}
	
	@Override
	public void add(EventItem object){
		eventList.add(object);
		notifyDataSetChanged();
	}
	
	@Override
	public void remove(EventItem object){
		eventList.remove(object);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount(){
		return eventList.size();
	}
	
	@Override
	public EventItem getItem(int index){
		return eventList.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		EventViewHolder viewHolder;
		
		if(row == null){
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.event_item, parent, false);
			
			viewHolder = new EventViewHolder();
			viewHolder.eventName = (TextView) row.findViewById(R.id.event_name);
			viewHolder.eventLoc = (TextView) row.findViewById(R.id.event_location);
			viewHolder.eventDesc = (TextView) row.findViewById(R.id.event_description);
			viewHolder.eventIcon = (ImageView) row.findViewById(R.id.event_icon);
			viewHolder.eventDate = (TextView) row.findViewById(R.id.event_date);
			row.setTag(viewHolder);
		}else{
			viewHolder = (EventViewHolder) row.getTag();
		}
		
		EventItem event = getItem(position);
		
		viewHolder.eventName.setText(event.getEventName());
		viewHolder.eventLoc.setText(event.getEventLoc());
		viewHolder.eventDesc.setText(event.getEventDesc());
		viewHolder.eventDate.setText(event.getEventDate());
		
		if(event.getImageName() != null && !event.getImageName().isEmpty() && !event.getImageName().equals("null")){
			Log.d("EVENT_NAME_ADAPTER", "NAME: " + event.getEventName());
			Utils.Utils.loadImage(viewHolder.eventIcon, "http://cmsc436.striveforthehighest.com/storage/pictures/" + event.getImageName());
		}else{
			viewHolder.eventIcon.setImageDrawable(null);
		}
		
		return row;
	}
}
