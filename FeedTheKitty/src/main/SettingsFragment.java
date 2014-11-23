package main;


import main.NavigationDrawerFragment.NavigationDrawerCallbacks;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import umd.cmsc.feedthekitty.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

    /**
     * A placeholder fragment containing a simple view.
     */
    public class SettingsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

		private NavigationDrawerCallbacks mCallbacks;
		private UiLifecycleHelper uiHelper;

        private static final String ARG_SECTION_NUMBER = "section_number";

    	private static final String TAG = "Settings - Main";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SettingsFragment newInstance(int sectionNumber) {
        	SettingsFragment fragment = new SettingsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SettingsFragment() {
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
		    super.onDestroy();
		    uiHelper.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
		    super.onSaveInstanceState(outState);
		    uiHelper.onSaveInstanceState(outState);
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.setting_fragment, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
            try {
                mCallbacks = (NavigationDrawerCallbacks) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
            }
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
		    } else if (state.isClosed()) {
		        Intent i = new Intent(getActivity(), LoginActivity.class);
		        getActivity().startActivity(i);
		    }
		}
    }
