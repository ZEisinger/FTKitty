package main;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import umd.cmsc.feedthekitty.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import EventFragments.EventCreateFragment;
import Utils.*;
import Venmo.CustomWebViewClient;
import Venmo.Messages;
import Venmo.OAuthCallback;

public class VenmoWebViewActivity extends Activity {

	Context mContext;
	private WebView mVenmoWebView;
	String mUrl;
	private String user_id;
	private String amount;
	private String note;
	private String visibility;
	private String verifyOnly;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.event_venmo_webview);

		mUrl = getIntent().getExtras().getString("url");
		user_id = getIntent().getExtras().getString("user_id");
		amount = getIntent().getExtras().getString("amount");
		note = getIntent().getExtras().getString("note");
		visibility = getIntent().getExtras().getString("visibility");
		verifyOnly = getIntent().getExtras().getString("verify_only");

		mContext = this;

		mVenmoWebView = (WebView)getLastNonConfigurationInstance();
		if(mVenmoWebView != null) { //If screen was rotated, don't reload the whole webview

			WebView myWebView = (WebView)findViewById(R.id.venmo_wv);
			RelativeLayout parent = (RelativeLayout)myWebView.getParent();
			parent.removeView(myWebView);

			RelativeLayout parent2 = (RelativeLayout)mVenmoWebView.getParent();
			parent2.removeView(mVenmoWebView);

			parent.addView(mVenmoWebView);
		}
		else { //load the webview
			mVenmoWebView = (WebView)findViewById(R.id.venmo_wv);
			mVenmoWebView.addJavascriptInterface(new VenmoJavaScriptInterface(this), "VenmoAndroidSDK");
			mVenmoWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			mVenmoWebView.getSettings().setJavaScriptEnabled(true);
			mVenmoWebView.getSettings().setUserAgentString("venmo-android-2.0");
			mVenmoWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			CustomWebViewClient customWebViewClient = new CustomWebViewClient();
			customWebViewClient.setOAuthCallback(new OAuthCallback() {
				@Override
				public void run(String token) {
					if(token.contains("?access_token=")){
						String[] garbage = token.split("access_token=");
						String authToken = garbage[1];
						Log.d("VenmoWebViewActivity", authToken);
						Log.d("VENMO", token);
						if(verifyOnly.equals("true")){
							String verifyURL = "https://sandbox-api.venmo.com/v1/me?access_token="+authToken;
							Ion.with(mContext).load(verifyURL)
							.asString()
							.setCallback(new FutureCallback<String>() {

								@Override
								public void onCompleted(Exception e,
										String result) {
									// TODO Auto-generated method stub
									Log.d("VENMO", "VERIFY");
						            JSONTokener tokener = new JSONTokener(result);

						            try{
						                JSONObject root = new JSONObject(tokener);
						                if(root.has("data") && root.getJSONObject("data").has("user")){
						                    JSONObject user = root.getJSONObject("data").getJSONObject("user");
						                    final String temp = Messages.safeJSON(user, "id");
						                    Log.d("USER", "TEMP: " + temp);
											DialogFragment dialogFragment = DialogFactory.createDialogOk(getString(R.string.venmo_verified), new CoreCallback() {
												@Override
												public void run() {
								                    Bundle conData = new Bundle();
								                    conData.putString("venmo_id", temp);
								                    Intent intent = new Intent();
								                    intent.putExtras(conData);
								                    setResult(RESULT_OK, intent);
								                    finish();
												}
											});
											dialogFragment.show(getFragmentManager(), "VenmoDialog");
						            		
						                }
						            }catch (JSONException w){
						                w.printStackTrace();
						            }
								}

							});
						}else{
							String paymentURL = "https://api.venmo.com/v1/payments?access_token="+ authToken + "&user_id="+ user_id +
									"&amount=" + amount + "&note=" + note + "&audience=" + visibility;
							Log.d("VenmoWebViewActivity", paymentURL);

							paymentURL = "https://api.venmo.com/v1/payments";
							Ion.with(mContext).load(paymentURL)
							.setBodyParameter("access_token", authToken)
							.setBodyParameter("user_id", user_id)
							.setBodyParameter("amount", amount)
							.setBodyParameter("note", note)
							.setBodyParameter("audience", visibility)
							.asString().setCallback(new FutureCallback<String>() {
								@Override
								public void onCompleted(Exception e, String result) {

									if(e != null){
										DialogFragment dialogFragment = DialogFactory.createDialogOk(getString(R.string.payment_fail), new CoreCallback() {
											@Override
											public void run() {
												Intent intent = new Intent(VenmoWebViewActivity.this, MainActivity.class);
												startActivity(intent);
											}
										});
										dialogFragment.show(getFragmentManager(), "PaymentDialog");
									}else {

										Log.d("VenmoWebViewActivity", result);
										Messages.PaymentResponse paymentResponse = new Messages.PaymentResponse(result);
										Log.d("VenmoWebViewActivity", "Result: " + paymentResponse.toString());

										Messages.ErrorResponse errorResponse = paymentResponse.getErrorResponse();
										if(errorResponse != null){
											DialogFragment dialogFragment = DialogFactory.createDialogOk(errorResponse.getMessage(), new CoreCallback() {
												@Override
												public void run() {
													Intent intent = new Intent(VenmoWebViewActivity.this, MainActivity.class);
													startActivity(intent);
												}
											});
											dialogFragment.show(getFragmentManager(), "PaymentDialog");
										}else {

											DialogFragment dialogFragment = DialogFactory.createDialogOk(
													getString(R.string.payment_success_base) + "\n" + "$" + paymentResponse.getAmount() + " transferred to " +
															paymentResponse.getUserReceiver().getDisplayName()
															, new CoreCallback() {
														@Override
														public void run() {
															Intent intent = new Intent(VenmoWebViewActivity.this, MainActivity.class);
															startActivity(intent);
														}
													});
											dialogFragment.show(getFragmentManager(), "PaymentDialog");
										}
									}

								}
							});
						}

					}
				}
			});
			mVenmoWebView.setWebViewClient(customWebViewClient);

			mVenmoWebView.loadUrl(mUrl);

		}

	}

	/*called right before screen rotates. We store the webview object so it can be recovered when the activity re-starts*/
	@Override
	public Object onRetainNonConfigurationInstance() {
		return mVenmoWebView;
	}

	//This class handles what happens when the user has successfully paid OR if there's an error, and it's time to
	//yield control back to your previous activity (the one that opened up this Venmo payment webview).
	public class VenmoJavaScriptInterface
	{
		Context mContext;
		Activity mActivity;

		/** Instantiate the interface and set the context */
		VenmoJavaScriptInterface(Context c) {
			mContext = c;
			mActivity = (Activity)c;
		}
		@JavascriptInterface
		public void paymentSuccessful(String signed_request) {
			Intent i = new Intent();
			i.putExtra("signedrequest", signed_request);
			mActivity.setResult(mActivity.RESULT_OK, i);
			mActivity.finish();
		}
		@JavascriptInterface 
		public void error(String error_message) {
			Intent i = new Intent();
			i.putExtra("error_message", error_message);
			mActivity.setResult(mActivity.RESULT_OK, i);
			mActivity.finish();
		}
		@JavascriptInterface 
		public void cancel() {
			Intent i = new Intent();
			mActivity.setResult(mActivity.RESULT_CANCELED);
			mActivity.finish();
		}
	}
}
