package com.tekkom.meawapp;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentCourseContentAdapter extends RecyclerView.Adapter<StudentCourseContentAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentCourseContentSummary> studentCourseContentSummaries;

    public StudentCourseContentAdapter(Context c, ArrayList<StudentCourseContentSummary> p) {
        context = c;
        studentCourseContentSummaries = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.content_recview_student_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String fnamaMateri = studentCourseContentSummaries.get(position).getNamaMateri();
        final String fdeskripsi = studentCourseContentSummaries.get(position).getDeskripsi();
        final String fimage = studentCourseContentSummaries.get(position).getImage();
        final String ffileURL = studentCourseContentSummaries.get(position).getFileURL();
        holder.namaMateri.setText(fnamaMateri);
        Picasso.get().load(studentCourseContentSummaries.get(position).getImage()).into(holder.imageMateri);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context1 = v.getContext();
                Intent intent = new Intent(context1, CourseDetailActivity.class);
                intent.putExtra("namaMateri", fnamaMateri);
                intent.putExtra("deskripsi", fdeskripsi);
                intent.putExtra("image", fimage);
                intent.putExtra("fileURL", ffileURL);
                context1.startActivity(intent);
                Snackbar.make(v, "Clicked element " + fnamaMateri, Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return studentCourseContentSummaries.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaMateri;
        ImageView imageMateri;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            namaMateri = itemView.findViewById(R.id.student_course_title);
            imageMateri = itemView.findViewById(R.id.student_course_thumbnail);
            cardView = itemView.findViewById(R.id.card_view_student_course);
        }
    }
}
