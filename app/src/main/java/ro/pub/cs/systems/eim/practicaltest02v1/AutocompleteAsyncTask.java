package ro.pub.cs.systems.eim.practicaltest02v1;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class AutocompleteAsyncTask extends AsyncTask<String, Void, String> {

    //private final TextView resultTextView;
    private final Context context;

//    public AutocompleteAsyncTask(TextView resultTextView) {
//        this.resultTextView = resultTextView;
//    }
    public AutocompleteAsyncTask(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... prefix) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Constants.AUTOCOMPLETE_ADDRESS + prefix[0].toString());
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpGet, responseHandler);
        } catch (ClientProtocolException clientProtocolException) {
            Log.i(Constants.TAG, clientProtocolException.getMessage());
        } catch (IOException ioException) {
            Log.i(Constants.TAG, ioException.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String content) {
//        if (content == null) {
//            resultTextView.setText("Eroare la obtinerea datelor");
//            return;
//        }

        try {
            JSONArray jsonarray = new JSONArray(content);
            JSONArray suggestions = jsonarray.getJSONArray(1);

            String third = suggestions.getString(2);

            Log.d(Constants.TAG, "Third entry: " + third);

            // BROADCAST
            Intent intent = new Intent(Constants.ACTION_DEFINITION_READY);
            intent.setPackage(context.getPackageName());
            intent.putExtra(Constants.EXTRA_DEFINITION, third);
            context.sendBroadcast(intent);
            if (context != null && intent != null) {
                Log.i(Constants.TAG, "Context și Intent există! Trimit broadcast...");
            } else {
                Log.e(Constants.TAG, "Context sau Intent NU există! Nu se poate trimite broadcast");
            }


//            StringBuilder result = new StringBuilder();
//            for (int i = 0; i < suggestions.length(); i++) {
//                Log.i(Constants.TAG, suggestions.getString(i));
//                result.append(suggestions.getString(i)).append("\n");
//            }
//
//            resultTextView.setText(result.toString());

        } catch (JSONException e) {
            Log.e(Constants.TAG, "JSON parsing error", e);
//            resultTextView.setText("Eroare la parsarea JSON");
        }
    }
}
