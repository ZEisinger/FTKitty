package Events;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import umd.cmsc.feedthekitty.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<Status>{
	private static String TAG = "EventAdapter";
	private List<Status> twitterList = new ArrayList<Status>();
	
	static class EventViewHolder{
		TextView userName;
		TextView tweet;
	}
	
	public TwitterAdapter(Context context, int textViewResourceId){
		super(context, textViewResourceId);
	}
	
	@Override
	public void add(Status object){
		twitterList.add(object);
		notifyDataSetChanged();
	}
	
	@Override
	public void remove(Status object){
		twitterList.remove(object);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount(){
		return twitterList.size();
	}
	
	@Override
	public Status getItem(int index){
		return twitterList.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		EventViewHolder viewHolder;
		
		if(row == null){
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.twitter_item, parent, false);
			
			viewHolder = new EventViewHolder();
			viewHolder.userName = (TextView) row.findViewById(R.id.twitter_user);
			viewHolder.tweet = (TextView) row.findViewById(R.id.twitter_tweet);
			row.setTag(viewHolder);
		}else{
			viewHolder = (EventViewHolder) row.getTag();
		}
		
		Status event = getItem(position);
		
		viewHolder.userName.setText("@"+event.getUser().getScreenName());
		viewHolder.tweet.setText(event.getText());
		
		return row;
	}
}
