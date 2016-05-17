package com.example.horay.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.firebase.client.Firebase;
import com.firebase.client.utilities.Base64;

import java.io.ByteArrayOutputStream;

public class AddBlip extends AppCompatActivity {
    EditText commentText;
    ImageView cameraPic;
    RadioGroup rg;

    int CAMERA_PIC_REQUEST;
    int color;

    double latitude;
    double longitude;

    Firebase ref = new Firebase("https://blipster.firebaseio.com/");

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
        return Base64.encodeBytes(bb);

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

         //Firebase userRef = ref.child("blips");
//        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//        Blip newMarker = new Blip("ryocsaito@gmail.com", loc.latitude, loc.longitude , "hiii" );
//        userRef.push().setValue(newMarker);


    }


}
