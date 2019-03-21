package com.tekkom.meawapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    public View view;
    public EditText loginEmail, loginPassword;
    public TextView loginForgetPassword, loginCreateAccount;
    public Button loginButton;
    public Dialog loadingDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_txv_create_new_account:
                getFragmentManager().beginTransaction()
                        .replace(R.id.login_frg_panel, new RegisterFragment())
                        .commit();
                break;
            case R.id.login_btn_log_in:
                userLogin();
                break;
            case R.id.login_txv_forget_password:
                getFragmentManager().beginTransaction()
                        .replace(R.id.login_frg_panel, new ResetPasswordFragment())
                        .commit();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        loginEmail = view.findViewById(R.id.login_txb_email);
        loginPassword = view.findViewById(R.id.login_txb_password);

        loginCreateAccount = view.findViewById(R.id.login_txv_create_new_account);
        loginForgetPassword = view.findViewById(R.id.login_txv_forget_password);

        loginButton = view.findViewById(R.id.login_btn_log_in);

        loadingDialog = new Dialog(getActivity());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.progress_bar_dialog);
        loadingDialog.setCancelable(false);

        loginForgetPassword.setOnClickListener(this);
        loginCreateAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        return view;
    }

    private void userLogin() {
        loadingDialog.show();
        final String inputEmail = loginEmail.getText().toString().trim();
        String inputPassword = loginPassword.getText().toString().trim();

        if ((inputEmail.isEmpty()) || (inputPassword.isEmpty())) {
            if (inputEmail.isEmpty()) {
                loginEmail.setError("ID cannot blank");
                loginEmail.requestFocus();
            }

            if (inputPassword.isEmpty()) {
                loginPassword.setError("Password cannot blank");
                loginPassword.requestFocus();
            }
    } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    if (documentSnapshot.exists()) {
                                                        Log.d(TAG, "signInWithEmail:success");
                                                        Toast.makeText(getActivity(), "Authentication success", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                                                        loadingDialog.dismiss();
                                                        if (documentSnapshot.getString("firsttime").equals("1")) {
                                                            startActivity(new Intent(getActivity(), FirstLoginActivity.class));
                                                        } else {
                                                            startActivity(new Intent(getActivity(), LectureMainActivity.class));
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "signInWithEmail:error", e);
                                                Toast.makeText(getActivity(), "Authentication error", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();

                                            }
                                        });
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }

                        }
                    });
        }

    }
}
