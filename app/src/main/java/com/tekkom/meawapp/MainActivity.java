package com.tekkom.meawapp;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;

public class MainActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, LectureMainActivity.class));
        } else {
            gettingStarted();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        startActivity(new Intent(this, LoginActivity.class));
    }

    void gettingStarted() {
        addSlide(new GettingStarted1Fragment());
        addSlide(new GettingStarted2Fragment());
        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        //askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        askForPermissions(new String[]{Manifest.permission.INTERNET}, 1);
//        askForPermissions(new String[]{Manifest.permission.CAMERA}, 1);

    }
}
