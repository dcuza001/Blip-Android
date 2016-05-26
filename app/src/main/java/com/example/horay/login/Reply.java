package com.example.horay.login;

/**
 * Created by Ryota on 5/26/2016.
 */
public class Reply {

    public String user;
    public String reply;

    Reply(){

    }

    Reply(String username, String comment){
        this.user = username;
        this.reply = comment;
    }
}
