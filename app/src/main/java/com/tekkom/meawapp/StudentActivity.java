package com.tekkom.meawapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class StudentActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home_stud:
                    fragment = new StudentHomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_classopt_stud:
                    fragment = new StudentClassOptionsFragment();
                    loadFragment(fragment);
                    Context contextsq = getApplicationContext();
                    CharSequence textsq = "Fragment Opsi Kelas";
                    int durationsq = Toast.LENGTH_SHORT;
                    Toast toastsq = Toast.makeText(contextsq, textsq, durationsq);
                    toastsq.show();

                    return true;
                case R.id.navigation_quizz_stud:
                    fragment = new StudentQuizzFragment();
                    loadFragment(fragment);
                    Context contextsa = getApplicationContext();
                    CharSequence textsa = "Fragment Quizz";
                    int durationsa = Toast.LENGTH_SHORT;

                    Toast toastsa = Toast.makeText(contextsa, textsa, durationsa);
                    toastsa.show();
                    return true;
                case R.id.navigation_profile_stud:
                    fragment = new StudentProfileFragment();
                    loadFragment(fragment);
                    Context contexts = getApplicationContext();
                    CharSequence texts = "Fragment Student profile";
                    int durations = Toast.LENGTH_SHORT;

                    Toast toasts = Toast.makeText(contexts, texts, durations);
                    toasts.show();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        BottomNavigationView navigation = findViewById(R.id.navigationStudent);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new StudentHomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_student, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
