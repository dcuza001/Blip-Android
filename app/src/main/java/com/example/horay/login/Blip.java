package com.example.horay.login;

import java.util.UUID;

/**
 * Created by Ryota on 5/16/2016.
 */
public class Blip {

    public String owner;
    public String comment;
    public double x;
    public double y;
    public String group = "default";
    public String type = "default";
    public String ID = UUID.randomUUID().toString();


    public Blip(String username, double x, double y, String comment) {
        this.owner = "ryocsaito@gmail.com";
        this.x = x;
        this.y = y;
        this.comment = comment;
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

