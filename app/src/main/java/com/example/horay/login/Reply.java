package com.example.horay.login;

import java.io.Serializable;

/**
 * Created by Ryota on 5/26/2016.
 */
public class Reply implements Serializable {

    public String user;
    public String reply;

    Reply(){

    }

    Reply(String username, String comment){
        this.user = username;
        this.reply = comment;
    }
}
