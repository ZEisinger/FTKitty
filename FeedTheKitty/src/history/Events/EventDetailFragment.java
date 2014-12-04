package history.Events;

import umd.cmsc.feedthekitty.R;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailFragment extends Fragment {

	private ImageView icon;
	private TextView eventDate;
	private TextView eventTime;
	private TextView eventLocation;
	private TextView eventDescription;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - implement the Activity
		getActivity().getActionBar().setTitle(
				Html.fromHtml("<font color='#FE8909'>"
						+ getArguments().getString("EventName") + "</font>"));
		icon = (ImageView) getActivity().findViewById(R.id.history_detail_icon);
		eventDate = (TextView) getActivity().findViewById(
				R.id.history_detail_date);
		eventTime = (TextView) getActivity().findViewById(
				R.id.history_detail_time);
		eventLocation = (TextView) getActivity().findViewById(
				R.id.history_detail_location);
		eventDescription = (TextView) getActivity().findViewById(
				R.id.history_detail_description);

		eventDate.setText(getArguments().getString("EventDate"));
		eventTime.setText(getArguments().getString("EventTime"));
		eventLocation.setText(getArguments().getString("EventLocation"));
		eventDescription.setText(getArguments().getString("EventDescription"));
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need
		// to be attached to the container ViewGroup
		return inflater
				.inflate(R.layout.history_event_detail, container, false);
	}
}
