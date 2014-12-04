package EventFragments;

import java.io.File;

import main.FriendsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Events.EventAdapter;
import Events.EventItem;
import Utils.CoreCallbackString;
import Venmo.Messages;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EventListFragment extends Fragment{

	private static String TAG = "EventListFragment";

	private ListView eventList;
	private ProgressBar progressBar;
	public static EventAdapter eventAdapter;
	private int lastFirstVisibleItem = 0;
	private int preLast;
	public static String currentUserID;
	public static String currentUserFriends;
	public static String currentUserFullName;
	
	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Public Events");
		
		// Has two callbacks since the UserID and friends list could be received at different times
		FriendsList friendsList = new FriendsList(getActivity(), new CoreCallbackString() {

			@Override
			// Deals with the UserID
			public void run(String result, String resultFullName) {
				// TODO Auto-generated method stub
				Log.d("ME", "ME: " + result);
				currentUserID = result;
				currentUserFullName = resultFullName;
			}
			
		}, new CoreCallbackString(){

			@Override
			// Deals with the Friends list
			public void run(String result, String resultFullName) {
				// TODO Auto-generated method stub
				currentUserFriends = result;
			}
			
		});
		
		progressBar = (ProgressBar) getActivity().findViewById(R.id.events_progress);
		eventList = (ListView) getActivity().findViewById(R.id.event_list_view);
		eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.event_item);

		eventList.setAdapter(eventAdapter);
		getEvents();
		eventList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				EventViewFragment evf = new EventViewFragment();
				Bundle bundle = new Bundle();
				bundle.putString("event_name", eventAdapter.getItem(position).getEventName());
				bundle.putString("event_desc", eventAdapter.getItem(position).getEventDesc());
				bundle.putString("event_hash_tag", eventAdapter.getItem(position).getEventHashTag());
				bundle.putString("username", eventAdapter.getItem(position).getUserName());
				evf.setArguments(bundle);

				getFragmentManager().beginTransaction()
				.replace(R.id.container, evf).addToBackStack("event_view").commit();			
			}

		});

		eventList.setOnScrollListener( new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub			
				if(view.getId() == eventList.getId()){
					final int lastItem = firstVisibleItem + visibleItemCount;
					if(firstVisibleItem > lastFirstVisibleItem){
						Log.d("EVENT", "SCORLLING DOWN");
					}else if(firstVisibleItem < lastFirstVisibleItem){
						Log.d("EVENT", "SCORLLING UP");
					}else if(lastItem == totalItemCount){
						if(preLast != lastItem){
							Log.d("EVENT", "LAST ITEM REACHED");
							preLast = lastItem;
//							for(int i = 0; i < 10; i++){
//								EventItem eventItem = new EventItem("EVENT2", "This is a description.2", "#feedthekitty");
//								eventAdapter.add(eventItem);
//							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;

			}

		});
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.event_fragment_main, container, false);
	}

	private void getEvents(){
		Ion.with(getActivity())
		.load("http://cmsc436.striveforthehighest.com/api/getEventList.php")
		.progressBar(progressBar)
		.setBodyParameter("visibility", "public")
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
								String imageName = Messages.safeJSON(tObj, "image_name");
								String userName = Messages.safeJSON(tObj, "username");
								
								Log.d("LIST", "NAME: " + eventName + "    " + "IMAGE: " + imageName);
								boolean isEnd = Utils.Utils.isEnd(eventDate, "");
								if(isEnd){
									Log.d("EVENT ENDED", "END: " + isEnd);
									// Do nothing with event, it is over and should not be displayed
									
								}else{
									EventItem event = new EventItem(eventName, eventLoc, eventDesc, eventHashTag, eventDate, imageName,
											userName);
									eventAdapter.add(event);
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
