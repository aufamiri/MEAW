package com.tekkom.meawapp;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHomeFragment extends Fragment {

    DatabaseReference reference, referenceB;
    String getname;
    String currentNrp;
    private RecyclerView recyclerView;
    private ArrayList<StudentCourseContentSummary> contentSummaryArrayList;
    private StudentCourseContentAdapter studentCourseContentAdapter;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment newInstance(String param1, String param2) {
        StudentHomeFragment fragment = new StudentHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);
        contentSummaryArrayList = new ArrayList<StudentCourseContentSummary>();
        recyclerView = view.findViewById(R.id.student_course_rv);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

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
                                currentNrp = documentSnapshot.getString("id");
                                reference = FirebaseDatabase.getInstance().getReference("MateriPengguna").child(currentNrp);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            referenceB = FirebaseDatabase.getInstance().getReference("MateriPengguna");
                                            referenceB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.child(currentNrp).getChildren().iterator();
                                                        while (dataSnapshotIterator.hasNext())
                                                        {
                                                            DataSnapshot dataSnapshot1 = dataSnapshotIterator.next();
                                                            getname = dataSnapshot1.getValue(String.class);
                                                            reference = FirebaseDatabase.getInstance().getReference("Materi").child(getname);
                                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if(dataSnapshot.exists())
                                                                    {
                                                                        StudentCourseContentSummary p = dataSnapshot.getValue(StudentCourseContentSummary.class);
                                                                        contentSummaryArrayList.add(p);
                                                                    }
                                                                    studentCourseContentAdapter = new StudentCourseContentAdapter(getActivity(), contentSummaryArrayList);
                                                                    recyclerView.setAdapter(studentCourseContentAdapter);
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
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
