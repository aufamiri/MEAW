package com.tekkom.meawapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDetailBook extends Fragment {

    private View view;
    private TextView titleText, descText;
    private ImageView coverImg;
    private Book book;

    public FragmentDetailBook(Book book) {
        this.book = book;
    }

    public FragmentDetailBook() {
        this.book = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInsanceState) {
        view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        titleText = view.findViewById(R.id.book_title);
        descText = view.findViewById(R.id.book_desc);
        coverImg = view.findViewById(R.id.book_cover);

        titleText.setText(book.getNamaMateri());
        descText.setText(book.getDeskripsi());

        Glide.with(getContext()).load(book.getCoverURL()).into(coverImg);

        return view;
    }

}
