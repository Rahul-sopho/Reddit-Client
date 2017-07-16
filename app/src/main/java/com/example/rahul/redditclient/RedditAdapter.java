package com.example.rahul.redditclient;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.rahul.redditclient.R.id.thumbnail;

/**
 * Created by Rahul on 15-07-2017.
 */

public class RedditAdapter extends RecyclerView.Adapter<RedditAdapter.ViewHolder>
{

    private List<Reddit> redditList;
    private Context context;



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timestamp,comments;
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            comments = (TextView) view.findViewById(R.id.comments);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }



    public RedditAdapter(MainActivity mainActivity, List<Reddit> redditList,Context context) {
        this.redditList = redditList;
        this.context = context;
    }

    public RedditAdapter(OfflineReddit offlineReddit, List<Reddit> redditList) {
        this.redditList = redditList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reddit_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reddit red = redditList.get(position);
        holder.title.setText(red.getTitle());
        holder.timestamp.setText(red.getTimestamp());
        holder.comments.setText("Comments:"+red.getComments());



        Picasso.with(context).load(red.getThumbnail()).resize(1400,730).into(holder.thumbnail);






    }

    @Override
    public int getItemCount() {


        return redditList.size();
    }
}