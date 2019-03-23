package com.tekkom.meawapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

import androidx.fragment.app.Fragment;

public class ActivityGettingStarted extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance("Slide 1", "Desk Slide 1", R.drawable.album1, R.color.colorAccent));
        addSlide(AppIntro2Fragment.newInstance("Slide 2", "Desk Slide 2", R.drawable.album2, R.color.grey));
        addSlide(AppIntro2Fragment.newInstance("Slide 3", "Desk Slide 3", R.drawable.album3, R.color.black));
        addSlide(AppIntro2Fragment.newInstance("Slide 4", "Desk Slide 4", R.drawable.album4, R.color.yellowPrimary));

        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
        askForPermissions(new String[]{Manifest.permission.USE_FINGERPRINT}, 1);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, ActivityLogin.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
