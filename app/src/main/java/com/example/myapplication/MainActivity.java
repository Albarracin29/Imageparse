package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView movieTitle;
    private TextView movieYear;

    private ImageView movieImage;
    private EditText searchText;
    private RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieTitle = findViewById(R.id.movieTitle);
        movieYear = findViewById(R.id.movieYear);

        movieImage = findViewById(R.id.imageView);
        searchText = findViewById(R.id.editTextTextPersonName);

        myQueue = Volley.newRequestQueue(this);
    }

    public void searchMovie(View v) {
        getRequest();
    }

    private void getRequest() {
        String temp_url = searchText.getText().toString();
        String url = "https://search.imdbot.workers.dev/?q="+temp_url;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("description");
                            JSONObject details = jsonArray.getJSONObject(0);

                            String movie = details.getString("#TITLE");
                            int year = details.getInt("#YEAR");
                            String poster_url = details.getString("#IMG_POSTER");

                            movieTitle.setText(movie);
                            movieYear.setText(Integer.toString(year));
                            Picasso.with(MainActivity.this).load(poster_url).into(movieImage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myQueue.add(request);

    }
}
