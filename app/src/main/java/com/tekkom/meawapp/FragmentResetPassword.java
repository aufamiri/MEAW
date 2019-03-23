package com.tekkom.meawapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class FragmentResetPassword extends Fragment implements View.OnClickListener {

    private final String TAG = "FragmentResetPassword";
    public View view;
    private EditText email;
    private Button send;
    private TextView loginPage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        // Inflate the layout for this fragment
        email = view.findViewById(R.id.forgetpassword_edt_email);
        send = view.findViewById(R.id.forgetpassword_btn_send);
        loginPage = view.findViewById(R.id.forgetpassword_txv_go_to_login_page);

        send.setOnClickListener(this);
        loginPage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgetpassword_btn_send:
                resetPassword();
                break;
            case R.id.forgetpassword_txv_go_to_login_page:
                getFragmentManager().beginTransaction()
                        .replace(R.id.fm_activity_login, new FragmentLogin())
                        .commit();
                break;
        }
    }

    private void resetPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim())
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
