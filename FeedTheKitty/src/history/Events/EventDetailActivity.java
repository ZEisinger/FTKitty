package history.Events;

import java.util.Date;

import umd.cmsc.feedthekitty.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.history_event_detail);

		// TODO - implement the Activity
		Intent intent = getIntent();

		getActionBar().setTitle(intent.getStringExtra("EventName"));
		ImageView icon = (ImageView) findViewById(R.id.history_detail_icon);
		TextView eventDate = (TextView) findViewById(R.id.history_detail_date);
		TextView eventTime = (TextView) findViewById(R.id.history_detail_time);
		TextView eventLocation = (TextView) findViewById(R.id.history_detail_location);
		TextView eventDescription = (TextView) findViewById(R.id.history_detail_description);
		TextView eventHashtagDetails = (TextView) findViewById(R.id.history_detail_hashtag_description);
		TextView eventHashtag = (TextView) findViewById(R.id.history_detail_hashtag);
		
		eventDate.setText(intent.getStringExtra("EventDate"));
		eventTime.setText(intent.getStringExtra("EventTime"));
		eventLocation
				.setText(intent.getStringExtra("EventLocation"));
		eventDescription.setText(intent.getStringExtra("EventDescription"));
		eventHashtagDetails.setText(intent.getStringExtra("EventHashtagDescription"));
		eventHashtag.setText(intent.getStringExtra("EventHashtag"));
	}
}
