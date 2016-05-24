package com.example.horay.login;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ryota on 5/16/2016.
 */
public class Blip implements Serializable {

    public String owner;
    public String comment;
    public double x;
    public double y;
    public String tag;
    public String group = "default";
    public String type = "default";
    public String ID = UUID.randomUUID().toString();
    public String pic;
    public String timeStamp;
    public List<String> replies;
    public int likes;
    public int dislikes;

    public Blip(String username, double x, double y, String comment, String tag, String group ,String type, String image) {

        this.owner = "ryocsaito@gmail.com";
        this.x = x;
        this.y = y;
        this.comment = comment;
        this.tag = tag;
        this.group = group;
        this.type = type;
        this.pic = image;
        this.likes = 0;
        this.dislikes = 0;
        this.timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd").format(new Date());
        this.replies = new ArrayList<>();
        this.replies.add("HIIII");

    }

    //Introducing the dummy constructor
    public Blip() {
    }

//    private String findDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
}

