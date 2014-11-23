package history.Fragments;

import history.Events.EventAdapter;
import history.Events.EventItem;
import umd.cmsc.feedthekitty.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventListFragment extends Fragment{
	
	private static String TAG = "EventListFragment";
	
	private ListView eventList;
	private EventAdapter eventAdapter;
	

//	// Set up some information about the mQuoteView TextView 
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		eventList = (ListView) getActivity().findViewById(R.id.history_event_list_view);
//		
//		eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.history_event_item);
//				
//		EventItem eventItem = new EventItem("Event Name", "Location", "Details", "Date", "Time", "Hashtag Details", "Hashtag");
//		
//		eventAdapter.add(eventItem);
//		
//		eventList.setAdapter(eventAdapter);
//	}
	
	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		View view=inflater.inflate(R.layout.history_fragment_main,container, false);
		
		eventList = (ListView) view.findViewById(R.id.history_event_list_view);
		
		eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.history_event_item);
				
		EventItem eventItem = new EventItem("Event Name", "Location", "Details", "Date", "Time", "Hashtag Details", "Hashtag");
		
		eventAdapter.add(eventItem);
		
		eventList.setAdapter(eventAdapter);

		return view;
	}

}
