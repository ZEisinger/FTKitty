package Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpFactory {

    public static HttpPostTask httpPoster(CustomHttpCallback cb, CoreCallback cbError){
        return new HttpPostTask(cb, cbError);
    }

    public static class HttpPostTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "HttpPostTask";
        private boolean noError = true;
        private CustomHttpCallback cb;
        private CoreCallback cbError;

        public HttpPostTask(CustomHttpCallback cb, CoreCallback cbError){
            this.cb = cb;
            this.cbError = cbError;
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            HttpsURLConnection httpUrlConnection = null;

            Log.d("POST", params[0]);

            try {
                httpUrlConnection = (HttpsURLConnection) new URL(params[0])
                        .openConnection();
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);

                httpUrlConnection.setRequestMethod("POST");
                InputStream in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());

                data = readStream(in);

            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                noError = false;
            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            if(!noError) {
                if(cbError != null){
                    cbError.run();
                }
            }else{
                if(cb != null){
                    cb.run(result);
                }
            }

        }

        private String readStream(InputStream in) throws IOException{
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }
    }
}
