package com.example.horay.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Signature;
import java.util.Map;

/**
 * Created by HoRay on 4/13/2016.
 */
public class SignUpActivity extends AppCompatActivity{

    EditText emailEditText;
    EditText passwordEditText;
    Button signUpButton;
    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);



        //Email works as long as you include @ and .com
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://blipster.firebaseio.com/");


                final FirebaseAuth auth = FirebaseAuth.getInstance();


                /*auth.createUserWithEmailAndPassword(
                        emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnSuccessListener(@NonNull Task < AuthResult > task){
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }*/
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String username = usernameEditText.getText().toString();
                if (username.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please insert a username",
                            Toast.LENGTH_SHORT).show();
                }
                if(email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please insert a email",
                            Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please insert a password",
                            Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password must be longer than 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
                else if(username.length() > 0 && username.contains(" ")) {
                    Toast.makeText(SignUpActivity.this, "Username must not have spaces",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if(username != null) {

                            ref.child("clients").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot usersSnapshot) {
                                    boolean dontAdd = false;
                                    for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                                        String usernames = userSnapshot.getKey().toString();

                                        if (usernames.equals(username)) {
                                            dontAdd = true;
                                            break;
                                        }

                                    }
                                    if(dontAdd == true)
                                    {
                                        Toast.makeText(SignUpActivity.this, "Account Creation failed.\n username is invalid",
                                            Toast.LENGTH_SHORT).show();
                                    }else{
                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {


                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                            //TODO:Display Picture
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(username)
                                                                    .setPhotoUri(Uri.parse("test"))
                                                                    .build();

                                                            user.updateProfile(profileUpdates)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {/*
                                                                                Toast.makeText(SignUpActivity.this, "Profile Updated",
                                                                                        Toast.LENGTH_SHORT).show();*/
                                                                            } else {/*
                                                                                Toast.makeText(SignUpActivity.this, "Profile failed",
                                                                                        Toast.LENGTH_SHORT).show();*/
                                                                            }
                                                                        }
                                                                    });
                                                            DatabaseReference clientsRef = ref.child("clients").child(username);
                                                            User info = new User(email, password);
                                                            clientsRef.setValue(info);
                                                            clientsRef.child("following").setValue("");

                                                            finish();
                                                        }

                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                        else {
                                                            Toast.makeText(SignUpActivity.this, "Account Creation failed.\n Email is invalid",
                                                                    Toast.LENGTH_SHORT).show();


                                                        }
                                                        //When testing use this
                                                        /*Toast.makeText(SignUpActivity.this, emailEditText.getText().toString() +
                                                                        passwordEditText.getText().toString(),
                                                                Toast.LENGTH_SHORT).show();*/
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                    }

                }

            }
        });



    }

}
