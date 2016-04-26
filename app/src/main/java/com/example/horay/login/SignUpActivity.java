package com.example.horay.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by HoRay on 4/13/2016.
 */
public class SignUpActivity extends AppCompatActivity{

    EditText emailEditText;
    EditText passwordEditText;
    Button signUpButton;
    EditText fullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        //fullNameEditText = (EditText) findViewById(R.id.fullName);

        //Email works as long as you include @ and .com
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase myFirebaseRef = new Firebase("https://blipster.firebaseio.com/");
                myFirebaseRef.createUser(emailEditText.getText().toString(), passwordEditText.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(SignUpActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Blip_Map.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(SignUpActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
                        Log.e("Account Creation Error", firebaseError.toString() );
                    }
                });
            }
        });

    }


}
