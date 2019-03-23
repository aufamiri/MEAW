package com.tekkom.meawapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFragment";
    public View view;
    public EditText registerEmail, registerPassword, registerConfirmPassword;
    public TextView registerLoginPage;
    public Button registerButton;

    private Button student, lecture;
    private String status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        registerEmail = view.findViewById(R.id.register_txb_email);
        registerPassword = view.findViewById(R.id.register_txb_password);
        registerConfirmPassword = view.findViewById(R.id.register_txb_confirm_password);

        registerLoginPage = view.findViewById(R.id.register_txv_go_to_login_page);

        registerButton = view.findViewById(R.id.register_btn_create_account);

        student = view.findViewById(R.id.register_btn_student);
        lecture = view.findViewById(R.id.register_btn_lecture);

        student.setTag(R.drawable.sh_lightblue);
        lecture.setTag(R.drawable.sh_lightblue);

        status = "";
        student.setOnClickListener(this);
        lecture.setOnClickListener(this);
        registerLoginPage.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_txv_go_to_login_page:
                getFragmentManager().beginTransaction()
                        .replace(R.id.login_frg_panel, new LoginFragment())
                        .commit();
                break;
            case R.id.register_btn_create_account:
                createNewAccount();
                break;
            case R.id.register_btn_student:
                if (student.getTag().equals(R.drawable.sh_lightblue)) {
                    student.setBackgroundResource(R.drawable.sh_darkerblue);
                    lecture.setBackgroundResource(R.drawable.sh_lightblue);
                    student.setTag(R.drawable.sh_lightblue);
                    status = "Student";
                }
                break;
            case R.id.register_btn_lecture:
                if (lecture.getTag().equals(R.drawable.sh_lightblue)) {
                    lecture.setBackgroundResource(R.drawable.sh_darkerblue);
                    student.setBackgroundResource(R.drawable.sh_lightblue);
                    lecture.setTag(R.drawable.sh_lightblue);
                    status = "Lecture";
                }
                break;
        }
    }

    private void createNewAccount() {
        final String inputEmail = registerEmail.getText().toString().trim();
        String inputPassword = registerPassword.getText().toString().trim();
        String inputConfirmPassword = registerConfirmPassword.getText().toString().trim();

        if (status.equals("") || inputEmail.isEmpty() || (inputPassword.isEmpty()) || (inputPassword.length() < 8) || (!(inputPassword.equals(inputConfirmPassword)))) {
            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {

                if (inputEmail.isEmpty()) {
                    registerEmail.setError("Email cannot blank");
                    registerEmail.requestFocus();
                }

                if (inputPassword.isEmpty()) {
                    registerPassword.setError("Password cannot blank");
                    registerPassword.requestFocus();
                }
            }

            if (inputPassword.length() < 8) {
                registerPassword.setError("Panjang password minimal 8 karakter");
                registerPassword.requestFocus();
            }

            if (!(inputPassword.equals(inputConfirmPassword))) {
                registerConfirmPassword.setError("Password Doesn't match");
                registerConfirmPassword.requestFocus();
            }

            if (status.equals("")) {
                Toast.makeText(getActivity(), "Select your status", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", inputEmail);
                                user.put("status", status);
                                user.put("firsttime", "1");
                                user.put("username", "");
                                user.put("name", "");
                                user.put("id", "");
                                user.put("departement", "");
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Error at database", Toast.LENGTH_LONG).show();
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error create account", e);
                        }
                    });
        }


    }

}
