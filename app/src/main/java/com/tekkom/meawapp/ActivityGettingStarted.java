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


        String desk1 = "Dapatkan akses gratis ke seluruh buku populer tanpa harus langganan!";
        String desk2 = "Beritahu dunia tentang karya-karyamu!";
        String desk3 = "Nikmati seluruh fitur yang kami berikan :)";

        addSlide(AppIntro2Fragment.newInstance("IT'S FREE!", desk1, R.drawable.money, R.color.backgroundLight2));
        addSlide(AppIntro2Fragment.newInstance("REALIZE YOUR DREAM!", desk2, R.drawable.dream, R.color.backgroundLight3));
        addSlide(AppIntro2Fragment.newInstance("ENJOY!", desk3, R.drawable.smile, R.color.backgroundLight4));

        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 3);

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
