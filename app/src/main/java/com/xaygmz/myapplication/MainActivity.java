package com.xaygmz.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private ArrayList<FeedModal> instaModalArrayList;
    private ArrayList<FeedModal> facebookFeedModalArrayList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.idLoadingPB);

        getFacebookFeeds();
    }

    private void getFacebookFeeds() {
        facebookFeedModalArrayList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);


        mRequestQueue.getCache().clear();
        String url = "https://jsonkeeper.com/b/OB3B";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {

                    String authorName = response.getString("authorName");
                    String authorImage = response.getString("authorImage");

                    JSONArray feedsArray = response.getJSONArray("feeds");

                    for (int i = 0; i < feedsArray.length(); i++) {
                        JSONObject feedsObj = feedsArray.getJSONObject(i);

                        String postDate = feedsObj.getString("postDate");
                        String postDescription = feedsObj.getString("postDescription");
                        String postIV = feedsObj.getString("postIV");
                        String postLikes = feedsObj.getString("postLikes");
                        String postComments = feedsObj.getString("postComments");

                        FeedModal feedModal = new FeedModal(authorImage, authorName, postDate, postDescription, postIV, postLikes, postComments);
                        facebookFeedModalArrayList.add(feedModal);

                        FeedAdapter adapter = new FeedAdapter(facebookFeedModalArrayList, MainActivity.this);
                        RecyclerView instRV = findViewById(R.id.idRVInstaFeeds);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

                        instRV.setLayoutManager(linearLayoutManager);
                        instRV.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get data with error " + error, Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }
}