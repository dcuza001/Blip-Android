package com.example.horay.login;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ryota on 5/16/2016.
 */
public class Blip {

    public String owner;
    public String comment;
    public double x;
    public double y;
    public String tag;
    public String group = "default";
    public String type = "default";
    public String ID = UUID.randomUUID().toString();
    public String image;
    public String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd").format(new Date());
    public String reply;
    public int rating = 0;

    public Blip(String username, double x, double y, String comment, String tag, String group ,String type, String image) {
        this.owner = "ryocsaito@gmail.com";
        this.x = x;
        this.y = y;
        this.comment = comment;
        this.tag = tag;
        this.group = group;
        this.type = type;
        this.image = image;
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
