package com.example.rahul.redditclient;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static String reddit_url="https://www.reddit.com/r/todayilearned/top.json?limit=10";

    ProgressBar progressBar;

    RedditAdapter mAdpter;
    RecyclerView redditListView;

    List<Reddit> mReddit = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        redditListView=(RecyclerView)findViewById(R.id.list) ;

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);



        redditListView.setLayoutManager(linearLayoutManager);
        redditListView.setHasFixedSize(false);

        redditListView.addOnScrollListener(createInfiniteScrollListener());



        // Create a new adapter that takes an empty list of reddits as input

        RedditAsyncTask task = new RedditAsyncTask();
        task.execute(reddit_url);


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection


        switch (item.getItemId()) {
            case R.id.offline:
                startActivity(new Intent(this, OfflineReddit.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(35, linearLayoutManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {

                RedditAsyncTask task = new RedditAsyncTask();
                task.execute(reddit_url);
                refreshView(redditListView, new RedditAdapter(MainActivity.this, mReddit,getApplicationContext()), mReddit.size()-1);

                progressBar.setVisibility(View.GONE);
            }
        };
    }




    private class RedditAsyncTask extends AsyncTask<String, Void, List<Reddit>> {


        String before;

        RedditAdapter mAdpter;

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Reddit}s as the result.
         */
        @Override
        protected List<Reddit> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Reddit> result = QueryUtils.fetchRedditData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of reddit data from a previous
         * query to USGS. Then we update the adapter with the new list of reddits,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Reddit> data) {
            // Clear the adapter of previous reddit data


            // If there is a valid list of {@link Reddit}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {

                SavingAsyncTask savingAsyncTask = new SavingAsyncTask(MainActivity.this, data);
                savingAsyncTask.execute();

                mReddit.addAll(data);

                redditListView.setAdapter(new RedditAdapter(MainActivity.this, mReddit,getApplicationContext()));



            }
        }
    }
}



    
