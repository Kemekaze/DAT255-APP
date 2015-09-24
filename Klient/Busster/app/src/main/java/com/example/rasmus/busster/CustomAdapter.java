package com.example.rasmus.busster;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2015-09-12.
 */
public class CustomAdapter extends BaseAdapter {

    Context ctxt;
    ArrayList<Post> posts;
    LayoutInflater myInflater;



    public CustomAdapter( ArrayList<Post> posts, Context context){
       this.posts = posts;
        this.ctxt = context;
        myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = myInflater.inflate(R.layout.busster_post_item,parent,false);
        }

        TextView textMessage = (TextView) convertView.findViewById(R.id.postMessage);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.postTimeStamp);
        TextView bussLine = (TextView) convertView.findViewById(R.id.bussLine);
        TextView vote = (TextView) convertView.findViewById(R.id.votes);

        Post thisPost = posts.get(position);

        textMessage.setText(thisPost.getMessageText());
        userName.setText(thisPost.getUserName());
        timeStamp.setText(thisPost.getTimeStamp());
        bussLine.setText(thisPost.getBussLine()+"");

        String dVotes = thisPost.getDownVote()+"";
        String uVotes = thisPost.getUpVote()+"";

        SpannableString voteString = new SpannableString(thisPost.getDownVote() + "/" + thisPost.getUpVote());
        voteString.setSpan(new ForegroundColorSpan(Color.RED), 0, dVotes.length(), 0);
        voteString.setSpan(new ForegroundColorSpan(Color.GREEN), dVotes.length()+1,dVotes.length()+uVotes.length()+1, 0);
        vote.setText(voteString, TextView.BufferType.SPANNABLE);

        return convertView;
    }

}
