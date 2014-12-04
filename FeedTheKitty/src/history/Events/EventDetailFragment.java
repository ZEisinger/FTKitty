package history.Events;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Utils.Utils;
import Venmo.Messages;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

		String eventUserName =getArguments().getString("Username");
		String eventName = getArguments().getString("EventName");
		getEvent(eventUserName, eventName);

//		eventDate.setText(getArguments().getString("EventDate"));
//		eventTime.setText(getArguments().getString("EventTime"));
//		eventLocation.setText(getArguments().getString("EventLocation"));
//		eventDescription.setText(getArguments().getString("EventDescription"));
	}
	
	private void getEvent(String username, String eventName){
		if(!username.isEmpty() && !eventName.isEmpty()){
			Ion.with(getActivity())
			.load("http://cmsc436.striveforthehighest.com/api/findEvent.php")
			.setBodyParameter("username", username)
			.setBodyParameter("event_name", eventName)
			.asString()
			.setCallback(new FutureCallback<String>() {

				@Override
				public void onCompleted(Exception e, String result) {
					// TODO Auto-generated method stub
					Log.d("EVENT", "Event: " + result);
					JSONTokener tokener = new JSONTokener(result);

					try{
						JSONObject root = new JSONObject(tokener);
						JSONObject id;
						String temp;
						if(root.has("errors")){
							Log.d("HEY", "HEY");
							temp = Messages.safeJSON(root, "errors");
							if(temp != null && !temp.isEmpty() && !temp.equals("null")){
								Log.d("ERROR", "Error: " + Messages.safeJSON(root, "errors"));
							}
						}
						if(root.has("result") && root.getJSONObject("result").has("username")){
							id = root.getJSONObject("result");
							// Need place in UI
						}
						if(root.has("result") && root.getJSONObject("result").has("description")){
							id = root.getJSONObject("result");
							temp = Messages.safeJSON(id, "description");
							if(temp != null && !temp.isEmpty()){
								eventDescription.setText(temp);
							}
						}
						if(root.has("result") && root.getJSONObject("result").has("location")){
							id = root.getJSONObject("result");
							temp = Messages.safeJSON(id, "location");
							if(temp != null && !temp.isEmpty()){
								eventLocation.setText(temp);
							}
						}
						if(root.has("result") && root.getJSONObject("result").has("event_date")){
							id = root.getJSONObject("result");
							temp = Messages.safeJSON(id, "event_date");
							if(temp != null && !temp.isEmpty()){
								eventDate.setText(temp);
							}
						}
						if(root.has("result") && root.getJSONObject("result").has("event_time")){
							id = root.getJSONObject("result");
							temp = Messages.safeJSON(id, "event_time");
							if(temp != null && !temp.isEmpty()){
								eventTime.setText(temp);
							}
						}
						if(root.has("result") && root.getJSONObject("result").has("image_name")){
							id = root.getJSONObject("result");
							temp = Messages.safeJSON(id, "image_name");
							if(temp != null && !temp.isEmpty()){
								Utils.loadImage(icon, "http://cmsc436.striveforthehighest.com/storage/pictures/" + temp);
							}
						}
					}catch (JSONException w){
						w.printStackTrace();
					}
				}

			});

		}

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
