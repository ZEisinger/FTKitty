package EventFragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import umd.cmsc.feedthekitty.R;
import main.VenmoWebViewActivity;
import Events.EventAdapter;
import Events.TwitterAdapter;
import Utils.Utils;
import Venmo.Messages;
import Venmo.VenmoLibrary;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class EventViewFragment extends Fragment{

	private BootstrapButton btnPay;
	private BootstrapButton btnShare;
	//private TextView txtEventName;
	private TextView txtEventDesc;
	private TextView txtEventDate;
	private TextView txtEventTime;
	private TextView txtEventLoc;
	private ImageView eventIcon;
	private ListView tweetView;
	private TwitterAdapter tweetAdapter;
	private String accessToken;
	private Handler handler;

	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String eventName = getArguments().getString("event_name");
		String eventDesc = getArguments().getString("event_desc");
		final String eventHashTag = getArguments().getString("event_hash_tag");

		getActivity().getActionBar().setTitle(eventName);
		
		tweetView = (ListView) getActivity().findViewById(R.id.twitter_list_view);
		tweetAdapter = new TwitterAdapter(getActivity().getApplicationContext(), R.layout.twitter_item);
		tweetView.setAdapter(tweetAdapter);

		//txtEventName = (TextView) getActivity().findViewById(R.id.event_name);
		txtEventDesc = (TextView) getActivity().findViewById(R.id.event_detail_description);
		txtEventDate = (TextView) getActivity().findViewById(R.id.event_detail_date);
		txtEventTime = (TextView) getActivity().findViewById(R.id.event_detail_time);
		txtEventLoc = (TextView) getActivity().findViewById(R.id.event_detail_location);
		eventIcon = (ImageView) getActivity().findViewById(R.id.event_detail_icon);
		txtEventDesc.setText(eventDesc);

		getEvent("steven", "Steven's Birthday");
		
		handler = new Handler();

		btnShare = (BootstrapButton) getActivity().findViewById(R.id.btn_event_view_share);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tweetIntent = new Intent(Intent.ACTION_SEND);
				tweetIntent.putExtra(Intent.EXTRA_TEXT, eventHashTag);
				tweetIntent.setType("text/plain");

				PackageManager packManager = getActivity().getPackageManager();
				List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

				boolean resolved = false;
				for(ResolveInfo resolveInfo: resolvedInfoList){
					if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
						tweetIntent.setClassName(
								resolveInfo.activityInfo.packageName, 
								resolveInfo.activityInfo.name );
						resolved = true;
						break;
					}
				}
				if(resolved){
					startActivity(tweetIntent);
				}else{
					String url = "http://www.twitter.com/intent/tweet?text=" + Uri.encode(eventHashTag);
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			}

		});

		btnPay = (BootstrapButton) getActivity().findViewById(R.id.btn_event_view_donate);	
		btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Intent venmoIntent = VenmoLibrary.openVenmoPayment("2097", "Feed the Kitty",
							"stevenberger101@gmail.com",
							"0.01",
							"Test", "pay");
					startActivityForResult(venmoIntent, 1); //1 is the requestCode we are using for Venmo. Feel free to change this to another number.
				}
				catch (android.content.ActivityNotFoundException e) //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView
				{

					Intent venmoIntent = new Intent(getActivity(), VenmoWebViewActivity.class);
					String venmo_uri = "https://api.venmo.com/v1/oauth/authorize?client_id=2097&scope=make_payments%20access_profile&response_type=token";
					Log.d("MainActivity", venmo_uri);
					venmoIntent.putExtra("url", venmo_uri);
					venmoIntent.putExtra("email", "stevenberger101@gmail.com");
					venmoIntent.putExtra("amount", "0.01");
					try {
						venmoIntent.putExtra("note", URLEncoder.encode("Test", "US-ASCII"));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					venmoIntent.putExtra("visibility", "private");
					startActivityForResult(venmoIntent, 1);
				}
			}

		});

	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.event_fragment_view_event, container, false);
	}
	
	public void getTweets(final String eventHashTag){

		if(!eventHashTag.isEmpty()){
			Thread twitterThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ConfigurationBuilder cb = new ConfigurationBuilder();
					cb.setDebugEnabled(true)
					.setOAuthConsumerKey("AvDmj9XYlONJiu599gkxnZWRu")
					.setOAuthConsumerSecret("BxyKz1C4FjRWqXMJl7ZLQcYQJypRw3i4GQOuW0w6SrN1p2lDaZ")
					.setOAuthAccessToken("29794490-RUGulUhMZCdpHyRtB249KC0DaSgTgF7bExO0XHFF9")
					.setOAuthAccessTokenSecret("ovy520GiDENaXo1gIItGljGJtacePqysEgNYTBaGzEogH");
					TwitterFactory tf = new TwitterFactory(cb.build());
					Twitter twitter = tf.getInstance();

					//				Twitter t = TwitterFactory.getSingleton();
					Query query = new Query(eventHashTag);
					try {
						final QueryResult result = twitter.search(query);
						handler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int index = 0;
								for (Status status : result.getTweets()) {
//									System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
//									txtEventName.setText(status.getUser().getScreenName());
									if(index == 4){
										break;
									}
									tweetAdapter.add(status);
									index++;
								}
								
							}
							
						});
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
			twitterThread.start();
		}

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
			                if(root.has("id") && root.getJSONObject("id").has("username")){
			                	id = root.getJSONObject("id");
			                	// Need place in UI
			                }
			                if(root.has("id") && root.getJSONObject("id").has("description")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "description");
			                	if(temp != null && !temp.isEmpty()){
			                		txtEventDesc.setText(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("location")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "location");
			                	if(temp != null && !temp.isEmpty()){
			                		txtEventLoc.setText(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("hashtag")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "hashtag");
			                	if(temp != null && !temp.isEmpty() && !temp.equals("#")){
			                		getTweets(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("event_date")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "event_date");
			                	if(temp != null && !temp.isEmpty()){
			                		txtEventDate.setText(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("event_time")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "event_time");
			                	if(temp != null && !temp.isEmpty()){
			                		txtEventTime.setText(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("event_time")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "event_time");
			                	if(temp != null && !temp.isEmpty()){
			                		txtEventTime.setText(temp);
			                	}
			                }
			                if(root.has("id") && root.getJSONObject("id").has("image_name")){
			                	id = root.getJSONObject("id");
			                	temp = Messages.safeJSON(id, "image_name");
			                	if(temp != null && !temp.isEmpty()){
			                		Utils.loadImage(eventIcon, "http://cmsc436.striveforthehighest.com/storage/pictures/" + temp);
			                	}
			                }
			            }catch (JSONException w){
			                w.printStackTrace();
			            }
					}
					
				});
				
		}
		
	}

}
