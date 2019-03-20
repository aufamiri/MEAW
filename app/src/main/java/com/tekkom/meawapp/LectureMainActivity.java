package com.tekkom.meawapp;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LectureMainActivity extends AppCompatActivity
        implements ContainerFragment.TabLayoutSetupCallback, PageFragment.OnListItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;


    private NavigationView navigationView;

    private View navView;
    private TextView profileName;
    private ImageView photoProfile;


    @Override
    public void onListItemClick(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_main);

        toolbar = (Toolbar) findViewById(R.id.lecture_toolbar);
        setSupportActionBar(toolbar);


        getDatabase();

        navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);
        profileName = navView.findViewById(R.id.navheaderlectureprofile_name);
        photoProfile = navView.findViewById(R.id.photo_profile);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.lecture_container, new ContainerFragment());
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lecture_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.lecture_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
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
                fragment = new UploadBookFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lecture_container, fragment);
            fragmentTransaction.commit();
        }

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
                                profileName.setText(documentSnapshot.getString("name"));
                                String url = documentSnapshot.getString("photoProfile");
                                if(url != "") {
                                    Picasso.get().load(url).into(photoProfile);
                                }
                                //Glide.with(this).load(documentSnapshot.getString("photoProfile")).into(findViewById(R.id.imageView));
                                //profileID.setText(documentSnapshot.getString("id"));
                            } else {
                            }
                        } else {
                        }
                    }
                });
    }
}
