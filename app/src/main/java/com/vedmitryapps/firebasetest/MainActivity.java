package com.vedmitryapps.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.loginET)
    EditText login;

    @BindView(R.id.passwordET)
    EditText password;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG21", "onAuthStateChanged:signed_in: " + user.getUid());
                    Intent intent = new Intent(MainActivity.this, ListTask.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("TAG21", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.authentication:
                auth(login.getText().toString(), password.getText().toString());
                break;
            case R.id.registration:
                registration(login.getText().toString(), password.getText().toString());
                break;
        }
    }

    private void auth(final String login, String password){
        mAuth.signInWithEmailAndPassword(login,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG21", "Auth Complete - " + login);
                } else {
                    Log.d("TAG21", "Auth failed - " + login);
                }
            }
        });
    }

    public void registration(final String login, String password){
        mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG21", "Registration Complete - " + login);
                } else {
                    Log.d("TAG21", "Registration failed - " + login);
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
