package com.example.newsaggregator_v2;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StoryLoader {

    //https://newsapi.org/v2/top-headlines?sources=cnn&apiKey=
    private static final String TAG = "StoryLoader";
    private static final String sourceURL = "https://newsapi.org/v2/top-headlines?";
    private static final String apiKey = "ef2a7251a56f459eaa43aa336d0269bc";

    private static MainActivity mainActivity;
    private static RequestQueue queue;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadStory(MainActivity mainActivityIn, String sourceName){
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(sourceURL).buildUpon();
        buildURL.appendQueryParameter("sources", sourceName);
        buildURL.appendQueryParameter("apiKey", apiKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "downloadStory: sourceName  " + sourceName);
        Log.d(TAG, "downloadStory: " + urlToUse);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONArray jStoryArray = jObjMain.getJSONArray("articles");
            ArrayList<Story> slist = new ArrayList<>();

            int length = 10;
            if (jStoryArray.length() <= 10) {
                length = jStoryArray.length();
            }
            for(int i = 0; i < length; i++) {
                JSONObject jSource = jStoryArray.getJSONObject(i);
                String storyAuthor = jSource.getString("author");
                String storyTitle = jSource.getString("title");
                String storyDesc = jSource.getString("description");
                String storyUrl = jSource.getString("url");
                String storyUrlToImage = jSource.getString("urlToImage");
                String storyPublishedAt = jSource.getString("publishedAt");

                DateTimeFormatter inputFormatter;
                if (storyPublishedAt.contains("Z")) {
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                } else if (storyPublishedAt.contains("+")) {
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+'SS:SS", Locale.ENGLISH);
                } else {
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                }
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("LLLL dd, yyy HH:ss", Locale.ENGLISH);
                LocalDateTime dates = LocalDateTime.parse(storyPublishedAt, inputFormatter);

                storyPublishedAt = outputFormatter.format((dates));

                int count = i + 1;
                String articleCount = count + " of " + length;
                slist.add(new
                        Story(storyAuthor, storyTitle, storyDesc,
                        storyUrl,storyUrlToImage, storyPublishedAt, articleCount)
                );
                Log.d(TAG, "downloadStory: " + slist.size());
            }

            mainActivity.updateStoryData(slist);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
