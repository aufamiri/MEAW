package com.tekkom.meawapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LectureProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "LectureProfileActivity";
    private TextView profileName, profileID;
    private View navView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);
        profileName = navView.findViewById(R.id.navheaderlectureprofile_name);
        profileID = navView.findViewById(R.id.navheaderlectureprofile_id);

        getDatabase();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.profile_activity_fragment_area, new HomeFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lecture_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        switch (id) {
            case R.id.navbot_up_book:
                fragment = new UploadBooksFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.navbot_dw_book:
                fragment = new DownloadBooksFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.navbot_exp_book:
                fragment = new ExploreBooksFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.navbot_home:
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
                fragmentTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_account_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_contacts:
                fragment = new ContactsFragment();
                break;
            case R.id.nav_schedule:
                fragment = new ScheduleFragment();
                break;
            case R.id.nav_share:
                fragment = new ShareFragment();
                break;
            case R.id.nav_feedback:
                fragment = new FeedbackFragment();
                break;
            case R.id.nav_about_us:
                fragment = new AboutUsFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingFragment();
                break;
            case R.id.nav_help_center:
                fragment = new HelpCenterFragment();
                break;
            case R.id.navbot_up_book:
                fragment = new UploadBooksFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                                profileName.setText(documentSnapshot.getString("name"));
                                profileID.setText(documentSnapshot.getString("id"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}
