package com.example.newsaggregator_v2;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceLoader {

    private static final String TAG = "SourceLoader";
    private static final String sourceURL = "https://newsapi.org/v2/sources?" +
            "apiKey=ef2a7251a56f459eaa43aa336d0269bc";

    private static MainActivity mainActivity;
    private static RequestQueue queue;

    public static void downloadSource(MainActivity mainActivityIn){
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(sourceURL).buildUpon();
        String urlToUse = buildURL.build().toString();
        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());
        Response.ErrorListener error =
                error1 -> mainActivity.updateSourceData(null);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONArray jSourceArray = jObjMain.getJSONArray("sources");

            ArrayList<Source> slist = new ArrayList<>();

            for(int i = 0; i < jSourceArray.length(); i++) {
                JSONObject jSource = jSourceArray.getJSONObject(i);
                String sourceId = jSource.getString("id");
                String sourceName = jSource.getString("name");
                String sourceCate = jSource.getString("category");
                slist.add(new Source(sourceId, sourceName, sourceCate));
            }

            mainActivity.updateSourceData(slist);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
