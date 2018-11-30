package com.tekkom.meawapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHomeFragment extends Fragment {

    DatabaseReference reference;
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
        recyclerView = view.findViewById(R.id.student_course_rv);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        reference = FirebaseDatabase.getInstance().getReference().child("Materi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentSummaryArrayList = new ArrayList<StudentCourseContentSummary>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    StudentCourseContentSummary p = dataSnapshot1.getValue(StudentCourseContentSummary.class);
                    contentSummaryArrayList.add(p);
                }
                studentCourseContentAdapter = new StudentCourseContentAdapter(getActivity(), contentSummaryArrayList);
                recyclerView.setAdapter(studentCourseContentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
