package com.example.myapplication;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView xmlListView;
    private int feedLimit = 10;
    private String feedUrl = "https://www.nasa.gov/news-release/feed/";
    private String feedCachedUrl = "INVALIDATED";
    private static final String STATE_URL = "feedUrl";
    private static final String STATE_LIMIT = "feedLimit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlListView = findViewById(R.id.xmlListView);
        if (savedInstanceState != null){
            feedUrl = savedInstanceState.getString(STATE_URL);
            feedLimit = savedInstanceState.getInt(STATE_LIMIT);
        }
        downloadUrl("https://www.nasa.gov/news-release/feed/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu,menu);
        if (feedLimit == 10){
            menu.findItem(R.id.menu_top10).setChecked(true);
        }else {
            menu.findItem(R.id.menu_top5).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_free) {
            feedUrl = "https://www.nasa.gov/news-release/feed/";
        } else if (id == R.id.menu_paid) {
            feedUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        } else if (id == R.id.menu_news) {
            feedUrl = "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
        } else if (id == R.id.menu_top5 || id == R.id.menu_top10){
            if (!item.isChecked()){
                item.setChecked(true);
                feedLimit = 15 - feedLimit;
                feedCachedUrl = "INVALIDATED";
                Log.d(TAG, "onOptionsItemSelected: feedLimit set to " + feedLimit);
            }else {
                Log.d(TAG, "onOptionsItemSelected: feedLimit unchanged");
            }
        } else if (id == R.id.menu_refresh) {
            feedCachedUrl = "INVALIDATED";
        } else {
            return super.onOptionsItemSelected(item);
        }
        downloadUrl(feedUrl);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_URL,feedUrl);
        outState.putInt(STATE_LIMIT,feedLimit);
        super.onSaveInstanceState(outState);
    }

    private void downloadUrl(String feedUrl) {
        if (!feedUrl.equalsIgnoreCase(feedCachedUrl)){
            Log.d(TAG, "downloadUrl: starts");
            DownloadData downloadData = new DownloadData();
            downloadData.executeDownload(feedUrl);
            feedCachedUrl = feedUrl;
            Log.d(TAG, "downloadUrl: ends");
        }else {
            Log.d(TAG, "downloadUrl: URL not changed");
        }
    }

    public class DownloadData {
        private static final String TAG = "DownloadData";
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeDownload(String... strings) {
            executorService.submit(() -> {
                // Arka planda çalışacak kod (doInBackground eşdeğeri)
                Log.d(TAG, "doInBackground: starts with " + strings[0]);
                String rssFeed = downloadFromUrl(strings[0]);
                if(rssFeed==null){
                    Log.e(TAG, "doInBackground: Error downloading");
                }
                // Ana iş parçacığında çalışacak kod (onPostExecute eşdeğeri)
                handler.post(() -> {
//                    Log.d(TAG, "onPostExecute: result = " + rssFeed);
                    ParseApplications parseApplications = new ParseApplications();
                    parseApplications.parse(rssFeed,feedLimit);

                    FeedAdapter<FeedEntry> arrayAdapter = new FeedAdapter<>(
                            MainActivity.this,R.layout.list_record,parseApplications.getApplications());
                    xmlListView.setAdapter(arrayAdapter);
                });
            });
        }

        private String downloadFromUrl(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int respone = connection.getResponseCode();
                Log.d(TAG, "downloadFromUrl: The response code was " + respone);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = bufferedReader.read(inputBuffer);
                    if (charsRead < 0){
                        break;
                    }
                    if(charsRead > 0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                bufferedReader.close();
                return xmlResult.toString();
            }catch(MalformedURLException e){
                Log.e(TAG, "downloadFromUrl: Invalid URL " + e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "downloadFromUrl: IO Exception reading data " + e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "downloadFromUrl: Security Exception. Needs permission? " + e.getMessage());
            }
            return null;
        }
    }
}