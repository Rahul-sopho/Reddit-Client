package com.example.rahul.redditclient;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.rahul.redditclient.RedditContract.RedditEntry;

/**
 * Created by Rahul on 16-07-2017.
 */

public class RedditProvider extends ContentProvider {

    static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    RedditDbHelper mDatabaseHelper;
    SQLiteDatabase mDatabase;

    private static final int URI_WITHOUT_PATH = 100;
    private static final int URI_WITH_PATH = 101;

    static {
        mUriMatcher.addURI(RedditContract.CONTENT_AUTHORITY, RedditContract.PATH_CAPTION, URI_WITHOUT_PATH);
        mUriMatcher.addURI(RedditContract.CONTENT_AUTHORITY, RedditContract.PATH_CAPTION + "/#", URI_WITH_PATH);
    }

    @Override
    public boolean onCreate() {

        mDatabaseHelper = new RedditDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        mDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (mUriMatcher.match(uri))
        {
            case URI_WITHOUT_PATH :
                cursor = mDatabase.query(RedditEntry.TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;

            case URI_WITH_PATH :
                selection = RedditEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = mDatabase.query(RedditEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default : throw new IllegalArgumentException("Cannot serve URI request at this moment.");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        mDatabase = mDatabaseHelper.getWritableDatabase();
        long id;

        switch (mUriMatcher.match(uri))
        {
            case URI_WITHOUT_PATH:
                id = mDatabase.insert(RedditEntry.TABLE_NAME, null, values);
                if (id == -1)
                    Toast.makeText(getContext(), "Failed to insert", Toast.LENGTH_SHORT).show();
                break;

            default: throw new IllegalArgumentException("Cannot serve URI request at this moment.");
        }

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Delete is not implemented here");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Update is not implemented here");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
