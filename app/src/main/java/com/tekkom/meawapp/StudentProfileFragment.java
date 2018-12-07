package com.tekkom.meawapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    private TextView userName, department, email, nrp, nohp, completeName;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    public static StudentProfileFragment newInstance(String param1, String param2){
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        userName = view.findViewById(R.id.userName);
        department = view.findViewById(R.id.userDepartment);
        email = view.findViewById(R.id.userEmail);
        nrp = view.findViewById(R.id.userNrp);
        nohp = view.findViewById(R.id.userContact);
        completeName = view.findViewById(R.id.namaPengguna);

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
                        //Toast.makeText(getContext(), "DocumentSnapshot data: " + documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                        userName.setText("Username : "+documentSnapshot.getString("username"));
                        nrp.setText("NRP : "+documentSnapshot.getString("id"));
                        email.setText("Email : "+documentSnapshot.getString("email"));
                        completeName.setText(documentSnapshot.getString("name"));
                        department.setText("Departemen : "+documentSnapshot.getString("departement"));

                    } else {
                        Toast.makeText(getContext(), "No such a document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "get failed with ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
