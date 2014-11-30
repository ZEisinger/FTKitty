package EventFragments;

import java.io.File;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Events.EventAdapter;
import Events.EventItem;
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
import android.widget.TextView;

public class EventListFragment extends Fragment{

	private static String TAG = "EventListFragment";

	private ListView eventList;
	public static EventAdapter eventAdapter;
	private int lastFirstVisibleItem = 0;
	private int preLast;
	
	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Public Events");

		eventList = (ListView) getActivity().findViewById(R.id.event_list_view);

		eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.event_item);

		for(int i = 0; i < 10; i++){
			EventItem eventItem = new EventItem("EVENT", "This is a description.", "#feedthekitty");
			eventAdapter.add(eventItem);
		}

		eventList.setAdapter(eventAdapter);

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
							for(int i = 0; i < 10; i++){
								EventItem eventItem = new EventItem("EVENT2", "This is a description.2", "#feedthekitty");
								eventAdapter.add(eventItem);
							}
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

}
