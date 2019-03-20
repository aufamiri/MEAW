package com.tekkom.meawapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        String namaMateri = intent.getStringExtra("namaMateri");
        String deskripsi = intent.getStringExtra("deskripsi");
        String image = intent.getStringExtra("image");
        TextView tnamaMateri = findViewById(R.id.namaMateri);
        TextView tdeskripsi = findViewById(R.id.deskripsiMateri);
        ImageView timage = findViewById(R.id.backDetail);

        tnamaMateri.setText(namaMateri);
        tdeskripsi.setText(deskripsi);
        Picasso.get().load(image).into(timage);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.myCollap);
        AppBarLayout appBarLayout = findViewById(R.id.myAppBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("INFORMASI DETAIL");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }
}
