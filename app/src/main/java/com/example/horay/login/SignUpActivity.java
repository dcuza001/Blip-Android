package com.example.horay.login;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Signature;
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

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://blipster.firebaseio.com/");


                FirebaseAuth auth = FirebaseAuth.getInstance();


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
                auth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                        passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("Poop", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    finish();
                                }

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                                Toast.makeText(SignUpActivity.this, emailEditText.getText().toString()+
                                        passwordEditText.getText().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });



    }

}
