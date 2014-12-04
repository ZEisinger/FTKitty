package main;

import java.util.Arrays;

import umd.cmsc.feedthekitty.R;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class LoginActivity extends Activity {

	PlaceholderFragment loginFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		if (savedInstanceState == null) {
			loginFragment = new PlaceholderFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, loginFragment).commit();
	    } else {
	        // Or set the fragment from restored state info
	    	loginFragment = (PlaceholderFragment) getFragmentManager()
	    			.findFragmentById(android.R.id.content);
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    loginFragment.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static final String TAG = "PlaceholderFragment";
		private UiLifecycleHelper uiHelper;

		public PlaceholderFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    uiHelper = new UiLifecycleHelper(getActivity(), callback);
		    uiHelper.onCreate(savedInstanceState);
		}
		
		@Override
		public void onResume() {
		    super.onResume();
		    // For scenarios where the main activity is launched and user
		    // session is not null, the session state change notification
		    // may not be triggered. Trigger it if it's open/closed.
		    Session session = Session.getActiveSession();
		    if (session != null &&
		           (session.isOpened() || session.isClosed()) ) {
		        onSessionStateChange(session, session.getState(), null);
		    }

		    uiHelper.onResume();
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    uiHelper.onActivityResult(requestCode, resultCode, data);
		}

		@Override
		public void onPause() {
		    super.onPause();
		    uiHelper.onPause();
		}

		@Override
		public void onDestroy() {
			uiHelper.onDestroy();
		    super.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
		    super.onSaveInstanceState(outState);
		    uiHelper.onSaveInstanceState(outState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.login_fragment,
					container, false);
			LoginButton lb = (LoginButton) rootView.findViewById(R.id.login_authButton);
			lb.setReadPermissions(Arrays.asList("user_friends"));
			
			return rootView;
		}
		

		private Session.StatusCallback callback = new Session.StatusCallback() {
		    @Override
		    public void call(Session session, SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
		};
		
		protected void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		    if (state.isOpened()) {
		        Log.i(TAG, "Logged in...");
		        Intent i = new Intent(getActivity(), MainActivity.class);
		        getActivity().startActivity(i);
		    } else if (state.isClosed()) {
		        Log.i(TAG, "Logged out...");
		    }
		}
	}
	
	@Override
    public void onBackPressed()
    {
		finish();
    }
}
