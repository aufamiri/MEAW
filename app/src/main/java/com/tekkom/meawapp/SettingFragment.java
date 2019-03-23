package com.tekkom.meawapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SettingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingFragment";
    public Activity activity;
    public View view;
    public TextView logout;
    private TextView reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        logout = view.findViewById(R.id.settings_frg_tv_log_out);
        reset = view.findViewById(R.id.settings_frg_tv_reset_password);

        reset.setOnClickListener(this);
        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_frg_tv_log_out:
                FirebaseAuth.getInstance()
                        .signOut();
                startActivity(new Intent(getActivity(), ActivityLogin.class));
                break;
            case R.id.settings_frg_tv_reset_password:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Successfully sent", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d(TAG, "Email sent.");
                        } else {
                            Toast.makeText(getActivity(), "Error to sent", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error to sent", Toast.LENGTH_SHORT)
                                .show();
                        Log.w(TAG, "Error reset password:", e);
                    }
                });
    }
}
