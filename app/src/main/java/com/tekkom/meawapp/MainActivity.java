package com.tekkom.meawapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, GettingStartedActivity.class));
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(this, LectureMainActivity.class));
//        } else {
//            startActivity(new Intent(this, GettingStartedActivity.class));
//        }
    }
}
