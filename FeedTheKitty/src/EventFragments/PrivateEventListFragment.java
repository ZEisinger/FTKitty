package EventFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Events.EventAdapter;
import Events.EventItem;
import Venmo.Messages;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PrivateEventListFragment extends Fragment{

	private static String TAG = "PrivateEventListFragment";

	private ListView privateEventList;
	private ProgressBar privateProgressBar;
	public static EventAdapter privateEventAdapter;

	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Private Events");
		
		privateProgressBar = (ProgressBar) getActivity().findViewById(R.id.private_events_progress);
		privateEventList = (ListView) getActivity().findViewById(R.id.private_event_list_view);		
		privateEventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.event_item);

		privateEventList.setAdapter(privateEventAdapter);
		
		Log.d("FRIENDS", "FRIENDS: " + EventListFragment.currentUserFriends);
		getEvents(EventListFragment.currentUserFriends);

		privateEventList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				EventViewFragment evf = new EventViewFragment();
				Bundle bundle = new Bundle();
				bundle.putString("event_name", privateEventAdapter.getItem(position).getEventName());
				bundle.putString("event_desc", privateEventAdapter.getItem(position).getEventDesc());
				bundle.putString("event_hash_tag", privateEventAdapter.getItem(position).getEventHashTag());
				bundle.putString("username", privateEventAdapter.getItem(position).getUserName());
				evf.setArguments(bundle);
				
				getFragmentManager().beginTransaction()
				.replace(R.id.container, evf).addToBackStack("event_view").commit();			
			}

		});
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.private_event_fragment_main, container, false);
	}
	
	private void getEvents(String friendsList){
		Ion.with(getActivity())
		.load("http://cmsc436.striveforthehighest.com/api/getEventList.php")
		.progressBar(privateProgressBar)
		.setBodyParameter("friend_list", friendsList + EventListFragment.currentUserID)
	    .setBodyParameter("visibility", "private")
		.asString()
		.setCallback(new FutureCallback<String>() {

			@Override
			public void onCompleted(Exception e, String result) {
				// TODO Auto-generated method stub
				Log.d("EVENT_LIST", "EVENT: " + result);

				JSONTokener tokener = new JSONTokener(result);

				try {
					JSONObject root = new JSONObject(tokener);
					
					String temp;
					if(root.has("errors")){
						Log.d("HEY", "HEY");
						temp = Messages.safeJSON(root, "errors");
						if(temp != null && !temp.isEmpty() && !temp.equals("null")){
							Log.d("ERROR", "Error: " + Messages.safeJSON(root, "errors"));
						}else if(root.has("result")){
							JSONArray arr = root.getJSONArray("result");
							for(int i = 0; i < arr.length(); i++){
								JSONObject tObj = arr.getJSONObject(i);
								String eventName = Messages.safeJSON(tObj, "event_name");
								String eventLoc = Messages.safeJSON(tObj, "location");
								String eventDesc = Messages.safeJSON(tObj, "description");
								String eventHashTag = Messages.safeJSON(tObj, "hashtag");
								String eventDate = Messages.safeJSON(tObj, "event_date");
								String eventTime = Messages.safeJSON(tObj, "event_time");
								String imageName = Messages.safeJSON(tObj, "image_name");
								String userName = Messages.safeJSON(tObj, "username");
								
								Log.d("LIST", "NAME: " + eventName + "    " + "IMAGE: " + imageName);
								boolean isEnd = Utils.Utils.isEnd(eventDate, eventTime);
								if(isEnd){
									Log.d("EVENT ENDED", "END: " + isEnd);
									// Do nothing with event, it is over and should not be displayed
									
								}else{
									EventItem event = new EventItem(eventName, eventLoc, eventDesc, eventHashTag, eventDate, imageName,
											userName);
									privateEventAdapter.add(event);
								}

							}
						}
					}
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

	}

}
