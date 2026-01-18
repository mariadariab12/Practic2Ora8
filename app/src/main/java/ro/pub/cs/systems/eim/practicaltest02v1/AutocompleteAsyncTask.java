package ro.pub.cs.systems.eim.practicaltest02v1;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class AutocompleteAsyncTask extends AsyncTask<String, Void, String> {

    private final TextView resultTextView;

    public AutocompleteAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Constants.AUTOCOMPLETE_ADDRESS + params[0]);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpGet, responseHandler);
        } catch (ClientProtocolException clientProtocolException) {
            Log.e(Constants.TAG, clientProtocolException.getMessage());
        } catch (IOException ioException) {
            Log.e(Constants.TAG, ioException.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String content) {
        if (content == null) {
            resultTextView.setText("Eroare la obtinerea datelor");
            return;
        }

        try {
            // 🔹 Exemplu: parsezi JSON-ul
            JSONObject jsonObject = new JSONObject(content);

            // ⚠️ EXEMPLU – adaptezi dupa structura reala a JSON-ului tau
            JSONArray suggestions = jsonObject.getJSONArray("suggestions");

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < suggestions.length(); i++) {
                result.append(suggestions.getString(i)).append("\n");
            }

            resultTextView.setText(result.toString());

        } catch (JSONException e) {
            Log.e(Constants.TAG, "JSON parsing error", e);
            resultTextView.setText("Eroare la parsarea JSON");
        }
    }
}
