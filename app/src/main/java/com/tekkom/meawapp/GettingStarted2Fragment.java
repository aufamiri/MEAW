package com.tekkom.meawapp;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class GettingStarted2Fragment extends Fragment implements View.OnClickListener {

    protected View view;
    private static final int REQUEST_PERMISSION = 9;
    private static final int REQUEST_WRITE_STORAGE = 10;
    private static final int REQUEST_READ_STORAGE = 11;
    private static final int REQUEST_INTERNET = 12;
    private static final int REQUEST_CAMERA = 13;
    protected Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_getting_started2, container, false);
        button = view.findViewById(R.id.getting_started2_btn);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getting_started2_btn:
                checkPermission();

                break;
        }
    }

    private void checkPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        ArrayList<Integer> requestCodeList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            requestCodeList.add(REQUEST_WRITE_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            requestCodeList.add(REQUEST_READ_STORAGE);

        }

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            permissionList.add(Manifest.permission.INTERNET);
            requestCodeList.add(REQUEST_INTERNET);


        }

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            permissionList.add(Manifest.permission.CAMERA);
            requestCodeList.add(REQUEST_CAMERA);

        }


//        for (int i = 0; i < permissionList.size(); i++) {
//
//            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{permissionList.get(i)}, requestCodeList.get(i));
//        }


        String[] Permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA };

        if(!hasPermissions(getContext(), Permissions)){

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), Permissions, 112);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                break;
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                break;
            case REQUEST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                break;
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                break;
            case 112:
                break;

        }
    }

    public static boolean hasPermissions(Context context, String... permissions){



        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){

            for(String permission: permissions){

                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){

                    return  false;

                }

            }

        }

        return true;

    }

}
