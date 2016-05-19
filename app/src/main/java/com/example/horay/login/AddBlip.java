package com.example.horay.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBlip extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText commentText;
    ImageView cameraPic;
    RadioGroup rg;
    Spinner spinnerTags;

    int CAMERA_PIC_REQUEST;
    int color;

    double latitude;
    double longitude;

    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");


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

    private String convertImgString(ImageView imageView){

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
        byte[] bb = bos.toByteArray();
        return Arrays.toString(bb);


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
        categories.add("Friends");
        categories.add("Personal");
        categories.add("Travel");
        categories.add("ryocsaito@gmail.com");

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

        commentText = (EditText)findViewById(R.id.commentInput);
        cameraPic = (ImageView)findViewById(R.id.cameraPic);
        rg = (RadioGroup)findViewById(R.id.colorGroup);
        spinnerTags = (Spinner) findViewById(R.id.spinnerTags);
        setupSpinner();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            cameraPic.setImageResource(android.R.color.transparent);
            cameraPic.setImageBitmap(image);
        }
    }


    public void takePicture(View v){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }

    public void submitPing(View v){

        //get all fields
        String comment = commentText.getText().toString();
        String tag;
        int color = getMarkerColor(rg);
        String imageBase64 = convertImgString(cameraPic);

        Blip b = new Blip("ryocsaito@gmail.com", latitude, longitude, comment, "Default" ,"Default", "Default", imageBase64);
        DatabaseReference userRef = ref.child("blips_ryota");
        userRef.push().setValue(b);

        this.finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
