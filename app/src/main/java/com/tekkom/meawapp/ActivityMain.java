package com.tekkom.meawapp;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


/*
TODO 1: EDIT MENU TIAP SLIDE
 */

public class MainActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, UserMainActivity.class));
            //startActivity(new Intent(this, LoginActivity.class));
        } else {
            gettingStarted();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        startActivity(new Intent(this, LoginActivity.class));
    }

    void gettingStarted() {
        addSlide(AppIntro2Fragment.newInstance("Slide 1","Desk Slide 1", R.drawable.album1, R.color.colorAccent));
        addSlide(AppIntro2Fragment.newInstance("Slide 2","Desk Slide 2", R.drawable.album2, R.color.grey));
        addSlide(AppIntro2Fragment.newInstance("Slide 3","Desk Slide 3", R.drawable.album3, R.color.black));
        addSlide(AppIntro2Fragment.newInstance("Slide 4","Desk Slide 4", R.drawable.album4, R.color.yellowPrimary));

        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
        askForPermissions(new String[]{Manifest.permission.USE_FINGERPRINT}, 1);

    }
}
