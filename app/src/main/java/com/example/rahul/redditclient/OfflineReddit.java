package com.example.rahul.redditclient;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rahul.redditclient.RedditContract.RedditEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 16-07-2017.
 */

public class OfflineReddit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID =1;
    RecyclerView redditRecycle;
    RedditAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_reddit);

        redditRecycle = (RecyclerView)findViewById(R.id.list1);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RedditEntry.COLUMN_ID,
                RedditEntry.COLUMN_TITLE,
                RedditEntry.COLUMN_NOOFCOMMENTS,
                RedditEntry.COLUMN_DATE
        };

        return new CursorLoader(OfflineReddit.this, RedditContract.CONTENT_URI, projection, null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        List<Reddit> list = new ArrayList<>();

        if(cursor != null) {

            while (cursor.moveToNext())
            {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(RedditEntry.COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(RedditEntry.COLUMN_DATE));
                String numOfComments = cursor.getString(cursor.getColumnIndexOrThrow(RedditEntry.COLUMN_NOOFCOMMENTS));
                list.add(new Reddit(null, date,title, numOfComments));
            }
            mAdapter = new RedditAdapter(OfflineReddit.this, list);
            redditRecycle.setLayoutManager(new LinearLayoutManager(this));
            redditRecycle.setAdapter(mAdapter);
            redditRecycle.setHasFixedSize(true);
        }

        else
            Toast.makeText(this,"No offline articles found", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //nothing



    }
}
