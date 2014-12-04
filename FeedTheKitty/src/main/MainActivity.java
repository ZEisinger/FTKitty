package main;

// TODO: https://developers.facebook.com/docs/android/login-with-facebook/v2.2
// TODO: http://www.w3schools.com/php/php_file_upload.asp
// TODO: Application drawer

//import history.Fragments.EventListFragment;
import history.Fragments.HistoryListFragment;
import umd.cmsc.feedthekitty.R;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import EventFragments.EventCreateFragment;
import EventFragments.EventListFragment;
import EventFragments.PrivateEventListFragment;
import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String TAG = "MainActivity";
	
    /**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
	private UiLifecycleHelper uiHelper;
	
	//private FriendsList mFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color
				.rgb(163, 73, 164)));
        setContentView(R.layout.feed_the_kitty);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
		getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
				getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
				getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout );
	    uiHelper = new UiLifecycleHelper(MainActivity.this, callback);
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
	    //mFriendsList = new FriendsList(MainActivity.this);
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
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
		onSectionAttached(position);
		getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
    	switch (position) {
        	case 0:
        		fragmentManager.beginTransaction()
				.replace(R.id.container, new EventCreateFragment()).addToBackStack("event_create").commit();				
        		break;
        	case 1:
        		fragmentManager.beginTransaction()
				.replace(R.id.container, new EventListFragment()).addToBackStack("event_public").commit();
        		break;
            case 2:
            	fragmentManager.beginTransaction()
            	.replace(R.id.container, new PrivateEventListFragment()).addToBackStack("event_private").commit();
            	break;
            case 3:
		        fragmentManager.beginTransaction()
					.replace(R.id.container, new HistoryListFragment()).addToBackStack("event_history").commit();
				break;
//            case NavigationDrawerFragment.LOG_OUT:
//			fragmentManager
//					.beginTransaction()
//					.replace(R.id.container,
//							SettingsFragment.newInstance(position)).commit();
//		        break;
    	}
    }

    public void onSectionAttached(int number) {
        switch (number) {
        	case 0:
        		mTitle = getString(R.string.btn_event_create);
        		break;
            case 1:
                mTitle = getString(R.string.title_public);
                break;
            case 2:
                mTitle = getString(R.string.title_private);
                break;
            case 3:
            	mTitle = getString(R.string.title_history);
            	break;
//            case NavigationDrawerFragment.LOG_OUT:
//            	mTitle = getString(R.string.settings);
//            	break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
		getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
		public void call(Session session, SessionState state,
				Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	protected void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        Intent i = new Intent(MainActivity.this, LoginActivity.class);
	        startActivity(i);
	    }
	}
	
	public CharSequence getActivityTitle(){
		return mTitle;
	}

	@Override
    public void onBackPressed()
    {
        getActionBar().setTitle(Html.fromHtml("<font color='#FE8909'>"+ mTitle +"</font>"));
        if (mNavigationDrawerFragment.isDrawerOpen())
        	if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                getFragmentManager().popBackStack();
            }
        super.onBackPressed();
    }
	
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.main_fragment, container,
					false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
