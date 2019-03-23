package com.tekkom.meawapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;


/*
TODO 1: EDIT MENU TIAP SLIDE
 */

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //startActivity(new Intent(this, ActivityUser.class));
            startActivity(new Intent(this, ActivityLogin.class));
        } else {
            startActivity(new Intent(this, ActivityGettingStarted.class));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
