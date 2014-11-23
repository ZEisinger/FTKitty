package EventFragments;

import umd.cmsc.feedthekitty.R;
import Events.EventAdapter;
import Events.EventItem;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PrivateEventListFragment extends Fragment{

	private static String TAG = "PrivateEventListFragment";

	private ListView privateEventList;
	public static EventAdapter privateEventAdapter;

	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Private Events");
		
		privateEventList = (ListView) getActivity().findViewById(R.id.private_event_list_view);
		
		privateEventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.event_item);

		for(int i = 0; i < 3; i++){
			EventItem eventItem = new EventItem("PRIVATE EVENT" + i, "This is a description.", "");
			privateEventAdapter.add(eventItem);
		}

		privateEventList.setAdapter(privateEventAdapter);

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

}
