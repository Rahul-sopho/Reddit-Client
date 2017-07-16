package com.example.rahul.redditclient;

/**
 * Created by Rahul on 15-07-2017.
 */

public class Reddit {

    private String thumbnail;
    private String timestamp;
    private String title;
    private String comments;





    public String getThumbnail() {
        return thumbnail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getComments() {
        return comments;
    }

    public Reddit( String thumbnail, String timestamp, String title, String comments) {

        this.thumbnail = thumbnail;
        this.timestamp = timestamp;
        this.title = title;
        this.comments = comments;
    }
}
