package com.example.horay.login;

import com.google.firebase.database.GenericTypeIndicator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ryota on 5/16/2016.
 */
public class Blip implements Serializable {

    public String owner;
    public String comment;
    public double x;
    public double y;
    public String tag;
    public String ID;
    public String url;
    public String timestamp;
    public List<Reply> replies;
    public int likes;
    public int dislikes;

    public Blip(String username, double x, double y, String comment, String tag, String pic ) {

        this.owner = username;
        this.x = x;
        this.y = y;
        this.comment = comment;
        this.tag = tag;
        this.url = "";
        this.likes = 0;
        this.dislikes = 0;
        this.timestamp = new SimpleDateFormat("HH:mm:ss MM/dd").format(new Date());
        this.replies = new ArrayList<Reply>();
        this.ID = Long.toString(System.currentTimeMillis())+this.owner ;


    }

    //Introducing the dummy constructor
    public Blip() {
    }

}

