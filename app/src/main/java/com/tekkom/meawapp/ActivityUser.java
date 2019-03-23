package com.tekkom.meawapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class ActivityUser extends AppCompatActivity
        implements ContainerFragment.TabLayoutSetupCallback, PageFragment.OnListItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;


    private NavigationView navigationView;

    private View navView;
    private TextView profileName;
    private ImageView photoProfile;

    private TabPagerAdapter tabPagerAdapter;
    private ViewPager viewPager;


    private void initViewPager(ViewPager pViewPager) {
        TabPagerAdapter pTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        pTabPagerAdapter.addFragment(new FragmentHome(), "HOME");
        pTabPagerAdapter.addFragment(new FragmentUpload(), "UPLOAD");
        pViewPager.setAdapter(pTabPagerAdapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_account_profile:
                fragment = new FragmentProfile();
                break;
            case R.id.nav_contacts:
                fragment = new FragmentContacts();
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
                fragment = new FragmentAboutUs();
                break;
            case R.id.nav_settings:
                fragment = new SettingFragment();
                break;
            case R.id.nav_help_center:
                fragment = new FragmentHelpCenter();
                break;
            case R.id.navbot_up_book:
                fragment = new FragmentUpload();
                break;
        }

        fragmentChanger(fragment);


        return true;
    }


    @Override
    public void onListItemClick(String title) {

        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setupTabLayout(ViewPager viewPager) {

        TabLayout tabLayout = findViewById(R.id.main_tab_layout);

        tabLayout.setupWithViewPager(viewPager);

    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_main);


        toolbar = findViewById(R.id.lecture_toolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);
        profileName = navView.findViewById(R.id.navheaderlectureprofile_name);
        photoProfile = navView.findViewById(R.id.photo_profile);


        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
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
        //Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.vp_main);
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.lecture_container);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!(fragment instanceof FragmentHome)) {
                fragmentChanger(FragmentHome.newInstance());
            } else {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
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
        switch (item.getItemId()) {
        }

        return super.onOptionsItemSelected(item);
    }


    void fragmentChanger(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lecture_container, fragment);
            fragmentTransaction.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, "ERROR CHANGE FRAGMENT", Toast.LENGTH_LONG).show();
        }
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
                                if (url != "") {
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
