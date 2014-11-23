package Venmo;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Steven on 10/26/2014.
 */
public class CustomWebViewClient extends WebViewClient {

    private final static String checkURL = "https://api.venmo.com/v1/oauth/authorize?client_id=2053&scope=make_payments%20access_profile&response_type=token";
    private static OAuthCallback authCallback;

    public void setOAuthCallback(OAuthCallback cb){
        authCallback = cb;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if(!checkURL.equals(url)){
            authCallback.run(url);
        }
    }
}

