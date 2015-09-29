package com.example.rasmus.busster;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2015-09-12.
 */
public class CustomAdapter extends BaseAdapter {

    Context ctxt;
    ArrayList<Post> posts;
    LayoutInflater myInflater;
    static float x1,x2;
    static float y1, y2;
    static float diffx ,diffy ;



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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = myInflater.inflate(R.layout.busster_post_item,parent,false);
        }

        TextView textMessage = (TextView) convertView.findViewById(R.id.postMessage);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.postTimeStamp);
        TextView bussLine = (TextView) convertView.findViewById(R.id.bussLine);
        TextView vote = (TextView) convertView.findViewById(R.id.votes);

        final Post thisPost = posts.get(position);

        textMessage.setText(thisPost.getMessageText());
        userName.setText(thisPost.getUserName());
        timeStamp.setText(thisPost.getTimeStamp());
        bussLine.setText(thisPost.getBussLine()+"");

        String dVotes = thisPost.getDownVote()+"";
        String uVotes = thisPost.getUpVote()+"";

        SpannableString voteString = new SpannableString(thisPost.getDownVote() + "/" + thisPost.getUpVote());
        voteString.setSpan(new ForegroundColorSpan(Color.RED), 0, dVotes.length(), 0);
        voteString.setSpan(new ForegroundColorSpan(Color.GREEN), dVotes.length() + 1, dVotes.length() + uVotes.length() + 1, 0);
        vote.setText(voteString, TextView.BufferType.SPANNABLE);

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                switch (touchevent.getAction()) {
                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN: {
                        x1 = touchevent.getX();
                        y1 = touchevent.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {

                        x2 = touchevent.getX();
                        y2 = touchevent.getY();
                        diffx = x2 - x1;
                        diffy = y2 - y1;

                        if(diffx > Math.abs(5) ){
                            break;
                        }

                        Toast.makeText(ctxt, diffx +"", Toast.LENGTH_LONG).show();

                        //if left to right sweep event on screen
                        if (x1 < x2 && Math.abs(diffy) < Math.abs(diffx)) {


                            //Toast.makeText(ctxt, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                        }

                        // if right to left sweep event on screen
                        if (x1 > x2 && Math.abs(diffy) < Math.abs(diffx)) {
                            deletePost(position);
                            //Toast.makeText(ctxt, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                        }

                        // if UP to Down sweep event on screen
                        if (y1 < y2 && Math.abs(diffx) < Math.abs(diffy)) {
                            //Toast.makeText(ctxt, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                        }

                        //if Down to UP sweep event on screen
                        if (y1 > y2 && Math.abs(diffy) < Math.abs(diffx)) {
                            //Toast.makeText(ctxt, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
                return true;
            }
        });

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctxt, "click", Toast.LENGTH_LONG).show();
            }
        });
        */

        return convertView;
    }

    public void addNewPost(String message,String userName ,String timeStamp, int bussNumber, int downVote, int upVote ){

        Post post = new Post(message, userName, timeStamp, bussNumber,downVote,upVote);
        posts.add(post);
        notifyDataSetChanged();

    }

    public void deletePost(int position){


        posts.remove(position);
        notifyDataSetChanged();

    }

}
