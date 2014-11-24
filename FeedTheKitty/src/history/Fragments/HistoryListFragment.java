package history.Fragments;

import umd.cmsc.feedthekitty.R;
import history.Events.EventAdapter;
import history.Events.EventDetailFragment;
import history.Events.EventItem;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryListFragment extends Fragment{

	private static String TAG = "EventListFragment";

	private ListView eventList;
	public static EventAdapter eventAdapter;

	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle(getString(R.string.title_history));

		eventList = (ListView) getActivity().findViewById(R.id.history_event_list_view);

		eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.history_event_item);

		EventItem eventItem = new EventItem("Event Name", "Location", "Details", "Date", "Time", "Hashtag Details", "Hashtag");

		eventAdapter.add(eventItem);

		eventList.setAdapter(eventAdapter);

		eventList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				EventDetailFragment detail = new EventDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString("EventName", eventAdapter.getItem(position).getEventName());
				bundle.putString("EventDate", eventAdapter.getItem(position).getEventDate());
				bundle.putString("EventTime", eventAdapter.getItem(position).getEventTime());
				bundle.putString("EventLocation", eventAdapter.getItem(position).getEventLocation());
				bundle.putString("EventDescription", eventAdapter.getItem(position).getEventDescription());
				detail.setArguments(bundle);

				getFragmentManager().beginTransaction()
				.replace(R.id.container, detail).addToBackStack("event_history_detail").commit();			
			}

		});
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.history_fragment_main, container, false);
	}
}
