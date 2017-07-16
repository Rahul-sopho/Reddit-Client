package com.example.rahul.redditclient;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rahul on 16-07-2017.
 */

public class RedditContract {

    static final String CONTENT_AUTHORITY = "com.example.rahul.redditclient";
    static final String PATH_CAPTION = "post";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_CAPTION);

    public class RedditEntry implements BaseColumns
    {
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOOFCOMMENTS = "noOfComments";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

        static final String TABLE_NAME = "reddit";
    }


}
