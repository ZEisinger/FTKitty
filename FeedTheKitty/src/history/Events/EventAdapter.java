package history.Events;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import umd.cmsc.feedthekitty.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EventAdapter extends ArrayAdapter<EventItem>{
	private static String TAG = "EventAdapter";
	private List<EventItem> eventList = new ArrayList<EventItem>();
	
	static class EventViewHolder{
		TextView eventName;
		TextView eventLocation;
		TextView eventDescription;
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
			row = inflater.inflate(R.layout.history_event_item, parent, false);
			
			viewHolder = new EventViewHolder();
			viewHolder.eventName = (TextView) row.findViewById(R.id.history_event_name);
			viewHolder.eventLocation = (TextView) row.findViewById(R.id.history_location);
			viewHolder.eventDescription = (TextView) row.findViewById(R.id.history_description);
			viewHolder.eventDate = (TextView) row.findViewById(R.id.history_date);
			row.setTag(viewHolder);
		}else{
			viewHolder = (EventViewHolder) row.getTag();
		}
		
		final EventItem event = getItem(position);
		
		viewHolder.eventName.setText(event.getEventName());
		viewHolder.eventLocation.setText(event.getEventLocation());
		viewHolder.eventDescription.setText(event.getEventDescription());
		viewHolder.eventDate.setText(event.getEventDate());
		
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Context context = EventAdapter.this.getContext();
				Intent intent = new Intent(context,
						EventDetailActivity.class);
				Intent eventIntent = event.packageToIntent();
				intent.putExtras(eventIntent);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return;
			}
		});
		
		return row;
	}
}
