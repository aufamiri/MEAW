package com.tekkom.meawapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

import java.util.ArrayList;

public class GettingStartedActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new GettingStarted1Fragment());
        addSlide(new GettingStarted2Fragment());
        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();

//        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        askForPermissions(new String[]{Manifest.permission.INTERNET}, 1);
//        askForPermissions(new String[]{Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        startActivity(new Intent(this, LoginActivity.class));
    }
}
