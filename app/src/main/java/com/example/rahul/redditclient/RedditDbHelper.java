package com.example.rahul.redditclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.rahul.redditclient.RedditContract.RedditEntry;

/**
 * Created by Rahul on 16-07-2017.
 */

public class RedditDbHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 4;

        private static final String DATABASE_NAME = "post";

        private static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + RedditEntry.TABLE_NAME + " (" +
                        RedditEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        RedditEntry.COLUMN_TITLE + " TEXT," +
                        RedditEntry.COLUMN_DATE + " TEXT, " +
                        RedditEntry.COLUMN_NOOFCOMMENTS + " TEXT, " +
                        RedditEntry.COLUMN_THUMBNAIL_URL + " TEXT);";

        private static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + RedditEntry.TABLE_NAME;


    public RedditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
