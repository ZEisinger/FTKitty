package history.Fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import umd.cmsc.feedthekitty.R;
import history.Events.EventAdapter;
import history.Events.EventDetailFragment;
import history.Events.EventItem;
import EventFragments.EventListFragment;
import EventFragments.EventViewFragment;
import Utils.Utils;
import Venmo.Messages;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.android.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class HistoryListFragment extends Fragment {

	private static String TAG = "EventListFragment";

	private TextView amount;
	private TextView events;
	private ListView eventList;
	public static EventAdapter eventAdapter;

	// Set up some information about the mQuoteView TextView
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle(getString(R.string.title_history));

		eventList = (ListView) getActivity().findViewById(
				R.id.history_event_list_view);

		eventAdapter = new EventAdapter(getActivity().getApplicationContext(),
				R.layout.history_event_item);

		eventList.setAdapter(eventAdapter);
		getEvents();
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Bundle bundle = new Bundle();
				bundle.putString("EventDate", eventAdapter.getItem(position)
						.getEventDate());
				bundle.putString("EventTime", eventAdapter.getItem(position)
						.getEventTime());
				bundle.putString("EventLocation", eventAdapter
						.getItem(position).getEventLocation());

				if (Utils.isEnd(eventAdapter.getItem(position).getEventDate(),
						eventAdapter.getItem(position).getEventTime())) {
					bundle.putString("EventDescription",
							eventAdapter.getItem(position).getEventDescription());
					bundle.putString("EventName", eventAdapter.getItem(position)
							.getEventName());
					bundle.putString("Username", eventAdapter.getItem(position)
							.getUserName());
					bundle.putString("Payment", eventAdapter.getItem(position)
							.getPayment());
					EventDetailFragment detail = new EventDetailFragment();
					detail.setArguments(bundle);

					getFragmentManager().beginTransaction()
							.replace(R.id.container, detail)
							.addToBackStack("event_history_detail").commit();
				} else {
					bundle.putString("event_desc",
							eventAdapter.getItem(position).getEventDescription());
					bundle.putString("event_name", eventAdapter.getItem(position)
							.getEventName());
					bundle.putString("username", eventAdapter.getItem(position)
							.getUserName());
					EventViewFragment detail = new EventViewFragment();
					detail.setArguments(bundle);
					getFragmentManager().beginTransaction()
							.replace(R.id.container, detail)
							.addToBackStack("event_view").commit();
				}
			}

		});
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need
		// to be attached to the container ViewGroup
		return inflater.inflate(R.layout.history_fragment_main, container,
				false);
	}

	private void getEvents() {
		Ion.with(getActivity())
				.load("http://cmsc436.striveforthehighest.com/api/findHistory.php")
				.setBodyParameter("username", "10152385345176566").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception e, String result) {
						// TODO Auto-generated method stub
						Log.d("History_LIST", "HISTORY: " + result);

						JSONTokener tokener = new JSONTokener(result);

						try {
							JSONObject root = new JSONObject(tokener);

							String temp;
							if (root.has("errors")) {
								temp = Messages.safeJSON(root, "errors");
								if (temp != null && !temp.isEmpty()
										&& !temp.equals("null")) {
									Log.d("ERROR",
											"Error: "
													+ Messages.safeJSON(root,
															"errors"));
								} else if (root.has("result")) {
									JSONArray arr = root.getJSONArray("result");
									for (int i = 0; i < arr.length(); i++) {
										JSONObject tObj = arr.getJSONObject(i);
										String eventName = Messages.safeJSON(
												tObj, "event_name");
										String eventLoc = Messages.safeJSON(
												tObj, "location");
										String eventDesc = Messages.safeJSON(
												tObj, "description");
										String eventHashTag = Messages
												.safeJSON(tObj, "hashtag");
										String eventDate = Messages.safeJSON(
												tObj, "event_date");
										String eventTime = Messages.safeJSON(
												tObj, "event_time");
										String imageName = Messages.safeJSON(
												tObj, "image_name");
										String userName = Messages.safeJSON(
												tObj, "username");
										String payment = Messages.safeJSON(
												tObj, "payment_amount");

										Log.d("LIST", "NAME: " + eventName
												+ "    " + "IMAGE: "
												+ imageName);
										EventItem event = new EventItem(
												eventName, eventLoc, eventDesc,
												eventDate, eventTime,
												eventHashTag, imageName,
												userName, payment);
										eventAdapter.add(event);
									}
								}
								if (root.has("sum")) {
									String sum = Messages.safeJSON(root, "sum");
									amount = (TextView) getActivity()
											.findViewById(
													R.id.history_list_total_amount);
									amount.setText(sum);
								}
								if (root.has("eventCount")) {
									String count = Messages.safeJSON(root,
											"eventCount");
									events = (TextView) getActivity()
											.findViewById(
													R.id.history_list_total_event);
									events.setText(count);
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