package com.mglabs.newsreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);

        DownloaTask task = new DownloaTask();
        try {
            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream in;
            int data;

            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;          //use StringBuilder instead to optimize performance
                    data = reader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int numberOfItems = 20;
            JSONArray jsonArray;


            try {
                jsonArray = new JSONArray(result);
                if (jsonArray.length() < 20) {
                    numberOfItems = jsonArray.length();
                }

                for (int i = 0; i < numberOfItems; i++) {
                    String articleID = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleID + ".json?print=pretty");
                    if (httpURLConnection != null) {
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                    }
                    in = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    data = reader.read();
                    String articleInfo = "";
                    while (data != -1) {
                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();
                    }
                    Log.d("ARTICOLO", articleInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
