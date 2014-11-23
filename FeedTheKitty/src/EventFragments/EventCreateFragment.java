package EventFragments;

import java.io.File;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Events.EventItem;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EventCreateFragment extends Fragment{
	
	private Button btnCreateEvent;
	private BootstrapEditText txtEventName;
	private BootstrapEditText txtEventDesc;
	private BootstrapEditText txtEventLoc;
	private BootstrapEditText txtHashTag;
	private BootstrapButton btnImageUpload;
	private RadioGroup visibilityGroup;
	private RadioButton radioPublic, radioPrivate;
	private String selectedImagePath;
	
	private static final int SELECT_PICTURE = 1;

		
	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Create Event");
		
		txtEventName = (BootstrapEditText) getActivity().findViewById(R.id.event_create_edit_name);
		txtEventDesc = (BootstrapEditText) getActivity().findViewById(R.id.event_create_edit_desc);
		txtEventLoc = (BootstrapEditText) getActivity().findViewById(R.id.event_create_edit_loc);
		txtHashTag = (BootstrapEditText) getActivity().findViewById(R.id.event_create_edit_hashtag);
		visibilityGroup = (RadioGroup) getActivity().findViewById(R.id.event_create_radio);
		radioPublic = (RadioButton) getActivity().findViewById(R.id.event_create_radio_public);
		radioPrivate = (RadioButton) getActivity().findViewById(R.id.event_create_radio_private);
		
		btnImageUpload = (BootstrapButton) getActivity().findViewById(R.id.btn_event_image_upload);
		btnImageUpload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent,
						"Select Picture"), SELECT_PICTURE);
			}

		});
		
		txtEventName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txtEventName.setDefault();
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		txtEventDesc.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txtEventDesc.setDefault();
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});

		txtEventLoc.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txtEventLoc.setDefault();
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		btnCreateEvent = (Button) getActivity().findViewById(R.id.btn_event_submit);
		btnCreateEvent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int checkedID = visibilityGroup.getCheckedRadioButtonId();
				
				if(txtEventName.getText().toString().isEmpty()){
					txtEventName.setError(getString(R.string.error_empty));
					txtEventName.setDanger();
				}
				
				if(txtEventDesc.getText().toString().isEmpty()){
					txtEventDesc.setError(getString(R.string.error_empty));
					txtEventDesc.setDanger();
				}
				
				if(txtEventLoc.getText().toString().isEmpty()){
					txtEventLoc.setError(getString(R.string.error_empty));
					txtEventLoc.setDanger();
				}
				
//				if(checkedID == radioPrivate.getId()){
//					EventItem privateEvent = new EventItem(txtEventName.getText().toString(),
//							txtEventDesc.getText().toString());
//					//PrivateEventListFragment.privateEventAdapter.add(privateEvent);
//				}
			}
			
		});
		
		
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.event_fragment_create, container, false);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getRealPathFromURI(selectedImageUri);
				Log.d("TAG", "FILE: " + selectedImagePath);
				final File fileToUpload = new File(selectedImagePath);
				Ion.with(getActivity())
				            .load("http://cmsc436.striveforthehighest.com/api/receivePhoto.php")
				            .setTimeout(60 * 60 * 1000)
				            .setMultipartFile("fileToUpload", "image/jpeg", fileToUpload)
				            .asString()
				            .setCallback(new FutureCallback<String>() {
				                @Override
				                public void onCompleted(Exception e, String result) {
				                    // When the loop is finished, updates the notification
				                    if (e != null) {
				                    	Log.d("TAG", "Error: " + e.getMessage());
				                    	return;
				                    }
				                    Log.d("IMAGE", "JSON: " + result);
				                }
				            });
			}
		}
	}
	
	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}

}

