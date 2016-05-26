package com.example.horay.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddBlip extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText commentText;
    ImageView cameraPic;
    RadioGroup rg;
    Spinner spinnerTags;


    int CAMERA_PIC_REQUEST;
    int color;
    String picLoc = "";

    String tag = "Default";
    String username;
    Blip blipToSend;
    double latitude;
    double longitude;
    String url;
    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");

    private void uploadData(String ID){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9136517748583089120.appspot.com");

        cameraPic.setDrawingCacheEnabled(true);
        cameraPic.buildDrawingCache();
        Bitmap bitmap = cameraPic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/" + ID
        + ".jpg");

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }


        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri uri = taskSnapshot.getDownloadUrl();
                Log.d("LINK", uri.toString());
                blipToSend.pic = uri.toString();
                DatabaseReference userRef = ref.child("blips_ryota");
                userRef.child(blipToSend.ID).setValue(blipToSend);
            }
        });


    }


    private int getMarkerColor(RadioGroup rg){

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.buttonRed:
                        color =  0;
                        break;

                    case R.id.buttonBlue:
                        color =  1;
                        break;

                    case R.id.buttonGreen:
                        color =  2;
                        break;

                    case R.id.buttonYellow:
                        color =  3;
                        break;

                }
            }
        });

        return color;
    }



    private void setupSpinner(){
        spinnerTags.setOnItemSelectedListener( this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        //Add categories here
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerTags.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blip);

        Intent intent = getIntent();
        latitude = intent.getExtras().getDouble("Lat");
        longitude = intent.getExtras().getDouble("Long");
        username = intent.getExtras().getString("Username");

        commentText = (EditText)findViewById(R.id.commentInput);
        cameraPic = (ImageView)findViewById(R.id.cameraPic);
        rg = (RadioGroup)findViewById(R.id.colorGroup);
        spinnerTags = (Spinner) findViewById(R.id.spinnerTags);
        setupSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if(data.getExtras().get("data") != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                cameraPic.setImageResource(android.R.color.transparent);
                cameraPic.setImageBitmap(image);
            }
        }
    }


    public void takePicture(View v){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }

    public void submitPing(View v){

        //get all fields
        String comment = commentText.getText().toString();
        String s = tag;
        int color = getMarkerColor(rg);

        //make marker
        blipToSend = new Blip(username, latitude, longitude, comment, s , "Default");
        uploadData(blipToSend.ID);
        this.finish();



    }


    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        tag = item;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
