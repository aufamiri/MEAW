package com.tekkom.meawapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";
    private View view;
    private EditText inputUserName, inputName, inputID;
    private TextView inputStatus, inputDepartement;
    private Button change;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        inputUserName = view.findViewById(R.id.profile_edt_username);
        inputName = view.findViewById(R.id.profile_edt_name);
        inputID = view.findViewById(R.id.profile_edt_id);
        change = view.findViewById(R.id.profile_btn_change);

        inputStatus = view.findViewById(R.id.profile_txv_status);
        inputDepartement = view.findViewById(R.id.profile_txv_departemen);

        getDatabase();

        change.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_btn_change:
                updateDatabase();
                break;
        }

    }

    private void getDatabase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                                inputUserName.setHint(documentSnapshot.getString("username"));
                                inputName.setHint(documentSnapshot.getString("name"));
                                inputStatus.setText(documentSnapshot.getString("status"));
                                inputID.setHint(documentSnapshot.getString("id"));
                                inputDepartement.setHint(documentSnapshot.getString("departement"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void updateDatabase() {
        String userName = inputUserName.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String id = inputID.getText().toString().trim();
        if (inputID.getText().toString().length() >= 4) {
            checkDepartement();
        }
        String departement = inputDepartement.getText().toString().trim();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        if (inputUserName.length() >= 4) {
            user.put("username", userName);
        }
        if (inputName.length() >= 4) {
            user.put("name", name);
        }
        if (inputID.length() >= 4) {
            user.put("id", id);
        }
        if (inputDepartement.length() >= 4) {
            user.put("departement", departement);
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Saving data done", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error saving document", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void checkDepartement() {
        final String departementID = inputID.getText().toString().trim().substring(0, 4);
        switch (departementID) {
            case "0721":
                inputDepartement.setText("Teknik Komputer");
                break;
            case "0711":
                inputDepartement.setText("Teknik Elektro");
                break;
            case "0731":
                inputDepartement.setText("Teknik Biomedik");
                break;
            default:
                inputDepartement.setText("Teknik Komputer");
                break;
        }
    }
}
