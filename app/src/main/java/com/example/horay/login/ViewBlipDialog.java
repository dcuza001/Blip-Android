package com.example.horay.login;


import android.content.Context;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    View view;
    EditText replyText;

    List<Reply> replyList;
    String username;

    ArrayAdapter<String> dataAdapter;
    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");


    private void setFields(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);



        usernameText.setText(blip.owner);
        commentText.setText(blip.comment);
        numLikes.setText(Integer.toString(blip.likes));
        numDislikes.setText(Integer.toString(blip.dislikes));
        tagView.setText(blip.tag);

        ImageLoader.getInstance().displayImage(blip.url, image);
        //Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(image);


    }

    private void setListeners(){

        final DatabaseReference userRef = ref.child("aaa").child(blip.ID);
        followButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ref.child("clients")
                        .child(username)
                        .child("following")
                        .child(blip.owner).setValue("");
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
                replyList.add(new Reply(username, replyText.getText().toString()));
                blip.replies = replyList;
                userRef.child("replies").setValue(replyList);
                Toast.makeText(getContext(), blip.replies.toString(), Toast.LENGTH_SHORT).show();
                dataAdapter.notifyDataSetChanged();
                //dataAdapter.notifyDataSetInvalidated();
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
        username = (String)bundle.getSerializable("Username");
        view =  inflater.inflate(R.layout.fragment_view_blip, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        replyText = (EditText)view.findViewById(R.id.editTextReply);

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

        if(blip.replies != null)
            replyList = blip.replies;

        else
            replyList = new ArrayList<>();

        //dataAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_2, replyList);

        Adapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_2,replyList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TwoLineListItem row;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem)inflater.inflate(android.R.layout.simple_list_item_2, null);
                }else{
                    row = (TwoLineListItem)convertView;
                }
                Reply data = replyList.get(position);
                row.getText1().setText(data.reply);
                row.getText2().setText(data.user);

                return row;
            }
        };


        replyView =(ListView) view.findViewById(R.id.listViewReplies) ;
        replyView.setAdapter((ListAdapter) adapter);


        setFields();
        setListeners();
    }



}
