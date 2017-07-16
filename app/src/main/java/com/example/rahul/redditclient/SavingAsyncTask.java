package com.example.rahul.redditclient;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.rahul.redditclient.RedditContract.RedditEntry;

/**
 * Created by Rahul on 16-07-2017.
 */

public class SavingAsyncTask extends AsyncTask<Void,Void,Void> {


    private Context mContext;
    private List<Reddit> mList = new ArrayList<>();

    public SavingAsyncTask(Context context, List<Reddit> list) {
        mContext = context;
        mList = list;
    }
    @Override
    protected Void doInBackground(Void... params) {

        Log.v(":::",String.valueOf(mList.size()));



        for (int i = 0; i < mList.size(); ++i) {

            Reddit currentReddit = mList.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(RedditEntry.COLUMN_TITLE, currentReddit.getTitle());
            contentValues.put(RedditEntry.COLUMN_DATE, currentReddit.getTimestamp());
            contentValues.put(RedditEntry.COLUMN_NOOFCOMMENTS, currentReddit.getComments());

            Log.v(":::",currentReddit.getComments());

            mContext.getContentResolver().insert(RedditContract.CONTENT_URI, contentValues);
        }

        return null;
    }
}
