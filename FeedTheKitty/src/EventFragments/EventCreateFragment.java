package EventFragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import Events.EventItem;
import Venmo.Messages;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class EventCreateFragment extends Fragment{

	private BootstrapButton btnCreateEvent;
	private BootstrapEditText txtEventName;
	private BootstrapEditText txtEventDesc;
	private BootstrapEditText txtEventLoc;
	private BootstrapEditText txtHashTag;
	private BootstrapButton btnImageUpload;
	private DatePicker eventDate;
	private TimePicker eventTime;
	private ImageView uploadPreview;
	private RadioGroup visibilityGroup;
	private RadioButton radioPublic, radioPrivate;
	private String selectedImagePath;
	private String imageName;
	private String paymentEmail;

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
		eventDate = (DatePicker) getActivity().findViewById(R.id.event_create_pick_date);
		eventTime = (TimePicker) getActivity().findViewById(R.id.event_create_pick_time); 
		uploadPreview = (ImageView) getActivity().findViewById(R.id.image_view_upload);

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

		btnCreateEvent = (BootstrapButton) getActivity().findViewById(R.id.btn_event_submit);
		btnCreateEvent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int checkedID = visibilityGroup.getCheckedRadioButtonId();
				String visibility = "private";
				String date = getDateFromDatePicker(eventDate);
				String time = getTimeFromTimePicker(eventTime);
				boolean flag = false;


				if(txtEventName.getText().toString().isEmpty()){
					txtEventName.setError(getString(R.string.error_empty));
					txtEventName.setDanger();
					flag = false;
				}else{
					flag = true;
				}

				if(txtEventLoc.getText().toString().isEmpty()){
					txtEventLoc.setError(getString(R.string.error_empty));
					txtEventLoc.setDanger();
					flag = false;
				}else{
					flag = true;
				}

				if(checkedID == radioPublic.getId()){
					visibility = "public";
				}
				Log.d("FLAG","Flag: " + flag);
				if(flag){
					Ion.with(getActivity())
					.load("http://cmsc436.striveforthehighest.com/api/insertEvent.php")
					.setBodyParameter("username", "steven")
					.setBodyParameter("event_name", txtEventName.getText().toString())
					.setBodyParameter("description", txtEventDesc.getText().toString())
					.setBodyParameter("location", txtEventLoc.getText().toString())
					.setBodyParameter("hashtag", "#"+txtHashTag.getText().toString())
					.setBodyParameter("event_date", date)
					.setBodyParameter("event_time", time)
					.setBodyParameter("visibility", visibility)
					.setBodyParameter("image_name", imageName)
					.setBodyParameter("payment_email", "stevenberger101@gmail.com")
					.setBodyParameter("end", "false")
					.asString()
					.setCallback(new FutureCallback<String>(){

						@Override
						public void onCompleted(Exception e, String result) {
							// TODO Auto-generated method stub
							if (e != null) {
								Log.d("TAG", "Error: " + e.getMessage());
								return;
							}
							Log.d("CREATE", "JSON: " + result);
						}

					});
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
		return inflater.inflate(R.layout.event_fragment_create, container, false);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				btnImageUpload.setEnabled(false);
				Uri selectedImageUri = data.getData();
				selectedImagePath = getRealPathFromURI(selectedImageUri);
				Log.d("TAG", "FILE: " + selectedImagePath);
				final File fileToUpload = new File(selectedImagePath);

				if(fileToUpload.exists()){
					Bitmap b = BitmapFactory.decodeFile(fileToUpload.getAbsolutePath());
					uploadPreview.setImageBitmap(b);

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
				            JSONTokener tokener = new JSONTokener(result);

				            try{
				                JSONObject root = new JSONObject(tokener);
				                if(root.has("error")){
				                    Log.d("ERROR", "Error: " + Messages.safeJSON(root, "error"));
				                }
				                if(root.has("name")){
				                	imageName = Messages.safeJSON(root, "name");
				                }
				                btnImageUpload.setEnabled(true);
				            }catch (JSONException w){
				                w.printStackTrace();
				            }
						}
					});
				}
			}
		}
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null); 
		cursor.moveToFirst(); 
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		return cursor.getString(idx); 
	}

	private String getDateFromDatePicker(DatePicker datePicker){

		long dateTime = datePicker.getCalendarView().getDate();
		Date date = new Date(dateTime);

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateString = sdf.format(date);

		return dateString;
	}

	private String getTimeFromTimePicker(TimePicker timePicker){
		int hour = timePicker.getCurrentHour();
		int minutes = timePicker.getCurrentMinute();
		boolean isPM = (hour / 12) > 0;
		hour %= 12;
		String strHour = String.format("%02d", hour);
		String strMinutes = String.format("%02d", minutes);

		String msg = strHour + ":" + strMinutes;

		if(isPM){
			msg += " PM";
		}else{
			msg += " AM";
		}

		return msg;
	}

}

