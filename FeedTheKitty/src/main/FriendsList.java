package main;

import java.util.List;

import Utils.CoreCallbackString;
import android.content.Context;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class FriendsList {
	
	protected static final String TAG = "FriendsList Object";
	String friendsList;
	Context mContext;
	public String results;
	public Boolean ready;
	public String userId;
	public String firstName;

	public FriendsList (Context context, final CoreCallbackString cbUserID, final CoreCallbackString cbFriends)
	{
		mContext = context;
		ready = false;
		results = null;

        if (Session.getActiveSession().isOpened()) {
        	// Request user data and show the results
            Request.newMeRequest(
            		Session.getActiveSession(),
            		new GraphUserCallback()
		            {
		                @Override
		                public void onCompleted(GraphUser user, Response response)
		                {
		                    if (null != user)
		                    {
		                    	userId = user.getId();
		                    	Log.i(TAG, "UserId: " + userId);
		                        firstName = user.getFirstName();
		                    	Log.i(TAG, "FirstName: " + firstName);
		                    	if(cbUserID != null){
		                    		String fullName = firstName + " " + user.getLastName();
		                    		cbUserID.run(userId, fullName);
		                    	}
		                    }
		                }
		            }
            ).executeAsync();
            
			Request.newMyFriendsRequest(
					Session.getActiveSession(),
				    new GraphUserListCallback()
				    {
				        @Override
				        public void onCompleted(List<GraphUser> users, Response response)
				        {
				            if (users != null)
			                {
				            	friendsList = "";
				                for (GraphUser user : users)
				                {
				                    friendsList = friendsList + user.getId() + ",";
				                }
			                }
	                    	Log.i(TAG, "response: " + response);
	                    	Log.i(TAG, "users: " + users);
	                    	Log.i(TAG, "friendsList: " + friendsList);
	                    	if(cbFriends != null){
	                    		cbFriends.run(friendsList, "");
	                    	}
	                    	setPrivateEvents();
				        }
				    }
			).executeAsync();
        }
	}
	
	public void setPrivateEvents () {
		String eventUrl = "http://cmsc436.striveforthehighest.com/api/getEventList.php";
	    Ion.with(mContext).load(eventUrl)
	            .setBodyParameter("friend_list", friendsList)
	            .setBodyParameter("visibility", "private")
	            .asString().setCallback(new FutureCallback<String>() {
	        @Override
	        public void onCompleted(Exception e, String result) {
	        	results = result;
            	Log.i(TAG, "results: " + results);
	        }
	    });
	}
}
