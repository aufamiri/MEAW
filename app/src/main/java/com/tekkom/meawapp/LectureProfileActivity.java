package com.tekkom.meawapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class LectureProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* TODO:merapihkan fungsi (getDatabase()) untuk NavigationView kedalam fragment tersendiri */

    private static final String TAG = "LectureProfileActivity";
    private TextView profileName, profileID;
    private View navView;
    private NavigationView navigationView;

    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_profile);
        Toolbar toolbar = findViewById(R.id.lecture_toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        ///new
        //tabLayout = findViewById(R.id.lecture_tab_bar);
        viewPager = findViewById(R.id.lecture_viewpager);

        LectureTabPagerAdapter lectureTabPagerAdapter = new LectureTabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(lectureTabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        navigationView = findViewById(R.id.nav_view);
//        navView = navigationView.getHeaderView(0);
//        profileName = navView.findViewById(R.id.navheaderlectureprofile_name);
 //       profileID = navView.findViewById(R.id.navheaderlectureprofile_id);
//        getDatabase();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (getSupportFragmentManager().findFragmentById(R.id.profile_activity_fragment_area) instanceof FragmentHome) {
//                startActivity(new Intent(Intent.ACTION_MAIN)
//                        .addCategory(Intent.CATEGORY_HOME)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            } else {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.profile_activity_fragment_area, new FragmentHome())
//                        .commit();
//            }
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lecture_profile, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case R.id.navbot_up_book:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.profile_activity_fragment_area, new FragmentUpload())
//                        .commit();
//                return true;
//            case R.id.navbot_dw_book:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.profile_activity_fragment_area, new DownloadBooksFragment())
//                        .commit();
//                return true;
//            case R.id.navbot_exp_book:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.profile_activity_fragment_area, new ExploreBooksFragment())
//                        .commit();
//                return true;
//            case R.id.navbot_home:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.profile_activity_fragment_area, new FragmentHome())
//                        .commit();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        Fragment fragment = null;
//
//        switch (item.getItemId()) {
//            case R.id.nav_account_profile:
//                fragment = new FragmentProfile();
//                break;
//            case R.id.nav_contacts:
//                fragment = new FragmentContacts();
//                break;
//            case R.id.nav_schedule:
//                fragment = new ScheduleFragment();
//                break;
//            case R.id.nav_share:
//                fragment = new ShareFragment();
//                break;
//            case R.id.nav_feedback:
//                fragment = new FeedbackFragment();
//                break;
//            case R.id.nav_about_us:
//                fragment = new FragmentAboutUs();
//                break;
//            case R.id.nav_settings:
//                fragment = new SettingFragment();
//                break;
//            case R.id.nav_help_center:
//                fragment = new FragmentHelpCenter();
//                break;
//            case R.id.navbot_up_book:
//                fragment = new FragmentUpload();
//                break;
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.profile_activity_fragment_area, fragment);
//            fragmentTransaction.commit();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getDatabase() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                                profileName.setText(documentSnapshot.getString("name"));
                                //profileID.setText(documentSnapshot.getString("id"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
