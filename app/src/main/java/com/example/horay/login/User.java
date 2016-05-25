package com.example.horay.login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoRay on 5/24/2016.
 */
public class User {
    public String email;
    public List<String> following;
    public String password;

    public User(String email, String password){
        this.email = email;
        following = new ArrayList<>();
        this.password = password;

    }
    public User(){
    }
}
