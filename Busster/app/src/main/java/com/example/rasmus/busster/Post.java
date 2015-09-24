package com.example.rasmus.busster;

import android.widget.TextView;

/**
 * Created by Rasmus on 2015-09-24.
 */
public class Post {
    private String messageText;
    private String userName;
    private String timeStamp;
    private int bussLine;
    private int uppVote;
    private int downVote;

    public Post(String messageText, String userName, String timeStamp, int bussLine, int uppVote, int downVote) {
        this.messageText = messageText;
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.bussLine = bussLine;
        this.uppVote = uppVote;
        this.downVote = downVote;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getBussLine() {
        return bussLine;
    }

    public int getUppVote() {
        return uppVote;
    }

    public int getDownVote() {
        return downVote;
    }

}
