package com.example.horay.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    MenuActivityRecyclerAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayList<MenuActivityRecyclerInfo> data = new ArrayList<>();
        data.add(new MenuActivityRecyclerInfo("BOB"));
        data.add(new MenuActivityRecyclerInfo("Someone"));
        data.add(new MenuActivityRecyclerInfo("dsfd"));
        data.add(new MenuActivityRecyclerInfo("Someone else"));
        data.add(new MenuActivityRecyclerInfo("BOB"));
        data.add(new MenuActivityRecyclerInfo("Someone"));
        data.add(new MenuActivityRecyclerInfo("dsfd"));
        data.add(new MenuActivityRecyclerInfo("Someone else"));
        data.add(new MenuActivityRecyclerInfo("BOB"));
        data.add(new MenuActivityRecyclerInfo("Someone"));
        data.add(new MenuActivityRecyclerInfo("dsfd"));
        data.add(new MenuActivityRecyclerInfo("Someone else"));

        //Inflate Recycler View Object
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        //Create Adapter
        adapter = new MenuActivityRecyclerAdapter(this, data);

        //Set Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

        //Set Adapter
        recyclerView.setAdapter(adapter);

    }
}