package com.tekkom.meawapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class FragmentLogin extends Fragment implements View.OnClickListener {
    private static final String TAG = "FragmentLogin";
    public View view;
    private EditText loginEmail, loginPassword;
    private TextView loginForgetPassword, loginCreateAccount;
    private Button loginButton, cancelButton;
    private Dialog loadingDialog, fpDialog;

//    private static final int REQUEST_PERMISSION = 9;
//    private static final int REQUEST_WRITE_STORAGE = 10;
//    private static final int REQUEST_READ_STORAGE = 11;
//    private static final int REQUEST_INTERNET = 12;
//    private static final int REQUEST_CAMERA = 13;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_txv_create_new_account:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fm_activity_login, new FragmentRegister())
                        .commit();
                break;
            case R.id.login_btn_log_in:
                userLogin();
                break;
            case R.id.login_txv_forget_password:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fm_activity_login, new FragmentResetPassword())
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

        fpDialog = new Dialog(getActivity());
        fpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fpDialog.setContentView(R.layout.fingerprint_dialog);
        fpDialog.setCancelable(false);
        fpDialog.show();

        cancelButton = (Button) fpDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fpDialog.dismiss();
            }
        });

        loginCreateAccount = view.findViewById(R.id.login_txv_create_new_account);
        loginForgetPassword = view.findViewById(R.id.login_txv_forget_password);

        loginButton = view.findViewById(R.id.login_btn_log_in);

        loadingDialog = new Dialog(Objects.requireNonNull(getActivity()));
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.progress_bar_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.setCancelable(false);

        loginForgetPassword.setOnClickListener(this);
        loginCreateAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        //checkPermission();
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
                                                            startActivity(new Intent(getActivity(), ActivityHome.class));
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

//    private void checkPermission() {
//        ArrayList<String> permissionList = new ArrayList<>();
//        ArrayList<Integer> requestCodeList = new ArrayList<>();
//
//        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            requestCodeList.add(REQUEST_WRITE_STORAGE);
//        }
//
//        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
//                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            requestCodeList.add(REQUEST_READ_STORAGE);
//
//        }
//
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//
//            permissionList.add(Manifest.permission.INTERNET);
//            requestCodeList.add(REQUEST_INTERNET);
//
//
//        }
//
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            permissionList.add(Manifest.permission.CAMERA);
//            requestCodeList.add(REQUEST_CAMERA);
//
//        }
//
//        String[] Permissions = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.INTERNET,
//                Manifest.permission.CAMERA};
//
//        if (!hasPermissions(getContext(), Permissions)) {
//
//            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), Permissions, 112);
//
//        }
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //
//                }
//                break;
//            case REQUEST_WRITE_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //
//                }
//                break;
//            case REQUEST_INTERNET:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //
//                }
//                break;
//            case REQUEST_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //
//                }
//                break;
//            case 112:
//                break;
//
//        }
//    }
//
//    public static boolean hasPermissions(Context context, String... permissions) {
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//
//            for (String permission : permissions) {
//
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//
//                    return false;
//
//                }
//
//            }
//
//        }
//
//        return true;
//
//    }

}
