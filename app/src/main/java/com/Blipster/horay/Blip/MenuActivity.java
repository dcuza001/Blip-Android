package com.Blipster.horay.Blip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.Blipster.horay.Blip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    MenuActivityRecyclerAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final ArrayList<MenuActivityRecyclerInfo> data = new ArrayList<>();

        //final Semaphore semaphore = new Semaphore(0);
        final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        //TODO:
        if(username != null) {
            ref.child("clients").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot usersSnapshot) {

                    if (usersSnapshot.child("following").exists()) {

                        ref.child("clients").child(username).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot usersSnapshot) {

                                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                                    String friend = userSnapshot.getKey().toString();

                                    data.add(new MenuActivityRecyclerInfo(friend));
                                }

                                //Inflate Recycler View Object
                                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

                                //Create Adapter
                                adapter = new MenuActivityRecyclerAdapter(MenuActivity.this, data);

                                //Set Layout Manager
                                recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

                                //Set Adapter
                                recyclerView.setAdapter(adapter);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }

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