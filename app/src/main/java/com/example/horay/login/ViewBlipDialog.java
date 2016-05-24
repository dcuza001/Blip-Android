package com.example.horay.login;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBlipDialog extends DialogFragment {

    ListView replyView;
    Button followButton;
    Button replyButton;

    ImageView image;
    ImageView likeImage;
    ImageView dislikeImage;

    TextView usernameText;
    TextView commentText;
    TextView numLikes;
    TextView numDislikes;
    TextView numReplies;
    TextView tagView;
    //get info from this
    Blip blip;

    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");


    private void setFields(){
        usernameText.setText(blip.owner);
        commentText.setText(blip.comment);
        numLikes.setText(Integer.toString(blip.likes));
        numDislikes.setText(Integer.toString(blip.dislikes));
        tagView.setText(blip.tag);
    }

    private void setListeners(){

        String ID = blip.ID;
        final DatabaseReference userRef = ref.child("blips_ryota").child(blip.ID);


        followButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        likeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                blip.likes++;
                numLikes.setText(Integer.toString(blip.likes));
                Map<String, Object> updates = new HashMap<>();
                updates.put("likes", blip.likes);
                userRef.updateChildren(updates);

            }
        });

        dislikeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                blip.dislikes++;
                numDislikes.setText(Integer.toString(blip.dislikes));
                Map<String, Object> updates = new HashMap<>();
                updates.put("dislikes", blip.dislikes);
                userRef.updateChildren(updates);
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

    }

    public ViewBlipDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        blip = (Blip) bundle.getSerializable("BlipObj");
        View view =  inflater.inflate(R.layout.fragment_view_blip, container, false);

        image = (ImageView) view.findViewById(R.id.BlipImage);
        likeImage = (ImageView) view.findViewById(R.id.LikeImg);
        dislikeImage = (ImageView) view.findViewById(R.id.DislikeImg);

        usernameText = (TextView) view.findViewById(R.id.usernameView);
        commentText =  (TextView) view.findViewById(R.id.commentView);
        numLikes = (TextView) view.findViewById(R.id.LikeNum);
        numDislikes = (TextView) view.findViewById(R.id.DislikeNum);
        numReplies = (TextView) view.findViewById(R.id.commentNum);
        tagView = (TextView)view.findViewById(R.id.TagView);

        followButton = (Button) view.findViewById(R.id.buttonFollow);
        replyButton = (Button) view.findViewById(R.id.replyButton);



        setFields();
        setListeners();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}
